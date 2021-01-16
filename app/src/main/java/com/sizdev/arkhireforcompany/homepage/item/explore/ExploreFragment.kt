package com.sizdev.arkhireforcompany.homepage.item.explore

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.sizdev.arkhireforcompany.R
import com.sizdev.arkhireforcompany.administration.login.LoginActivity
import com.sizdev.arkhireforcompany.databinding.FragmentExploreBinding
import com.sizdev.arkhireforcompany.homepage.item.project.showproject.ProjectPresenter
import com.sizdev.arkhireforcompany.networking.ArkhireApiClient
import com.sizdev.arkhireforcompany.networking.ArkhireApiService
import kotlinx.android.synthetic.main.alert_session_expired.view.*
import kotlinx.coroutines.*
import java.lang.Runnable


class ExploreFragment : Fragment(), ExploreContract.View {

    private lateinit var binding: FragmentExploreBinding
    private lateinit var dialog: AlertDialog
    private lateinit var handler: Handler
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var popupMenu: PopupMenu

    private var presenter: ExplorePresenter? = null
    private var searchKeyword: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore, container, false)

        // Set Service
        setService()

        // Set Up RecyclerView
        setRecyclerView()

        // Show Progress Bar
        showProgressBar()

        // Data Refresh Management
        dataRefreshManager()

        // Manage Pop Up
        popUpManager()

        // Manage Search View
        searchManager()


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        presenter?.bindToView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter?.unbind()
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

    private fun searchManager() {
        binding.tbExplore.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.tbExplore.clearFocus()
                if (query != null) {
                    binding.rvExplore.visibility = View.VISIBLE
                    binding.lnNotFound.visibility = View.GONE
                    handler.removeCallbacksAndMessages(null)
                    presenter?.searchByName(query)
                    searchKeyword = query
                    binding.filter.setOnClickListener {

                        // Pop Up Listener
                        binding.loadingScreen.visibility = View.VISIBLE
                        popupMenu.setOnMenuItemClickListener { menuItem ->
                            val id = menuItem.itemId

                            when (id) {
                                0 -> presenter?.searchByLocation(query)
                                1 -> presenter?.searchByWorkTime(query)
                            }
                            false
                        }

                        popupMenu.show()
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    handler.removeCallbacksAndMessages(null)
                    searchKeyword = newText
                    presenter?.searchByName(newText)
                }
                return false
            }
        })

        binding.tbExplore.setOnCloseListener {
            binding.tbExplore.clearFocus()
            presenter?.getTalent()
            false
        }
    }

    private fun popUpManager() {
        popupMenu = PopupMenu(activity, binding.filter)
        popupMenu.menu.add(Menu.NONE, 0 ,0, "Location")
        popupMenu.menu.add(Menu.NONE, 1 ,1, "WorkTime")
    }

    private fun showProgressBar() {
        binding.loadingScreen.visibility = View.VISIBLE
        binding.progressBar.max = 100
    }

    private fun setRecyclerView() {
        binding.rvExplore.adapter = ExploreAdapter()
        binding.rvExplore.layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)

    }

    private fun setService() {
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        val service = activity?.let { ArkhireApiClient.getApiClient(it)?.create(ArkhireApiService::class.java) }
        presenter = ExplorePresenter(coroutineScope, service)
    }

    private fun dataRefreshManager() {
        handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                presenter?.getTalent()
                handler.postDelayed(this, 60000)
            }
        })
    }



    override fun addExploreList(list: List<ExploreModel>) {
        (binding.rvExplore.adapter as ExploreAdapter).addList(list)
    }

    @SuppressLint("SetTextI18n")
    override fun setError(error: String) {
        when (error){
            "Session Expired !" -> {
                sessionExpiredAlert()
                dialog.show()
            }

            "Search Result Not Found !" -> {
                binding.rvExplore.visibility = View.GONE
                binding.tvQueryNotfound.visibility = View.VISIBLE
                binding.tvQueryNotfound.text = "Search result of $searchKeyword is not found"
            }

            else -> {
                Toast.makeText(activity, error, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun hideProgressBar() {
        binding.loadingScreen.visibility = View.GONE
    }

    private fun sessionExpiredAlert() {
        val view: View = layoutInflater.inflate(R.layout.alert_session_expired, null)

        dialog = activity?.let {
            AlertDialog.Builder(it)
                    .setView(view)
                    .setCancelable(false)
                    .create()
        }!!

        view.bt_okRelog.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(activity, LoginActivity::class.java)
            val sharedPref = requireActivity().getSharedPreferences("Token", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("accID", null)
            editor.apply()
            startActivity(intent)
            activity?.finish()
        }
    }

}
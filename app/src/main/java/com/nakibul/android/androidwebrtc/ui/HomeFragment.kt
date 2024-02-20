package com.nakibul.android.androidwebrtc.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.nakibul.android.androidwebrtc.UserListRecyclerViewAdapter
import com.nakibul.android.androidwebrtc.databinding.FragmentHomeBinding
import com.nakibul.android.androidwebrtc.repository.MainRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(),UserListRecyclerViewAdapter.Listener {
    private lateinit var binding: FragmentHomeBinding
    private val args by navArgs<HomeFragmentArgs>()
    private var userName: String? = null

    @Inject
    lateinit var mainRepository: MainRepository
    private var userAdapter: UserListRecyclerViewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        val action = HomeFragmentDirections.actionHomeFragmentToCallFragment()
    }

    private fun init() {
        userName = args.username.toString()
        subscribeObservers()
        startMyService()
    }

    private fun startMyService() {

    }

    private fun subscribeObservers() {
        setUpRecyclerView()
        mainRepository.observeUsersStatus {
            Log.d("UserList>>>", it.toString())
            userAdapter?.updateList(it)
        }
    }

    private fun setUpRecyclerView(){
        userAdapter = UserListRecyclerViewAdapter(this)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.mainRecyclerView.apply {
            setLayoutManager(layoutManager)
            adapter = userAdapter
        }
    }

    override fun onVideoCallClicked(username: String) {

    }

    override fun onAudioCallClicked(username: String) {

    }

}
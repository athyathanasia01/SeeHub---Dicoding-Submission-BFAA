package com.dicoding.seehub.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.seehub.adapter.UserListAdapter
import com.dicoding.seehub.data.response.UserListResponseItem
import com.dicoding.seehub.databinding.FragmentFollowBinding
import com.dicoding.seehub.model.FollowFactory
import com.dicoding.seehub.model.FollowersViewModel
import com.dicoding.seehub.model.FollowingViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [FollowFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowFragment : Fragment() {

    private var _binding : FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : ViewModel
    private lateinit var adapter: UserListAdapter

    companion object {
        private const val ARG_POSITION = "position"

        fun NewInstance(position : Int) : FollowFragment {
            val fragment = FollowFragment()
            val args = Bundle()
            args.putInt(ARG_POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt(ARG_POSITION) ?: 0
        val user = activity?.intent?.getParcelableExtra<UserListResponseItem>(DetailActivity.EXTRA_USER) as UserListResponseItem

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUserFollow.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvUserFollow.addItemDecoration(itemDecoration)

        binding.rvUserFollow.setHasFixedSize(true)

        val factory = FollowFactory()

        viewModel = if (position == 0) {
            ViewModelProvider(this, factory).get(FollowersViewModel::class.java)
        } else {
            ViewModelProvider(this, factory).get(FollowingViewModel::class.java)
        }

        viewModel.let {
            when (it) {
                is FollowersViewModel -> {
                    it.listFollowers.observe(viewLifecycleOwner) {
                        showListUser(it!!)
                    }
                    it.isLoading.observe(viewLifecycleOwner) {
                        showLoading(it)
                    }
                    it.getListFollowers(user.login)
                }

                is FollowingViewModel -> {
                    it.listFollowers.observe(viewLifecycleOwner) {
                        showListUser(it!!)
                    }
                    it.isLoading.observe(viewLifecycleOwner) {
                        showLoading(it)
                    }
                    it.getListFollowing(user.login)
                }
            }
        }
    }

    private fun showLoading(load : Boolean) {
        if (load) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showListUser(userList : List<UserListResponseItem>) {
        adapter = UserListAdapter()
        adapter.submitList(userList)
        binding.rvUserFollow.adapter = adapter

        adapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserListResponseItem) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USER, data)
                startActivity(intent)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
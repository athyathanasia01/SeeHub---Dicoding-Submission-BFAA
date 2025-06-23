package com.dicoding.seehub.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.seehub.adapter.UserListAdapter
import com.dicoding.seehub.data.Result
import com.dicoding.seehub.data.database.BaseUserItem
import com.dicoding.seehub.data.database.UserFollowersEntity
import com.dicoding.seehub.data.database.UserFollowingEntity
import com.dicoding.seehub.data.response.UserListResponseItem
import com.dicoding.seehub.databinding.FragmentFollowBinding
import com.dicoding.seehub.model.FollowersViewModel
import com.dicoding.seehub.model.ModelFactory
import com.dicoding.seehub.model.FollowingViewModel
import com.dicoding.seehub.model.preferences.ThemeViewModel
import com.dicoding.seehub.utils.toUserListResponseItem

/**
 * A simple [Fragment] subclass.
 * Use the [FollowFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowFragment : Fragment() {

    private var _binding : FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var factory: ModelFactory
    private val followingViewModel : FollowingViewModel by viewModels { factory }
    private val followersViewModel : FollowersViewModel by viewModels { factory }
    private val themeViewModel : ThemeViewModel by viewModels { factory }
    private lateinit var followAdapter: UserListAdapter
    private var tabName: String? = null

    companion object {
        const val ARG_TAB = "tab_name"
        const val TAB_FOLLOWING = "following"
        const val TAB_FOLLOWERS = "followers"

        private const val FOLLOWING_TAG = "FollowingFragment"
        private const val FOLLOWERS_TAG = "FollowersFragment"
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
        tabName = arguments?.getString(ARG_TAB)

        val user = activity?.intent?.getParcelableExtra<UserListResponseItem>(DetailActivity.EXTRA_USER) as UserListResponseItem

        factory = ModelFactory.getFactoryModelInstance(requireActivity(), requireActivity().application)

        themeViewModel.getThemeSetting().observe(viewLifecycleOwner) { isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        followAdapter = UserListAdapter { user ->
            when (tabName) {
                TAB_FOLLOWING -> {
                    if (user.isFavorite) {
                        followingViewModel.deleteUserFollowingFavorite(user as UserFollowingEntity)
                    } else {
                        followingViewModel.insertUserFollowingFavorite(user as UserFollowingEntity)
                    }
                }

                TAB_FOLLOWERS -> {
                    if (user.isFavorite) {
                        followersViewModel.deleteUserFollowersFavorite(user as UserFollowersEntity)
                    } else {
                        followersViewModel.insertUserFollowersFavorite(user as UserFollowersEntity)
                    }
                }
            }
        }

        followAdapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: BaseUserItem) {
                val intent = Intent(requireActivity(), DetailActivity::class.java)
                val dataUser = data.toUserListResponseItem()
                intent.putExtra(DetailActivity.EXTRA_USER, dataUser)
                Log.d(FOLLOWING_TAG, "onOptionsItemSelected: start to activity detail for user ${dataUser.login}")
                startActivity(intent)
            }
        })

        when (tabName) {
            TAB_FOLLOWING -> {
                followingViewModel.getListFollowingUser(user.login).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                            followAdapter.submitList(null)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            followAdapter.submitList(result.data)
                            binding.rvUserFollow.scrollToPosition(0)
                            if (result.data.isEmpty()) {
                                Toast.makeText(
                                    requireActivity(),
                                    "Tidak ada user following pada user ini",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        is Result.Error -> {
                            showLoading(false)
                            Toast.makeText(
                                requireActivity(),
                                "Error ${result.error}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            TAB_FOLLOWERS -> {
                followersViewModel.getListFollowersUser(user.login).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                            followAdapter.submitList(null)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            followAdapter.submitList(result.data)
                            binding.rvUserFollow.scrollToPosition(0)
                            if (result.data.isEmpty()) {
                                Toast.makeText(
                                    requireActivity(),
                                    "Tidak ada user followers pada user ini",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        is Result.Error -> {
                            showLoading(false)
                            Toast.makeText(
                                requireActivity(),
                                "Error ${result.error}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        binding.rvUserFollow.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = followAdapter
        }
    }

    private fun showLoading(load : Boolean) {
        if (load) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
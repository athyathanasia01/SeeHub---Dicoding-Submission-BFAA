package com.dicoding.seehub.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.seehub.R
import com.dicoding.seehub.adapter.UserListAdapter
import com.dicoding.seehub.data.Result
import com.dicoding.seehub.data.database.BaseUserItem
import com.dicoding.seehub.data.database.UserEntity
import com.dicoding.seehub.databinding.ActivityMainBinding
import com.dicoding.seehub.model.ModelFactory
import com.dicoding.seehub.model.UserViewModel
import com.dicoding.seehub.model.preferences.ThemeViewModel
import com.dicoding.seehub.utils.toUserListResponseItem

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var factory: ModelFactory
    private val viewModel: UserViewModel by viewModels { factory }
    private val themeViewModel: ThemeViewModel by viewModels { factory }
    private lateinit var adapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ModelFactory.getFactoryModelInstance(this, application)

        themeViewModel.getThemeSetting().observe(this) { isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        adapter = UserListAdapter { user ->
            if (user.isFavorite) {
                viewModel.deleteUserFavorite(user as UserEntity)
            } else {
                viewModel.insertUserFavorite(user as UserEntity)
            }
        }

        adapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: BaseUserItem) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                val dataUser = data.toUserListResponseItem()
                intent.putExtra(DetailActivity.EXTRA_USER, dataUser)
                print(dataUser)
                Log.d(TAG, "onOptionsItemSelected: start to activity detail for user ${dataUser.login}")
                startActivity(intent)
            }
        })

        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
            setHasFixedSize(true)
        }

        showListUser(type = ListType.INIT)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { textView, actionId, event ->
                searchBar.setText(searchView.text)
                searchView.hide()
                showListUser(username = searchView.text.toString().trim(), type = ListType.SEARCH)
                searchBar.setText("")
                searchView.clearFocus()
                false
            }
        }

        supportActionBar?.elevation = 0f
        val appBar = binding.appBar
        setSupportActionBar(appBar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.seehub -> {
                val intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        showListUser(type = ListType.INIT)
    }

    override fun onTopResumedActivityChanged(isTopResumedActivity: Boolean) {
        super.onTopResumedActivityChanged(isTopResumedActivity)
        showListUser(type = ListType.INIT)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading(load : Boolean) {
        if (load) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showListUser(username: String = "", type: ListType) {
        val liveData = if (type == ListType.INIT) {
            viewModel.getListUser()
        } else {
            viewModel.getListUser(username)
        }

        liveData.removeObservers(this)
        liveData.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                    adapter.submitList(null)
                }
                is Result.Success -> {
                    showLoading(false)
                    adapter.submitList(result.data)
                    binding.rvUser.scrollToPosition(0)
                    if (result.data.isEmpty()) {
                        Toast.makeText(
                            this,
                            "Data tidak ditemukan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(
                        this,
                        "Error ${result.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    companion object {
        enum class ListType { INIT, SEARCH }
        private const val TAG = "MainActivity"
    }
}
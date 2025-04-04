package com.dicoding.seehub.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.seehub.adapter.UserListAdapter
import com.dicoding.seehub.data.response.UserListResponseItem
import com.dicoding.seehub.databinding.ActivityMainBinding
import com.dicoding.seehub.model.UserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.listUser.observe(this) {
            showListUser(it)
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    viewModel.getListUser(searchView.text.toString().trim())
                    searchBar.setText("")
                    false
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
        val adapter = UserListAdapter()
        adapter.submitList(userList)
        binding.rvUser.adapter = adapter
        binding.rvUser.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        adapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserListResponseItem) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USER, data)
                startActivity(intent)
            }
        })
    }
}
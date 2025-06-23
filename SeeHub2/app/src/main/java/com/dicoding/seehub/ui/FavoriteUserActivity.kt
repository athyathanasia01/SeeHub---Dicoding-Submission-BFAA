package com.dicoding.seehub.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.seehub.adapter.FavoriteUserListAdapter
import com.dicoding.seehub.data.database.UserFavoriteEntity
import com.dicoding.seehub.databinding.ActivityFavoriteUserBinding
import com.dicoding.seehub.model.FavoriteViewModel
import com.dicoding.seehub.model.ModelFactory
import com.dicoding.seehub.model.preferences.ThemeViewModel
import com.dicoding.seehub.utils.toUserListResponseItem

class FavoriteUserActivity : AppCompatActivity() {

    private var _binding: ActivityFavoriteUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var factory: ModelFactory
    private val viewModel: FavoriteViewModel by viewModels { factory }
    private val themeViewModel: ThemeViewModel by viewModels { factory }
    private lateinit var adapter: FavoriteUserListAdapter

    companion object {
        private const val TAG = "FavoriteUserActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ModelFactory.getFactoryModelInstance(this, application)

        themeViewModel.getThemeSetting().observe(this) { isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        adapter = FavoriteUserListAdapter { favorite ->
            viewModel.deleteFromFavorite(favorite)
        }

        adapter.setOnItemClickCallback(object : FavoriteUserListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserFavoriteEntity) {
                val intent = Intent(this@FavoriteUserActivity, DetailActivity::class.java)
                val dataUser = data.toUserListResponseItem()
                intent.putExtra(DetailActivity.EXTRA_USER, dataUser)
                Log.d(TAG, "onOptionsItemSelected: start to activity detail for user ${dataUser.login}")
                startActivity(intent)
            }
        })

        binding.rvUserFavorite.apply {
            layoutManager = LinearLayoutManager(this@FavoriteUserActivity)
            adapter = this@FavoriteUserActivity.adapter
            showListFavorite()
            setHasFixedSize(true)
        }

        supportActionBar?.elevation = 0f
        supportActionBar?.title = "Favorite User"
        supportActionBar?.show()
    }

    private fun showListFavorite() {
        viewModel.getListFavoriteUser().observe(this) { result ->
            if (result != null) {
                adapter.submitList(result)
                binding.rvUserFavorite.scrollToPosition(0)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
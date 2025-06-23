package com.dicoding.seehub.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.seehub.R
import com.dicoding.seehub.adapter.SectionsPagerAdapter
import com.dicoding.seehub.data.response.DetailResponse
import com.dicoding.seehub.data.response.UserListResponseItem
import com.dicoding.seehub.databinding.ActivityDetailBinding
import com.dicoding.seehub.model.DetailViewModel
import com.dicoding.seehub.model.ModelFactory
import com.dicoding.seehub.model.preferences.ThemeViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>()
    private lateinit var factory: ModelFactory
    private val themeViewModel: ThemeViewModel by viewModels { factory }
    private lateinit var detailUser : DetailResponse

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_1_name,
            R.string.tab_2_name
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager : ViewPager2 = binding.viewPager
        val tab : TabLayout = binding.tabLayout
        viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(tab, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        val user = intent?.getParcelableExtra<UserListResponseItem>(EXTRA_USER) as UserListResponseItem

        supportActionBar?.elevation = 0f

        factory = ModelFactory.getFactoryModelInstance(this, application)

        themeViewModel.getThemeSetting().observe(this) { isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.detailUser.observe(this) {
            detailUser = it
            binding.apply {
                tvUsername.setText(it.login)
                tvId.setText(String.format(this@DetailActivity.getString(R.string.id_init), it.id.toString(), it.nodeId))
                tvFollow.setText(String.format(this@DetailActivity.getString(R.string.follow_init), it.followers, it.following))
            }
            Glide.with(this)
                .load(it.avatarUrl)
                .into(binding.imgUserAvatar)
        }

        viewModel.getDetailUser(user.login)

        binding.btnDetailMore.setOnClickListener {
            val dialog = DialogDetailFragment.newInstance(detailUser)
            dialog.show(supportFragmentManager, "ShowDialogFragment")
        }
    }

    private fun showLoading(load : Boolean) {
        if (load) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
package com.dicoding.seehub.ui

import android.content.Intent
import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.dicoding.seehub.databinding.ActivitySettingBinding
import com.dicoding.seehub.model.ModelFactory
import com.dicoding.seehub.model.preferences.ThemeViewModel

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var factory: ModelFactory
    private val viewModel: ThemeViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ModelFactory.getFactoryModelInstance(this, application)

        viewModel.getThemeSetting().observe(this) { isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }

        binding.btnFavorite.setOnClickListener {
            val intent = Intent(this@SettingActivity, FavoriteUserActivity::class.java)
            startActivity(intent)
        }

        binding.btnFaq.setOnClickListener {
            //soon
        }

        supportActionBar?.elevation = 0f
        supportActionBar?.title = "Setting"
        supportActionBar?.show()
    }
}
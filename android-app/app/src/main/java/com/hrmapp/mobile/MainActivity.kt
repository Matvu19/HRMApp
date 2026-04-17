package com.hrmapp.mobile

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.hrmapp.mobile.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            val showBottomNav = when (destination.id) {
                R.id.homeFragment,
                R.id.attendanceFragment,
                R.id.leaveFragment,
                R.id.notificationFragment,
                R.id.profileFragment -> true
                else -> false
            }

            binding.bottomNav.visibility =
                if (showBottomNav) View.VISIBLE else View.GONE

            val bottomPadding = if (showBottomNav) {
                resources.getDimensionPixelSize(R.dimen.bottom_nav_content_padding)
            } else {
                0
            }

            binding.navHostFragment.updatePadding(bottom = bottomPadding)
        }
    }
}
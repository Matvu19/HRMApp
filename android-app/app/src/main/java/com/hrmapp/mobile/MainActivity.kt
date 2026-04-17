package com.hrmapp.mobile

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.hrmapp.mobile.databinding.ActivityMainBinding
import com.hrmapp.mobile.feature.session.SessionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val sessionViewModel: SessionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController

        binding.bottomNav.setupWithNavController(navController)

        // 🔥 Auto login
        sessionViewModel.loadSession {
            if (sessionViewModel.isLoggedIn) {
                navController.navigate(R.id.homeFragment)
            } else {
                navController.navigate(R.id.loginFragment)
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNav.visibility =
                if (destination.id == R.id.homeFragment) View.VISIBLE else View.GONE
        }
    }
}
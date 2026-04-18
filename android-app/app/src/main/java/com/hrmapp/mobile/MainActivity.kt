package com.hrmapp.mobile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var bottomNav: BottomNavigationView
    private var suppressBottomNavCallback = false

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        bottomNav = findViewById(R.id.bottomNavigation)

        setupBottomNav()
        askNotificationPermissionIfNeeded()

        if (savedInstanceState == null) {
            handleIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun setupBottomNav() {
        bottomNav.setOnItemSelectedListener { item ->
            if (suppressBottomNavCallback) return@setOnItemSelectedListener true

            val currentId = navController.currentDestination?.id
            if (currentId == item.itemId) return@setOnItemSelectedListener true

            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.notificationFragment -> {
                    navController.navigate(R.id.notificationFragment)
                    true
                }
                R.id.profileFragment -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
                R.id.settingsFragment -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }
                else -> false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val showBottomNav = destination.id == R.id.homeFragment ||
                    destination.id == R.id.notificationFragment ||
                    destination.id == R.id.profileFragment ||
                    destination.id == R.id.settingsFragment

            bottomNav.visibility = if (showBottomNav) View.VISIBLE else View.GONE

            val targetItemId = when (destination.id) {
                R.id.homeFragment -> R.id.homeFragment
                R.id.notificationFragment -> R.id.notificationFragment
                R.id.profileFragment -> R.id.profileFragment
                R.id.settingsFragment -> R.id.settingsFragment
                else -> null
            }

            if (targetItemId != null && bottomNav.selectedItemId != targetItemId) {
                suppressBottomNavCallback = true
                bottomNav.selectedItemId = targetItemId
                suppressBottomNavCallback = false
            }
        }
    }

    private fun handleIntent(intent: Intent?) {
        val target = intent?.getStringExtra("target") ?: return

        when (target) {
            "approvals" -> navController.navigate(R.id.approvalFragment)
            "dashboard" -> navController.navigate(R.id.dashboardFragment)
            "notifications" -> navController.navigate(R.id.notificationFragment)
            "profile" -> navController.navigate(R.id.profileFragment)
            "settings" -> navController.navigate(R.id.settingsFragment)
            "payroll" -> navController.navigate(R.id.payrollFragment)
            "leave" -> navController.navigate(R.id.leaveFragment)
        }
    }

    private fun askNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

        val granted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (!granted) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
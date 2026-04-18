package com.hrmapp.mobile.feature.settings

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.hrmapp.mobile.BuildConfig
import com.hrmapp.mobile.R
import com.hrmapp.mobile.core.storage.SessionManager
import com.hrmapp.mobile.core.ui.setSafeClickListener
import com.hrmapp.mobile.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentSettingsBinding.bind(view)

        binding.tvAppVersion.text = "Phiên bản: ${BuildConfig.VERSION_NAME}"
        binding.tvBuildType.text = "Build: ${BuildConfig.BUILD_TYPE}"
        binding.tvBaseUrl.text = "API: ${BuildConfig.BASE_URL}"

        binding.btnBackSettings.setSafeClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnClearSavedAccounts.setSafeClickListener {
            MainScope().launch {
                sessionManager.clearSavedAccounts()
                Snackbar.make(binding.root, "Đã xoá toàn bộ tài khoản đã lưu", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.btnLogoutSettings.setSafeClickListener {
            MainScope().launch {
                sessionManager.clearSession()

                val navController = findNavController()
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(navController.graph.startDestinationId, true)
                    .build()

                navController.navigate(
                    R.id.loginFragment,
                    bundleOf("logout_message" to "Bạn đã đăng xuất"),
                    navOptions
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
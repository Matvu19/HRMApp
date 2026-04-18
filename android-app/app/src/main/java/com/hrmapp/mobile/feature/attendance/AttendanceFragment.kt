package com.hrmapp.mobile.feature.attendance

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hrmapp.mobile.core.location.LocationHelper
import com.hrmapp.mobile.core.location.SimpleLocation
import com.hrmapp.mobile.core.storage.AttendanceQueueStore
import com.hrmapp.mobile.core.ui.setSafeClickListener
import com.hrmapp.mobile.databinding.FragmentAttendanceBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AttendanceFragment : Fragment() {

    private var _binding: FragmentAttendanceBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AttendanceViewModel by viewModels()
    private lateinit var adapter: AttendanceAdapter
    private var currentLocation: SimpleLocation? = null

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        refreshLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = AttendanceAdapter()
        binding.rvAttendance.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAttendance.adapter = adapter

        binding.btnGrantLocation.setSafeClickListener {
            requestLocationPermission()
        }

        binding.btnCheckIn.setSafeClickListener {
            viewModel.checkIn(currentLocation)
        }

        binding.btnCheckOut.setSafeClickListener {
            viewModel.checkOut(currentLocation)
        }

        binding.btnRefreshAttendance.setSafeClickListener {
            viewModel.loadHistory()
            refreshLocation()
        }

        binding.btnRetryQueue.setSafeClickListener {
            viewModel.retryQueuedActions()
        }

        refreshLocation()
        viewModel.loadHistory()

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.progressAttendance.visibility =
                if (state.isLoading) View.VISIBLE else View.GONE
            adapter.submitList(state.items)
            binding.tvAttendanceMessage.text = state.message
            binding.tvLastAction.text =
                if (state.message.isNotBlank()) "Lần thao tác gần nhất: ${state.message}"
                else "Lần thao tác gần nhất: chưa có"
            binding.tvQueueCount.text = "Số thao tác đang chờ: ${state.queueCount}"
            binding.tvLocation.text = state.locationText
            binding.tvGpsStatus.text = state.gpsStatus

            if (!state.isLoading && state.message.startsWith("Thành công:")) {
                Snackbar.make(binding.root, state.message, Snackbar.LENGTH_SHORT).show()
            }

            if (!state.isLoading && state.message.contains("hàng chờ", ignoreCase = true)) {
                Snackbar.make(binding.root, state.message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestLocationPermission() {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun refreshLocation() {
        val fineGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!fineGranted && !coarseGranted) {
            currentLocation = null
            viewModel.updateLocation(null)
            return
        }

        val helper = LocationHelper(requireContext())
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            currentLocation = helper.getCurrentLocationOrNull()
            viewModel.updateLocation(currentLocation)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.hrmapp.mobile.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hrmapp.mobile.R
import com.hrmapp.mobile.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnOpenApproval.setOnClickListener {
            findNavController().navigate(R.id.approvalFragment)
        }

        binding.btnOpenDashboard.setOnClickListener {
            findNavController().navigate(R.id.dashboardFragment)
        }

        binding.btnOpenAttendance.setOnClickListener {
            findNavController().navigate(R.id.attendanceFragment)
        }

        binding.btnOpenLeave.setOnClickListener {
            findNavController().navigate(R.id.leaveFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
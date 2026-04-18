package com.hrmapp.mobile.feature.profile

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hrmapp.mobile.databinding.DialogChangePasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordDialogFragment : DialogFragment() {

    private var _binding: DialogChangePasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogChangePasswordBinding.inflate(layoutInflater)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Đổi mật khẩu")
            .setView(binding.root)
            .setNegativeButton("Hủy") { _, _ -> dismiss() }
            .setPositiveButton("Lưu", null)
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val currentPassword = binding.etCurrentPassword.text.toString().trim()
                val newPassword = binding.etNewPassword.text.toString().trim()
                val confirmPassword = binding.etConfirmPassword.text.toString().trim()

                binding.tvChangePasswordMessage.text = ""

                if (currentPassword.isBlank()) {
                    binding.tvChangePasswordMessage.text = "Vui lòng nhập mật khẩu hiện tại"
                    return@setOnClickListener
                }

                if (newPassword.isBlank()) {
                    binding.tvChangePasswordMessage.text = "Vui lòng nhập mật khẩu mới"
                    return@setOnClickListener
                }

                if (newPassword.length < 6) {
                    binding.tvChangePasswordMessage.text = "Mật khẩu mới phải có ít nhất 6 ký tự"
                    return@setOnClickListener
                }

                if (confirmPassword.isBlank()) {
                    binding.tvChangePasswordMessage.text = "Vui lòng xác nhận mật khẩu mới"
                    return@setOnClickListener
                }

                if (newPassword != confirmPassword) {
                    binding.tvChangePasswordMessage.text = "Mật khẩu xác nhận không khớp"
                    return@setOnClickListener
                }

                binding.tvChangePasswordMessage.text = "Đang đổi mật khẩu..."

                viewModel.changePassword(
                    currentPassword = currentPassword,
                    newPassword = newPassword
                ) { success, message ->
                    if (!isAdded) return@changePassword

                    requireActivity().runOnUiThread {
                        binding.tvChangePasswordMessage.text = message
                        if (success) {
                            dismiss()
                        }
                    }
                }
            }
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
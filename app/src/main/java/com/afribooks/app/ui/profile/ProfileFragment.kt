package com.afribooks.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.afribooks.app.data.repository.UserRepository
import com.afribooks.app.databinding.FragmentProfileBinding
import com.afribooks.app.ui.auth.LoginActivity
import com.google.android.material.snackbar.Snackbar

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateUserData()
        setupMenuListeners()
    }

    private fun populateUserData() {
        val user = UserRepository.getLoggedInUser()
        if (user != null) {
            binding.tvUsername.text = user.username
            binding.tvEmail.text    = user.email
        } else {
            binding.tvUsername.text = "Student"
            binding.tvEmail.text    = "student@university.ac.za"
        }
    }

    private fun setupMenuListeners() {
        binding.menuMyListings.setOnClickListener {
            Toast.makeText(requireContext(), "My Listings — coming soon!", Toast.LENGTH_SHORT).show()
        }

        binding.menuMessages.setOnClickListener {
            Toast.makeText(requireContext(), "Messages — coming soon!", Toast.LENGTH_SHORT).show()
        }

        binding.menuSavedBooks.setOnClickListener {
            val saved = com.afribooks.app.data.repository.BookRepository.getSavedBooks()
            val msg = if (saved.isEmpty()) "No saved books yet."
                      else "You have ${saved.size} saved book(s)."
            Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
        }

        binding.menuSettings.setOnClickListener {
            Toast.makeText(requireContext(), "Settings — coming soon!", Toast.LENGTH_SHORT).show()
        }

        binding.menuHelp.setOnClickListener {
            Snackbar.make(
                binding.root,
                "Need help? Email support@afribooks.co.za",
                Snackbar.LENGTH_LONG
            ).show()
        }

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Log Out") { _, _ ->
                    UserRepository.logout()
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    requireActivity().overridePendingTransition(
                        android.R.anim.fade_in, android.R.anim.fade_out
                    )
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.afribooks.app.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afribooks.app.R
import com.afribooks.app.data.repository.UserRepository
import com.afribooks.app.databinding.ActivityRegisterBinding
import com.afribooks.app.utils.ValidationUtils
import com.afribooks.app.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            hideKeyboard()
            if (validateInputs()) performRegister()
        }

        binding.tvLoginLink.setOnClickListener {
            finish()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        listOf(
            binding.etUsername to binding.tilUsername,
            binding.etEmail    to binding.tilEmail,
            binding.etPassword to binding.tilPassword,
            binding.etConfirmPassword to binding.tilConfirmPassword
        ).forEach { (et, til) ->
            et.setOnFocusChangeListener { _, _ -> til.error = null }
        }
    }

    private fun validateInputs(): Boolean {
        var valid = true

        val username = binding.etUsername.text.toString().trim()
        val email    = binding.etEmail.text.toString().trim()
        val pass     = binding.etPassword.text.toString()
        val confirm  = binding.etConfirmPassword.text.toString()

        if (!ValidationUtils.isNotBlank(username)) {
            binding.tilUsername.error = getString(R.string.field_required); valid = false
        } else binding.tilUsername.error = null

        if (!ValidationUtils.isValidEmail(email)) {
            binding.tilEmail.error = getString(R.string.invalid_email); valid = false
        } else binding.tilEmail.error = null

        if (!ValidationUtils.isValidPassword(pass)) {
            binding.tilPassword.error = getString(R.string.password_too_short); valid = false
        } else binding.tilPassword.error = null

        if (!ValidationUtils.passwordsMatch(pass, confirm)) {
            binding.tilConfirmPassword.error = getString(R.string.passwords_no_match); valid = false
        } else binding.tilConfirmPassword.error = null

        return valid
    }

    private fun performRegister() {
        val username = binding.etUsername.text.toString().trim()
        val email    = binding.etEmail.text.toString().trim()
        val pass     = binding.etPassword.text.toString()

        val success = UserRepository.register(username, email, pass)
        if (success) {
            UserRepository.login(email, pass)
            Snackbar.make(binding.root, getString(R.string.register_success), Snackbar.LENGTH_SHORT).show()
            binding.root.postDelayed({
                val intent = Intent(this, com.afribooks.app.ui.home.MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }, 800L)
        } else {
            Snackbar.make(
                binding.root,
                "An account with this email already exists.",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }
}

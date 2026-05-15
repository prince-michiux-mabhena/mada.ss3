package com.afribooks.app.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afribooks.app.data.repository.UserRepository
import com.afribooks.app.databinding.ActivityLoginBinding
import com.afribooks.app.ui.home.MainActivity
import com.afribooks.app.utils.ValidationUtils
import com.afribooks.app.utils.hideKeyboard
import com.afribooks.app.utils.showSnackbar
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            hideKeyboard()
            if (validateInputs()) {
                performLogin()
            }
        }

        binding.tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        binding.tvForgotPassword.setOnClickListener {
            Snackbar.make(
                binding.root,
                "Password reset link sent to your email.",
                Snackbar.LENGTH_LONG
            ).show()
        }

        // Clear errors on text change
        binding.etEmail.setOnFocusChangeListener { _, _ ->
            binding.tilEmail.error = null
        }
        binding.etPassword.setOnFocusChangeListener { _, _ ->
            binding.tilPassword.error = null
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (!ValidationUtils.isValidEmail(email)) {
            binding.tilEmail.error = getString(com.afribooks.app.R.string.invalid_email)
            isValid = false
        } else {
            binding.tilEmail.error = null
        }

        if (!ValidationUtils.isValidPassword(password)) {
            binding.tilPassword.error = getString(com.afribooks.app.R.string.password_too_short)
            isValid = false
        } else {
            binding.tilPassword.error = null
        }

        return isValid
    }

    private fun performLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        val user = UserRepository.login(email, password)
        if (user != null) {
            Snackbar.make(binding.root, "Welcome back, ${user.username}!", Snackbar.LENGTH_SHORT).show()
            binding.root.postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }, 800L)
        } else {
            // Demo mode: allow any valid email/password combo
            if (ValidationUtils.isValidEmail(email) && ValidationUtils.isValidPassword(password)) {
                // Register on the fly for demo
                UserRepository.register(email.substringBefore("@"), email, password)
                UserRepository.login(email, password)
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            } else {
                Snackbar.make(
                    binding.root,
                    "Invalid email or password. Please try again.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
}

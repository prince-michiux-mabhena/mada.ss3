package com.afribooks.app.ui.enquiry

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afribooks.app.R
import com.afribooks.app.databinding.ActivityEnquiryBinding
import com.afribooks.app.utils.ValidationUtils
import com.afribooks.app.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar

class EnquiryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEnquiryBinding

    companion object {
        const val EXTRA_BOOK_TITLE    = "extra_book_title"
        const val EXTRA_SELLER_EMAIL  = "extra_seller_email"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnquiryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bookTitle   = intent.getStringExtra(EXTRA_BOOK_TITLE)   ?: "Unknown Book"
        val sellerEmail = intent.getStringExtra(EXTRA_SELLER_EMAIL) ?: ""

        binding.tvEnquiryBookTitle.text = bookTitle

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        // Clear errors on focus
        listOf(
            binding.etName    to binding.tilName,
            binding.etEmail   to binding.tilEmail,
            binding.etMessage to binding.tilMessage
        ).forEach { (et, til) ->
            et.setOnFocusChangeListener { _, _ -> til.error = null }
        }

        binding.btnSendEnquiry.setOnClickListener {
            hideKeyboard()
            if (validateForm()) sendEnquiry(sellerEmail)
        }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val name    = binding.etName.text.toString().trim()
        val email   = binding.etEmail.text.toString().trim()
        val message = binding.etMessage.text.toString().trim()

        if (!ValidationUtils.isNotBlank(name)) {
            binding.tilName.error = getString(R.string.field_required)
            valid = false
        } else binding.tilName.error = null

        if (!ValidationUtils.isValidEmail(email)) {
            binding.tilEmail.error = getString(R.string.invalid_email)
            valid = false
        } else binding.tilEmail.error = null

        if (!ValidationUtils.isNotBlank(message)) {
            binding.tilMessage.error = getString(R.string.field_required)
            valid = false
        } else if (message.length < 10) {
            binding.tilMessage.error = "Message is too short (min 10 characters)"
            valid = false
        } else binding.tilMessage.error = null

        return valid
    }

    private fun sendEnquiry(sellerEmail: String) {
        val name    = binding.etName.text.toString().trim()
        val email   = binding.etEmail.text.toString().trim()
        val message = binding.etMessage.text.toString().trim()
        val subject = "Enquiry about: ${binding.tvEnquiryBookTitle.text}"
        val body    = "Hi,\n\nMy name is $name and I am interested in your book listing.\n\n$message\n\nYou can reach me at: $email\n\nSent via AfriBooks"

        val mailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL,   arrayOf(sellerEmail))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT,    body)
        }

        if (mailIntent.resolveActivity(packageManager) != null) {
            // A mail app is available — open it pre-filled
            binding.etName.text?.clear()
            binding.etEmail.text?.clear()
            binding.etMessage.text?.clear()
            startActivity(mailIntent)
        } else {
            // No mail app installed — show a friendly fallback Snackbar
            Snackbar.make(
                binding.root,
                "No email app found. Please email $sellerEmail directly.",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}

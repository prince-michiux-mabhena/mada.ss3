package com.afribooks.app.ui.detail

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afribooks.app.R
import com.afribooks.app.data.model.Book
import com.afribooks.app.data.repository.BookRepository
import com.afribooks.app.databinding.ActivityBookDetailBinding
import com.afribooks.app.ui.enquiry.EnquiryActivity
import com.afribooks.app.utils.ValidationUtils
import com.google.android.material.snackbar.Snackbar

class BookDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookDetailBinding

    companion object {
        const val EXTRA_BOOK = "extra_book"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val book = intent.getSerializableExtra(EXTRA_BOOK) as? Book
        if (book == null) {
            finish()
            return
        }

        setupToolbar()
        populateBookData(book)
        setupButtons(book)
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
    }

    private fun populateBookData(book: Book) {
        binding.tvDetailTitle.text       = book.title
        binding.tvDetailPrice.text       = ValidationUtils.formatPrice(book.price)
        binding.tvDetailCourse.text      = "${book.courseName} (${book.courseCode})"
        binding.tvDetailCondition.text   = book.condition.displayName
        binding.tvDetailSeller.text      = book.seller
        binding.tvDetailDescription.text = book.description.ifBlank { "No description provided." }

        // Book cover tint
        try {
            binding.ivBookDetailCover.setBackgroundColor(Color.parseColor(book.coverColorHex))
        } catch (e: IllegalArgumentException) {
            binding.ivBookDetailCover.setBackgroundColor(Color.parseColor("#C0572B"))
        }

        updateSaveButton(book)
    }

    private fun setupButtons(book: Book) {
        binding.btnContactSeller.setOnClickListener {
            val intent = Intent(this, EnquiryActivity::class.java)
            intent.putExtra(EnquiryActivity.EXTRA_BOOK_TITLE, book.title)
            intent.putExtra(EnquiryActivity.EXTRA_SELLER_EMAIL, book.sellerEmail)
            startActivity(intent)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        binding.btnSaveBook.setOnClickListener {
            BookRepository.toggleSaved(book.id)
            val updatedBook = BookRepository.getBookById(book.id) ?: book
            val msg = if (updatedBook.isSaved) getString(R.string.book_saved)
                      else getString(R.string.book_unsaved)
            Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
            updateSaveButton(updatedBook)
        }
    }

    private fun updateSaveButton(book: Book) {
        val savedBook = BookRepository.getBookById(book.id) ?: book
        binding.btnSaveBook.text = if (savedBook.isSaved) "★ Saved" else getString(R.string.save_book)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}

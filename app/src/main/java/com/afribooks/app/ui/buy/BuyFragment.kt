package com.afribooks.app.ui.buy

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.afribooks.app.R
import com.afribooks.app.adapter.BookAdapter
import com.afribooks.app.data.model.BookCondition
import com.afribooks.app.data.repository.BookRepository
import com.afribooks.app.databinding.FragmentBuyBinding
import com.afribooks.app.ui.detail.BookDetailActivity
import com.afribooks.app.utils.hide
import com.afribooks.app.utils.show
import com.google.android.material.snackbar.Snackbar

class BuyFragment : Fragment() {

    private var _binding: FragmentBuyBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookAdapter: BookAdapter
    private val activeConditions = mutableSetOf<BookCondition>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearch()
        setupFilters()
        applyFilters()
    }

    private fun setupRecyclerView() {
        bookAdapter = BookAdapter(
            onBookClick = { book ->
                val intent = Intent(requireContext(), BookDetailActivity::class.java)
                intent.putExtra(BookDetailActivity.EXTRA_BOOK, book)
                startActivity(intent)
                requireActivity().overridePendingTransition(
                    android.R.anim.slide_in_left, android.R.anim.slide_out_right
                )
            },
            onSaveClick = { book ->
                BookRepository.toggleSaved(book.id)
                val msg = if (!book.isSaved) getString(R.string.book_saved)
                          else getString(R.string.book_unsaved)
                Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
                applyFilters()
            }
        )

        binding.rvBooks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = bookAdapter
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { applyFilters() }
        })
    }

    private fun setupFilters() {
        // "All" chip — clears condition filters
        binding.chipAll.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                activeConditions.clear()
                binding.chipLikeNew.isChecked = false
                binding.chipGood.isChecked     = false
                binding.chipFair.isChecked     = false
                binding.chipPoor.isChecked     = false
                applyFilters()
            }
        }

        mapOf(
            binding.chipLikeNew to BookCondition.LIKE_NEW,
            binding.chipGood    to BookCondition.GOOD,
            binding.chipFair    to BookCondition.FAIR,
            binding.chipPoor    to BookCondition.POOR
        ).forEach { (chip, condition) ->
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    activeConditions.add(condition)
                    binding.chipAll.isChecked = false
                } else {
                    activeConditions.remove(condition)
                    if (activeConditions.isEmpty()) binding.chipAll.isChecked = true
                }
                applyFilters()
            }
        }
    }

    private fun applyFilters() {
        val query = binding.etSearch.text.toString().trim()
        val results = BookRepository.searchAndFilter(query, activeConditions)

        bookAdapter.submitList(results)

        if (results.isEmpty()) {
            binding.tvEmpty.show()
            binding.rvBooks.hide()
        } else {
            binding.tvEmpty.hide()
            binding.rvBooks.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

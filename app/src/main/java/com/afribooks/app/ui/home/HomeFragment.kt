package com.afribooks.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.afribooks.app.R
import com.afribooks.app.adapter.BookAdapter
import com.afribooks.app.data.repository.BookRepository
import com.afribooks.app.databinding.FragmentHomeBinding
import com.afribooks.app.ui.buy.BuyFragment
import com.afribooks.app.ui.detail.BookDetailActivity
import com.afribooks.app.ui.sell.SellFragment
import com.afribooks.app.utils.showSnackbar
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookAdapter: BookAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupClickListeners()
        loadRecentListings()
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
                loadRecentListings()
            }
        )

        binding.rvRecentListings.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = bookAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupClickListeners() {
        binding.btnBuyBooks.setOnClickListener {
            (activity as? MainActivity)?.let {
                it.binding.bottomNavigation.selectedItemId = R.id.nav_buy
            }
        }

        binding.btnSellBook.setOnClickListener {
            (activity as? MainActivity)?.let {
                it.binding.bottomNavigation.selectedItemId = R.id.nav_sell
            }
        }

        binding.searchContainer.setOnClickListener {
            (activity as? MainActivity)?.let {
                it.binding.bottomNavigation.selectedItemId = R.id.nav_buy
            }
        }

        binding.tvSeeAll.setOnClickListener {
            (activity as? MainActivity)?.let {
                it.binding.bottomNavigation.selectedItemId = R.id.nav_buy
            }
        }
    }

    private fun loadRecentListings() {
        val recentBooks = BookRepository.getRecentBooks(6)
        bookAdapter.submitList(recentBooks)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

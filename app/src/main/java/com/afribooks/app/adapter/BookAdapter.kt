package com.afribooks.app.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afribooks.app.data.model.Book
import com.afribooks.app.data.model.BookCondition
import com.afribooks.app.databinding.ItemBookCardBinding
import com.afribooks.app.utils.ValidationUtils

class BookAdapter(
    private val onBookClick: (Book) -> Unit,
    private val onSaveClick: (Book) -> Unit
) : ListAdapter<Book, BookAdapter.BookViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BookViewHolder(
        private val binding: ItemBookCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book) {
            binding.tvBookTitle.text = book.title
            binding.tvCourseName.text = "Course: ${book.courseCode}"
            binding.tvPrice.text = ValidationUtils.formatPrice(book.price)
            binding.tvConditionBadge.text = book.condition.displayName

            // Condition badge colour
            val badgeColor = when (book.condition) {
                BookCondition.LIKE_NEW -> Color.parseColor("#1B5E20")
                BookCondition.GOOD     -> Color.parseColor("#2E7D32")
                BookCondition.FAIR     -> Color.parseColor("#F57F17")
                BookCondition.POOR     -> Color.parseColor("#B71C1C")
            }
            binding.tvConditionBadge.backgroundTintList =
                android.content.res.ColorStateList.valueOf(badgeColor)

            // Book cover background tint using course colour
            try {
                binding.ivBookCover.setBackgroundColor(Color.parseColor(book.coverColorHex))
            } catch (e: IllegalArgumentException) {
                binding.ivBookCover.setBackgroundColor(Color.parseColor("#C0572B"))
            }

            // Save icon tint
            val heartColor = if (book.isSaved) "#C0572B" else "#E0E0E0"
            binding.ivSaveBook.setColorFilter(Color.parseColor(heartColor))

            binding.root.setOnClickListener { onBookClick(book) }
            binding.ivSaveBook.setOnClickListener { onSaveClick(book) }
        }
    }

    class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Book, newItem: Book) = oldItem == newItem
    }
}

package com.afribooks.app.ui.sell

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.afribooks.app.R
import com.afribooks.app.data.model.BookCondition
import com.afribooks.app.databinding.FragmentSellBinding
import com.afribooks.app.utils.ValidationUtils
import com.afribooks.app.utils.hideKeyboard
import com.afribooks.app.utils.hide
import com.afribooks.app.utils.show
import com.google.android.material.snackbar.Snackbar

class SellFragment : Fragment() {

    private var _binding: FragmentSellBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardImageUpload.setOnClickListener {
            Snackbar.make(
                binding.root,
                "Image picker would open here in a full implementation.",
                Snackbar.LENGTH_SHORT
            ).show()
        }

        binding.btnSubmitListing.setOnClickListener {
            activity?.hideKeyboard()
            if (validateForm()) submitListing()
        }

        // Clear field errors on focus change
        listOf(
            binding.etBookTitle   to binding.tilBookTitle,
            binding.etCourseName  to binding.tilCourseName,
            binding.etPrice       to binding.tilPrice,
            binding.etDescription to binding.tilDescription
        ).forEach { (et, til) ->
            et.setOnFocusChangeListener { _, _ -> til.error = null }
        }

        // Clear condition error as soon as any chip is checked
        binding.chipGroupCondition.setOnCheckedStateChangeListener { _, _ ->
            binding.tvConditionError.hide()
        }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val title  = binding.etBookTitle.text.toString().trim()
        val course = binding.etCourseName.text.toString().trim()
        val price  = binding.etPrice.text.toString().trim()

        if (!ValidationUtils.isNotBlank(title)) {
            binding.tilBookTitle.error = getString(R.string.field_required)
            valid = false
        } else binding.tilBookTitle.error = null

        if (!ValidationUtils.isNotBlank(course)) {
            binding.tilCourseName.error = getString(R.string.field_required)
            valid = false
        } else binding.tilCourseName.error = null

        if (!ValidationUtils.isValidPrice(price)) {
            binding.tilPrice.error = getString(R.string.price_invalid)
            valid = false
        } else binding.tilPrice.error = null

        // Show inline error TextView instead of Snackbar
        if (getSelectedCondition() == null) {
            binding.tvConditionError.show()
            valid = false
        } else {
            binding.tvConditionError.hide()
        }

        return valid
    }

    private fun getSelectedCondition(): BookCondition? = when {
        binding.chipLikeNew.isChecked -> BookCondition.LIKE_NEW
        binding.chipGood.isChecked    -> BookCondition.GOOD
        binding.chipFair.isChecked    -> BookCondition.FAIR
        binding.chipPoor.isChecked    -> BookCondition.POOR
        else                          -> null
    }

    private fun submitListing() {
        binding.etBookTitle.text?.clear()
        binding.etCourseName.text?.clear()
        binding.etPrice.text?.clear()
        binding.etDescription.text?.clear()
        binding.chipGroupCondition.clearCheck()
        binding.tvConditionError.hide()

        Snackbar.make(
            binding.root,
            getString(R.string.listing_submitted),
            Snackbar.LENGTH_LONG
        ).setBackgroundTint(
            requireContext().getColor(R.color.afri_green)
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

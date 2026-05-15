package com.afribooks.app.utils

import android.util.Patterns

object ValidationUtils {

    fun isValidEmail(email: String): Boolean =
        email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun isValidPassword(password: String): Boolean =
        password.length >= 6

    fun isNotBlank(value: String): Boolean = value.isNotBlank()

    fun isValidPrice(price: String): Boolean {
        if (price.isBlank()) return false
        return try {
            val v = price.toDouble()
            v > 0
        } catch (e: NumberFormatException) {
            false
        }
    }

    fun passwordsMatch(p1: String, p2: String): Boolean = p1 == p2

    fun formatPrice(price: Double): String = "R %.2f".format(price)
}

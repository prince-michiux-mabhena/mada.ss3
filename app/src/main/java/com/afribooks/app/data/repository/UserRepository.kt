package com.afribooks.app.data.repository

import com.afribooks.app.data.model.User

object UserRepository {

    private val users = mutableListOf(
        User("Amara", "amara@student.ac.za", "password123"),
        User("Kofi", "kofi@student.ac.za", "password123")
    )

    private var loggedInUser: User? = null

    fun register(username: String, email: String, password: String): Boolean {
        if (users.any { it.email.equals(email, ignoreCase = true) }) return false
        users.add(User(username, email, password))
        return true
    }

    fun login(email: String, password: String): User? {
        val user = users.find {
            it.email.equals(email, ignoreCase = true) && it.password == password
        }
        loggedInUser = user
        return user
    }

    fun logout() { loggedInUser = null }

    fun getLoggedInUser(): User? = loggedInUser

    fun isLoggedIn(): Boolean = loggedInUser != null
}

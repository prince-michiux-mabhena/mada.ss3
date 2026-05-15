package com.afribooks.app.data.repository

import com.afribooks.app.data.model.Book
import com.afribooks.app.data.model.BookCondition

object BookRepository {

    private val allBooks = mutableListOf(
        Book(
            id = 1,
            title = "Introduction to Psychology",
            courseName = "Psychology",
            courseCode = "PSY 101",
            price = 45.00,
            condition = BookCondition.GOOD,
            seller = "Amara Diallo",
            sellerEmail = "amara@university.ac.za",
            description = "Comprehensive introduction to the field of psychology. Covers perception, cognition, emotion, personality and behaviour. Minimal highlighting, all pages intact.",
            coverColorHex = "#C0572B"
        ),
        Book(
            id = 2,
            title = "Calculus Volume 1",
            courseName = "Mathematics",
            courseCode = "MATH 201",
            price = 65.00,
            condition = BookCondition.LIKE_NEW,
            seller = "Kofi Mensah",
            sellerEmail = "kofi@university.ac.za",
            description = "Calculus textbook covering limits, derivatives and integrals. Used only for one semester. No writing or highlighting inside.",
            coverColorHex = "#2E7D32"
        ),
        Book(
            id = 3,
            title = "Biology: The Science of Life",
            courseName = "Biological Sciences",
            courseCode = "BIO 110",
            price = 80.00,
            condition = BookCondition.LIKE_NEW,
            seller = "Zuri Okonkwo",
            sellerEmail = "zuri@university.ac.za",
            description = "Full colour biology textbook. Covers cell biology, genetics, evolution and ecology. Near perfect condition - only read once.",
            coverColorHex = "#1565C0"
        ),
        Book(
            id = 4,
            title = "Principles of Economics",
            courseName = "Economics",
            courseCode = "ECO 101",
            price = 55.00,
            condition = BookCondition.GOOD,
            seller = "Taraji Osei",
            sellerEmail = "taraji@university.ac.za",
            description = "Core economics textbook covering micro and macroeconomic theory. Some pencil notes in margins, easily erased.",
            coverColorHex = "#4527A0"
        ),
        Book(
            id = 5,
            title = "Business Communication",
            courseName = "Business Studies",
            courseCode = "BUS 204",
            price = 35.00,
            condition = BookCondition.FAIR,
            seller = "Chike Adeyemi",
            sellerEmail = "chike@university.ac.za",
            description = "Practical business communication textbook. Covers report writing, presentations, email etiquette. Cover slightly worn.",
            coverColorHex = "#D4A017"
        ),
        Book(
            id = 6,
            title = "Introduction to Programming with Python",
            courseName = "Computer Science",
            courseCode = "CSC 101",
            price = 70.00,
            condition = BookCondition.GOOD,
            seller = "Nia Abara",
            sellerEmail = "nia@university.ac.za",
            description = "Python programming fundamentals. Covers data structures, algorithms and OOP. Great starter book for CS students.",
            coverColorHex = "#00695C"
        ),
        Book(
            id = 7,
            title = "African History: A Continent's Story",
            courseName = "History",
            courseCode = "HIS 301",
            price = 40.00,
            condition = BookCondition.GOOD,
            seller = "Seun Balogun",
            sellerEmail = "seun@university.ac.za",
            description = "Comprehensive survey of African history from ancient civilisations to the modern era. Well maintained.",
            coverColorHex = "#BF360C"
        ),
        Book(
            id = 8,
            title = "Organic Chemistry 1",
            courseName = "Chemistry",
            courseCode = "CHE 202",
            price = 90.00,
            condition = BookCondition.LIKE_NEW,
            seller = "Adaeze Nwosu",
            sellerEmail = "adaeze@university.ac.za",
            description = "Organic chemistry textbook with reaction mechanisms, stereochemistry and synthesis strategies. Barely used.",
            coverColorHex = "#880E4F"
        ),
        Book(
            id = 9,
            title = "Financial Accounting Essentials",
            courseName = "Accounting",
            courseCode = "ACC 101",
            price = 60.00,
            condition = BookCondition.FAIR,
            seller = "Emeka Obiora",
            sellerEmail = "emeka@university.ac.za",
            description = "Introductory accounting textbook. Covers double entry, trial balance, income statements and balance sheets.",
            coverColorHex = "#1A237E"
        ),
        Book(
            id = 10,
            title = "Statistics for Social Sciences",
            courseName = "Statistics",
            courseCode = "STA 201",
            price = 48.00,
            condition = BookCondition.GOOD,
            seller = "Fatima Diallo",
            sellerEmail = "fatima@university.ac.za",
            description = "Applied statistics with SPSS examples. Covers descriptive statistics, hypothesis testing and regression analysis.",
            coverColorHex = "#33691E"
        )
    )

    fun getAllBooks(): List<Book> = allBooks.toList()

    fun getRecentBooks(limit: Int = 5): List<Book> = allBooks.take(limit)

    fun searchBooks(query: String): List<Book> {
        if (query.isBlank()) return allBooks.toList()
        val lower = query.lowercase()
        return allBooks.filter {
            it.title.lowercase().contains(lower) ||
                    it.courseCode.lowercase().contains(lower) ||
                    it.courseName.lowercase().contains(lower)
        }
    }

    fun filterByCondition(condition: BookCondition): List<Book> =
        allBooks.filter { it.condition == condition }

    fun searchAndFilter(query: String, conditions: Set<BookCondition>): List<Book> {
        var result = if (query.isBlank()) allBooks.toList() else searchBooks(query)
        if (conditions.isNotEmpty()) {
            result = result.filter { it.condition in conditions }
        }
        return result
    }

    fun getBookById(id: Int): Book? = allBooks.find { it.id == id }

    fun toggleSaved(bookId: Int) {
        allBooks.find { it.id == bookId }?.let {
            it.isSaved = !it.isSaved
        }
    }

    fun getSavedBooks(): List<Book> = allBooks.filter { it.isSaved }
}

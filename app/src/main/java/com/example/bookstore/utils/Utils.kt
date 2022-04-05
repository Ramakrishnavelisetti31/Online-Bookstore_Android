package com.example.bookstore.utils
import java.util.regex.Matcher
import java.util.regex.Pattern

class Utils {
    private var regex = ""
    fun emailId(emailIds: String?): Boolean {
        regex = "^[a-zA-Z-9]+([._+-]*[0-9A-Za-z]+)*@[a-zA-Z0-9]+.[a-zA-Z]{2,4}([.][a-z]{2,4})?$"
        val pattern: Pattern = Pattern.compile(regex)
        val matcher: Matcher = pattern.matcher(emailIds)
        return matcher.matches()
    }

    fun passwordRule1(password: String?): Boolean {
        regex = "^[a-z]{8,}$"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }

    fun passwordRule2(password: String?): Boolean {
        regex = "^[a-z](?=.*[A-Z]+)(?=.*[0-9]+).{8,}$"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }
}
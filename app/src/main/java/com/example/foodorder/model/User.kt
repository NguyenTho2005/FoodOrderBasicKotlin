package com.example.foodorder.model

data class User(
    val id: String,
    var name: String,
    var email: String,
    var phone: String = "",
    var avatarUrl: String = ""
)
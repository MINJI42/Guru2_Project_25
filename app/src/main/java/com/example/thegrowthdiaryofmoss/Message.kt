package com.example.thegrowthdiaryofmoss

data class Message(
    val senderId: String,
    val senderNickname: String,
    val content: String,
    val timestamp: Long
)

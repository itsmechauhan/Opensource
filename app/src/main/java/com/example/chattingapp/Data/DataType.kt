package com.example.chattingapp.Data

data class UserData(
    var userId: String?="",
    var name: String?="",
    var number: String?="",
    var imageurl: String?="",

){
    fun toMap()=mapOf(
        "userId" to userId,
        "name" to name,
        "number" to number,
        "imageurl" to imageurl

    )
}
package com.example.paynearby.DATABASE

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Parcelize
//@Entity(tableName = "user_table")
//data class User(
//    @PrimaryKey(autoGenerate = true)
//    val id: Int,
//    val firstName: String,
//    val lastName: String,
//    val age: Int
//): Parcelable

@Entity(tableName = "user_table")
data class User(
        val topImage: String,
        val bottomImage: String,
        @PrimaryKey(autoGenerate = true) var id: Long? = null
)
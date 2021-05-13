package com.example.paynearby.DATABASE

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

//    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @Insert
    fun addUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("DELETE FROM user_table")
    fun deleteAllUsers()

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<User>>

}
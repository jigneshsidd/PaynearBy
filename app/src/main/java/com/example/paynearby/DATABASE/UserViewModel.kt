package com.example.paynearby.DATABASE

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.doAsync

class UserViewModel(application: Application): AndroidViewModel(application) {

    var readAllData: LiveData<List<User>>
    private val repository: UserRepository
    var applicationContext = application
    init {
        val userDao = UserDatabase.getDatabase(
            application
        ).userDao()
        repository = UserRepository(userDao)
        readAllData = repository.readAllData
    }

    fun addUser(user: User){
        runBlocking {
            async {
                doAsync {
                    repository.addUser(user)
                }
            }
        }
    }

    fun readAllUserData(): LiveData<List<User>>{
        readAllData = repository.readAllData
        return readAllData
    }

    fun updateUser(user: User){
//        runBlocking {
//            async {
//                doAsync {
//                    repository.updateUser(user)
//                }
//            }
//        }
    }

    fun deleteUser(user: User){
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.deleteUser(user)
//        }
    }

    fun deleteAllUsers(){
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.deleteAllUsers()
//        }
    }

}
package com.helin.noteapplicationkotlin

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginFragmentViewModel : ViewModel() {


    var username = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var loginUser= MutableLiveData<Person>()

    fun onClick(view: View?) {
        val person = Person(username.value,password.value)
        loginUser.setValue(person)
    }

    fun getUser(): MutableLiveData<Person> {

        return loginUser
    }

}
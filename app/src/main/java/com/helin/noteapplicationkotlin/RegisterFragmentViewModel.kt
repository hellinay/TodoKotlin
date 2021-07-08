package com.helin.noteapplicationkotlin

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel



class RegisterFragmentViewModel:ViewModel() {

    var username = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var name = MutableLiveData<String>()
    var surname = MutableLiveData<String>()
    var personData= MutableLiveData<Person>()


    fun onClick(view: View?) {
        val person = Person(username.value, username.value,password.value,surname.value)
        personData.setValue(person)
    }

    fun getUser(): MutableLiveData<Person> {

        return personData
    }

}
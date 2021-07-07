package com.helin.noteapplicationkotlin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotesViewModel: ViewModel() {

    val notes = MutableLiveData<List<Notes>>()




}
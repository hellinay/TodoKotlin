package com.helin.noteapplicationkotlin

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NotesViewModel : ViewModel() {

    var fragment= Fragment()
    val notes = MutableLiveData<ArrayList<Notes>>()


    fun addNote(person: Person, context: Context) {
        val db = DBHelper(context)
        db.writableDatabase
        val rg: RadioGroup
        val okayBtn: Button
        lateinit var radioButton: RadioButton
        val dialogadd = Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen)
        dialogadd.setContentView(R.layout.dialog_box_add)
        rg = dialogadd.findViewById(R.id.RadioGroupNoteAdd)
        okayBtn = dialogadd.findViewById(R.id.buttonOkay)
        layout(context)
        dialogadd.show()
        okayBtn.setOnClickListener {
            val itemAdd = NotesDao(context)
            val note = dialogadd.findViewById<EditText>(R.id.editTextEnterNote)
            val type = rg.checkedRadioButtonId
            radioButton = dialogadd.findViewById(type)
            if (radioButton.getText().toString().contains("ToDo")) {
                itemAdd.addNote(db, NoteType.TODO, note.text.toString(), person!!.id)
            } else if (radioButton.getText().toString().contains("Done")) {
                itemAdd.addNote(db, NoteType.DONE, note.text.toString(), person!!.id)
            } else if (radioButton.getText().toString().contains("In Progress")) {
                itemAdd.addNote(db, NoteType.INPROGRESS, note.text.toString(), person!!.id)
            }
            dialogadd.dismiss()
            getAllNotes(context, person)

        }
    }

    fun getAllNotes(context: Context, person: Person) {

        val notesDao = NotesDao(context)
        Log.e("getAllNotes", "getAllNotesSize: "+notesDao.getAllNotes(person.id).size )
        notes.value=(notesDao.getAllNotes(person.id))

    }

    fun deleteNote(context: Context, note: Notes, person: Person, position: Int) {
        val itemDelete = NotesDao(context)
        val db = DBHelper(context)
        db.writableDatabase
        MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setMessage("Do you want to delete " + note.note + " ?")
                .setNegativeButton(R.string.noBtn) { dialog, which -> dialog.dismiss() }
                .setPositiveButton(R.string.okayBtn) { dialog, id ->
                    itemDelete.deleteNote(db, note.noteId, person.id)

                    getAllNotes(context, person)
                }
                .show()


    }

    fun editNote(note:Notes, context:Context, person: Person, radioGroup: RadioGroup) {
        val saveBtn: Button
        val uploadBtn: Button
        val editedNote: TextView
        var rbtn:RadioButton

        uploadBtn = Adapter.myDialog!!.findViewById(R.id.buttonUpload)
        saveBtn = Adapter.myDialog!!.findViewById(R.id.buttonSave)
        editedNote = Adapter.myDialog!!.findViewById(R.id.editTextNoteEdited)
        editedNote.text = note!!.note
        layout(context)
        Adapter.myDialog!!.show()
        uploadBtn.setOnClickListener { checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, NotesFragment.Companion.READ_EXTERNAL_STORAGE,context) }
        saveBtn.setOnClickListener {
            val itemUpdate = NotesDao(context)
            val type = radioGroup!!.checkedRadioButtonId
            rbtn = Adapter.myDialog!!.findViewById<View>(type) as RadioButton
            if (rbtn!!.text.toString().contains("ToDo")) {
                note!!.type == (NoteType.TODO)
                itemUpdate.updateNote(note.noteId, NoteType.TODO, person.id, editedNote.text.toString(), NotesFragment.Companion.selectedImagePath)
                note.note = (editedNote.text.toString())
            } else if (rbtn!!.text.toString().contains("Done")) {
                note!!.type = (NoteType.DONE)
                itemUpdate.updateNote(note.noteId, NoteType.DONE, person.id, editedNote.text.toString(), NotesFragment.Companion.selectedImagePath)
                note!!.note = (editedNote.text.toString())
            } else if (rbtn!!.text.toString().contains("In Progress")) {
                note!!.type = NoteType.INPROGRESS
                itemUpdate.updateNote(note!!.noteId, NoteType.INPROGRESS, person.id, editedNote.text.toString(), NotesFragment.Companion.selectedImagePath)
                note!!.note = (editedNote.text.toString())
            } else Adapter.myDialog!!.dismiss()
            Adapter.myDialog!!.dismiss()
            getAllNotes(context, person)
        }


    }
    fun checkPermission(permission: String, requestCode: Int,context: Context) {
        if (ContextCompat.checkSelfPermission(context!!, permission) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(context, "permission", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(fragment.requireActivity(), arrayOf(permission), requestCode)
        } else {
            Toast.makeText(context, "Permission already granted", Toast.LENGTH_SHORT).show()
            (fragment as NotesFragment).selectImage()
        }
    }
    fun layout(context: Context){
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
    }

}



package com.helin.noteapplicationkotlin

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.helin.noteapplicationkotlin.Adapter.CardViewNotesHolder
import java.util.*

class Adapter(fragment: Fragment, var myContext: Context, notes: ArrayList<Notes>, person: Person) : RecyclerView.Adapter<CardViewNotesHolder>() {
    private val inflater: LayoutInflater
    var notes: ArrayList<Notes>
    var db: DBHelper? = null //?= could be null
    var rbtn: RadioButton? = null
    var person: Person
    var rv: RecyclerView? = null
    var type = 0
    lateinit var rgedit: RadioGroup
    var fragment: Fragment
    var viewModel=NotesViewModel()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewNotesHolder {

        val view = inflater.inflate(R.layout.cardview_notes, parent, false)
        rv = view.findViewById(R.id.RecyclerViewNotes)
        val cardViewNotesHolder = CardViewNotesHolder(view)
        init()
        /*val model: NotesViewModel by viewModels()
        model.notes.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Log.d("NotesFragment", "onCreateView: "+ it)
        })*/
        return cardViewNotesHolder
    }

    override fun onBindViewHolder(holder: CardViewNotesHolder, position: Int) {
        holder.setdata(notes!![position], position)
    }

    override fun getItemCount(): Int {
        return notes!!.size
    }

    inner class CardViewNotesHolder(v: View) : RecyclerView.ViewHolder(v) {
        var deleteBtn: Button
        var editBtn: Button
        var radioGroup: RadioGroup
        var noteTw: TextView
        var radioButtonToDo: RadioButton
        var radioButtonInProg: RadioButton
        var radioButtonDone: RadioButton
        var noteHeader: TextView
        fun allUnclickable() {
            radioButtonDone.isClickable = false
            radioButtonToDo.isClickable = false
            radioButtonInProg.isClickable = false
        }

        fun setdata(note: Notes?, position: Int) {
            noteTw.text = note!!.note
            if (note.type == 1) {
                radioButtonDone.isChecked = true
                noteHeader.text = "Done"
                allUnclickable()
                val done = myDialog!!.findViewById<RadioButton>(R.id.radioDone)
                done.isChecked = true
            } else if (note.type == 2) {
                radioButtonToDo.isChecked = true
                noteHeader.text = "To Do"
                allUnclickable()
                val todo = myDialog!!.findViewById<RadioButton>(R.id.radioToDo)
                todo.isChecked = true
            } else if (note.type == 3) {
                radioButtonInProg.isChecked = true
                noteHeader.text = "In Progress"
                allUnclickable()
                val prog = myDialog!!.findViewById<RadioButton>(R.id.radioInProgress)
                prog.isChecked = true
            }
            editBtn.setOnClickListener { dialogEdit(note) }

            deleteBtn.setOnClickListener {
                viewModel.deleteNote(myContext,note,person,position)

            }
        }


        fun dialogEdit(note: Notes) {
            viewModel.editNote(note,myContext,person,radioGroup)
            /*val saveBtn: Button
            val uploadBtn: Button
            val editedNote: TextView
            uploadBtn = myDialog!!.findViewById(R.id.buttonUpload)
            saveBtn = myDialog!!.findViewById(R.id.buttonSave)
            editedNote = myDialog!!.findViewById(R.id.editTextNoteEdited)
            editedNote.text = note!!.note
            val linearLayoutManager = LinearLayoutManager(myContext)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            myDialog!!.show()
            uploadBtn.setOnClickListener { checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, NotesFragment.Companion.READ_EXTERNAL_STORAGE) }
            saveBtn.setOnClickListener {
                val itemUpdate = NotesDao(myContext)
                val type = rgedit!!.checkedRadioButtonId
                rbtn = myDialog!!.findViewById<View>(type) as RadioButton
                if (rbtn!!.text.toString().contains("ToDo")) {
                    note!!.type==(NoteType.TODO)
                    itemUpdate.updateNote(note.noteId, NoteType.TODO, person.id, editedNote.text.toString(), NotesFragment.Companion.selectedImagePath)
                    note.note=(editedNote.text.toString())
                } else if (rbtn!!.text.toString().contains("Done")) {
                    note!!.type=(NoteType.DONE)
                    itemUpdate.updateNote(note.noteId, NoteType.DONE, person.id, editedNote.text.toString(), NotesFragment.Companion.selectedImagePath)
                    note!!.note=(editedNote.text.toString())
                } else if (rbtn!!.text.toString().contains("In Progress")) {
                    note!!.type=NoteType.INPROGRESS
                    itemUpdate.updateNote(note!!.noteId, NoteType.INPROGRESS, person.id, editedNote.text.toString(), NotesFragment.Companion.selectedImagePath)
                    note!!.note=(editedNote.text.toString())
                } else myDialog!!.dismiss()
                myDialog!!.dismiss()

            }*/
        }

        init {
            radioGroup = v.findViewById(R.id.RadioGroupNoteAdd)
            noteHeader = v.findViewById(R.id.textViewNoteHeader)
            noteTw = v.findViewById(R.id.textViewNoteName)
            deleteBtn = v.findViewById(R.id.buttonDel)
            editBtn = v.findViewById(R.id.buttonEdit)
            radioButtonToDo = v.findViewById(R.id.radioToDo)
            radioButtonInProg = v.findViewById(R.id.radioInProgress)
            radioButtonDone = v.findViewById(R.id.radioDone)
        }
    }

    fun init() {
        rgedit = myDialog.findViewById(R.id.RadioGroupEdit)
        type = rgedit.checkedRadioButtonId

    }

    companion object {
        lateinit var myDialog: Dialog
    }

    init {
        inflater = LayoutInflater.from(myContext)
        this.notes = notes
        this.person = person
        this.fragment = fragment
    }
}
package com.helin.noteapplicationkotlin

import android.content.ContentValues
import android.content.Context
import android.util.Log

class NotesDao(var myContext: Context?) {
    val ToDo = 1
    val InProg = 2
    val Done = 3
    var db: DBHelper? = null
    fun addNote(db: DBHelper?, type: Int, note: String?, personId: Int) {
        var db = db
        db = DBHelper(myContext!!.applicationContext)
        val dba = db.writableDatabase
        val values = ContentValues()
        values.put(DBHelper.Companion.KEY_NOTE_TYPE, type)
        values.put(DBHelper.Companion.KEY_NOTE_NOTE, note)
        values.put(DBHelper.Companion.KEY_NOTE_PERSONID, personId)
        dba.insertOrThrow("notes", null, values)
        dba.close()
    }

    fun deleteNote(db: DBHelper?, noteId: Int, person_id: Int) {
        var db = db
        db = DBHelper(myContext!!.applicationContext)
        val dba = db.writableDatabase
        dba.delete("notes", "note_id=? and person_id_notes=?", arrayOf(noteId.toString(), person_id.toString()))
        dba.close()
    }

    fun updateNote(noteId: Int, type: Int, person_id: Int, note: String, imgPath: String?) {
        db = DBHelper(myContext!!.applicationContext)
        val dba = db!!.writableDatabase
        val values = ContentValues()
        values.put(DBHelper.Companion.KEY_NOTE_TYPE, type)
        values.put(DBHelper.Companion.KEY_NOTE_NOTE, note)
        values.put(DBHelper.Companion.KEY_NOTE_IMG, imgPath)
        Log.e("type", "" + type)
        Log.e("note", "" + note)
        dba.update("notes", values, "note_id=? and person_id_notes=?", arrayOf(noteId.toString(), person_id.toString()))
        dba.close()
    }
}
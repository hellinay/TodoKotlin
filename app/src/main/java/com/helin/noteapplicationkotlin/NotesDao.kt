package com.helin.noteapplicationkotlin

import android.content.ContentValues
import android.content.Context
import android.util.Log
import java.util.ArrayList

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
    fun getAllNotes(personId: Int): ArrayList<Notes> {
        val query = "SELECT  * FROM " + DBHelper.TABLE_NOTES + " WHERE " + DBHelper.KEY_NOTE_PERSONID + "=? "
        val `val` = ArrayList<Notes>()
        db = DBHelper(myContext!!.applicationContext)

        val cursor = db!!.readableDatabase.rawQuery(query, arrayOf(personId.toString()))
        while (cursor.moveToNext()) {
            val note = Notes()
            note.note = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NOTE_NOTE))
            note.noteId = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_NOTE_ID))
            note.type = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_NOTE_TYPE))
            note.userId = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_NOTE_PERSONID))
            `val`.add(note)
        }
        cursor.close()
        return `val`
    }
}
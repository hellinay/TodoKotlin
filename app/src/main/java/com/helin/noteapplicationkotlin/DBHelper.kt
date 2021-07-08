package com.helin.noteapplicationkotlin

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

class DBHelper(private val context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    private val dbObj: SQLiteDatabase? = null
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES +
                "(" +
                KEY_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +  // Define a primary key
                KEY_NOTE_TYPE + " INTEGER," +
                KEY_NOTE_PERSONID + " INTEGER," +
                KEY_NOTE_NOTE + " TEXT," +
                KEY_NOTE_IMG + " TEXT" +
                ")"
        val CREATE_PERSON_TABLE = "CREATE TABLE " + TABLE_PERSON +
                "(" +
                KEY_PERSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_PERSON_PASSWORD + " TEXT NOT NULL," +
                KEY_PERSON_USERNAME + " TEXT," +
                KEY_PERSON_NAME + " TEXT," +
                KEY_PERSON_SURNAME + " TEXT" +
                ")"
        db.execSQL(CREATE_NOTES_TABLE)
        db.execSQL(CREATE_PERSON_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES)
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON)
            onCreate(db)
        }
    }

    fun hasObject(personId: String): Boolean {
        val db = this.writableDatabase
        val selectString = "SELECT * FROM " + TABLE_PERSON + " WHERE " + KEY_PERSON_USERNAME + "= ?"
        val cursor = db.rawQuery(selectString, arrayOf(personId))
        val exist: Boolean
        exist = if (cursor.count > 0) {
            true
        } else {
            false
        }
        db.close()
        cursor.close()
        return exist
    }

    fun getAllNotes(personId: Int): ArrayList<Notes> {
        val query = "SELECT  * FROM " + TABLE_NOTES + " WHERE " + KEY_NOTE_PERSONID + "=? "
        val `val` = ArrayList<Notes>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, arrayOf(personId.toString()))
        while (cursor.moveToNext()) {
            val note = Notes()
            note.note = cursor.getString(cursor.getColumnIndex(KEY_NOTE_NOTE))
            note.noteId = cursor.getInt(cursor.getColumnIndex(KEY_NOTE_ID))
            note.type = cursor.getInt(cursor.getColumnIndex(KEY_NOTE_TYPE))
            note.userId = cursor.getInt(cursor.getColumnIndex(KEY_NOTE_PERSONID))
            `val`.add(note)
        }
        cursor.close()
        return `val`
    }

    fun login(username: String, password: String): Person? {
        val query = "SELECT  * FROM " + TABLE_PERSON + " WHERE " + KEY_PERSON_USERNAME + "=? AND " + KEY_PERSON_PASSWORD + "=?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, arrayOf(username, password))
        while (cursor.moveToNext()) {
            val person = Person(KEY_PERSON_NAME, KEY_PERSON_USERNAME, KEY_PERSON_PASSWORD, KEY_PERSON_SURNAME)
            person.id = cursor.getInt(cursor.getColumnIndex(KEY_PERSON_ID))
            person.email = cursor.getString(cursor.getColumnIndex(KEY_PERSON_USERNAME))
            person.password = cursor.getString(cursor.getColumnIndex(KEY_PERSON_PASSWORD))
            person.name = cursor.getString(cursor.getColumnIndex(KEY_PERSON_NAME))
            person.surname = cursor.getString(cursor.getColumnIndex(KEY_PERSON_SURNAME))
            return person
        }
        cursor.close()
        return null
    }

    companion object {
        private const val DB_NAME = "notes"
        private const val DB_VERSION = 10
        const val TABLE_NOTES = "Notes"
        const val TABLE_PERSON = "Person"
        const val KEY_NOTE_ID = "note_id"
        const val KEY_NOTE_TYPE = "type"
        const val KEY_NOTE_NOTE = "note"
        const val KEY_NOTE_PERSONID = "person_id_notes"
        const val KEY_PERSON_ID = "person_id"
        const val KEY_PERSON_PASSWORD = "person_password"
        const val KEY_PERSON_USERNAME = "person_username"
        const val KEY_PERSON_NAME = "name"
        const val KEY_PERSON_SURNAME = "surname"
        const val KEY_NOTE_IMG = "image"
    }
}
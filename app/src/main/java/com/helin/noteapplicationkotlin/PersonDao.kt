package com.helin.noteapplicationkotlin

import android.content.ContentValues
import android.content.Context

class PersonDao(var mycontext: Context?) {
    var db: DBHelper? = null
    fun register(db: DBHelper?, username: String?, password: String?, name: String?, surname: String?) {
        val dba = db!!.writableDatabase
        val values = ContentValues()
        values.put(DBHelper.Companion.KEY_PERSON_USERNAME, username)
        values.put(DBHelper.Companion.KEY_PERSON_PASSWORD, password)
        values.put(DBHelper.Companion.KEY_PERSON_NAME, name)
        values.put(DBHelper.Companion.KEY_PERSON_SURNAME, surname)
        dba.insertOrThrow("person", null, values)
        dba.close()
    }

    fun checkUser(username: String, password: String): Boolean {
        db = DBHelper(mycontext!!.applicationContext)
        val columns = arrayOf<String>(
                DBHelper.Companion.KEY_PERSON_USERNAME,
                DBHelper.Companion.KEY_PERSON_PASSWORD
        )
        val dba = db!!.readableDatabase
        val selection: String = DBHelper.Companion.KEY_PERSON_USERNAME + " = ?" + " AND " + DBHelper.Companion.KEY_PERSON_PASSWORD + " = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = dba.query(DBHelper.Companion.TABLE_PERSON,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null)
        val cursorCount = cursor.count
        cursor.close()
        db!!.close()
        return if (cursorCount > 0) {
            true
        } else false
    }
}
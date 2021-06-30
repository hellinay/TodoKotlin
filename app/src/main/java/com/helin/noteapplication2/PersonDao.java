package com.helin.noteapplication2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PersonDao {
    DBHelper db;
    Context mycontext;

    public PersonDao(Context mycontext) {
        this.mycontext = mycontext;
    }

    public void register(DBHelper db, String username, String password, String name, String surname){
        SQLiteDatabase dba= db.getWritableDatabase();
        ContentValues values= new ContentValues();

        values.put(db.KEY_PERSON_USERNAME,username);
        values.put(db.KEY_PERSON_PASSWORD,password);
        values.put(db.KEY_PERSON_NAME,name);
        values.put(db.KEY_PERSON_SURNAME,surname);

        dba.insertOrThrow("person",null,values);
        dba.close();
    }
    public boolean checkUser(String username, String password) {
        db=new DBHelper(mycontext.getApplicationContext());
        // array of columns to fetch
        String[] columns = {
                db.KEY_PERSON_USERNAME
        };
        SQLiteDatabase dba = db.getReadableDatabase();
        // selection criteria
        String selection = db.KEY_PERSON_USERNAME + " = ?" + " AND " + db.KEY_PERSON_PASSWORD + " = ?";
        // selection arguments
        String[] selectionArgs = {username, password};
        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = dba.query(db.TABLE_PERSON, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
}











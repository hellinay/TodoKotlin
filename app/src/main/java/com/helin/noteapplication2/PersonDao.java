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
        String[] columns = {
                db.KEY_PERSON_USERNAME,
                db.KEY_PERSON_PASSWORD
        };
        SQLiteDatabase dba = db.getReadableDatabase();
        String selection = db.KEY_PERSON_USERNAME + " = ?" + " AND " + db.KEY_PERSON_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = dba.query(db.TABLE_PERSON,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
}











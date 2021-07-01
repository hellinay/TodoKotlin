package com.helin.noteapplication2;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "notes";
    private static final int DB_VERSION = 10;


    public static final String TABLE_NOTES = "Notes";
    public static final String TABLE_PERSON = "Person";

    public static final String KEY_NOTE_ID = "note_id";
    public static final String KEY_NOTE_TYPE = "type";
    public static final String KEY_NOTE_NOTE = "note";
    public static final String KEY_NOTE_PERSONID = "person_id_notes";

    public static final String KEY_PERSON_ID = "person_id";
    public static final String KEY_PERSON_PASSWORD = "person_password";
    public static final String KEY_PERSON_USERNAME = "person_username";
    public static final String KEY_PERSON_NAME = "name";
    public static final String KEY_PERSON_SURNAME = "surname";
    public static final String KEY_NOTE_IMG = "image";
    private final Context context;

    private SQLiteDatabase dbObj;

    public DBHelper(@Nullable Context context) {
        super(context,DB_NAME,null,DB_VERSION);
        this.context = context;

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES +
                "(" +
                KEY_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + // Define a primary key
                KEY_NOTE_TYPE + " INTEGER," +
                KEY_NOTE_PERSONID + " INTEGER," +
                KEY_NOTE_NOTE + " TEXT," +
                KEY_NOTE_IMG + " TEXT" +

                ")";

        String CREATE_PERSON_TABLE = "CREATE TABLE " + TABLE_PERSON +
                "(" +
                KEY_PERSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_PERSON_PASSWORD + " TEXT NOT NULL," +
                KEY_PERSON_USERNAME + " TEXT," +
                KEY_PERSON_NAME + " TEXT," +
                KEY_PERSON_SURNAME + " TEXT" +
                ")";

        db.execSQL(CREATE_NOTES_TABLE);
        db.execSQL(CREATE_PERSON_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
            onCreate(db);
        }
    }

    public boolean hasObject(String personId){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_PERSON + " WHERE " + KEY_PERSON_ID + "= ?";

        Cursor cursor = db.rawQuery(selectString,new String[]{personId});
        boolean exist;
        if(cursor.getCount()>0){
            exist=true;
        } else {
            exist=false;
        }
        db.close();
        cursor.close();

        return exist;
    }


    public ArrayList<Notes> getAllNotes(int personId){
        String query = "SELECT  * FROM " + TABLE_NOTES+ " WHERE " + KEY_NOTE_PERSONID + "=? ";
        ArrayList<Notes> val= new ArrayList<Notes>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(personId)});
        while (cursor.moveToNext()){
            Notes note= new Notes();
            note.setNote(cursor.getString(cursor.getColumnIndex(KEY_NOTE_NOTE)));
            note.setNoteId(cursor.getInt(cursor.getColumnIndex(KEY_NOTE_ID)));
            note.setType(cursor.getInt(cursor.getColumnIndex(KEY_NOTE_TYPE)));
            note.setUserId(cursor.getInt(cursor.getColumnIndex(KEY_NOTE_PERSONID)));
            val.add(note);
        }

        cursor.close();
        return val;

    }
    public Person login(String username, String password){
        String query = "SELECT  * FROM " + TABLE_PERSON + " WHERE " + KEY_PERSON_USERNAME + "=? AND "+ KEY_PERSON_PASSWORD +"=?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{username,password});
        while (cursor.moveToNext()){
            Person person=new Person();
            person.setId(cursor.getInt(cursor.getColumnIndex(KEY_PERSON_ID)));
            person.setEmail(cursor.getString(cursor.getColumnIndex(KEY_PERSON_USERNAME)));
            person.setPassword(cursor.getString(cursor.getColumnIndex(KEY_PERSON_PASSWORD)));
            person.setName(cursor.getString(cursor.getColumnIndex(KEY_PERSON_NAME)));
            person.setSurname(cursor.getString(cursor.getColumnIndex(KEY_PERSON_SURNAME)));

            return person;
        }
        cursor.close();
        return null;




    }








}

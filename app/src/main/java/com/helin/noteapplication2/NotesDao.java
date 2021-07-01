package com.helin.noteapplication2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class NotesDao {

    public final int ToDo=1;
    public final int InProg=2;
    public final int Done=3;
    DBHelper db;
    Context myContext;
    //CustomOpenHelper myOpenHelper = new CustomOpenHelper(this);


    public NotesDao(Context myContext) {
        this.myContext = myContext;
    }

    public void addNote(DBHelper db, int type, String note, int personId){

        db = new DBHelper(myContext.getApplicationContext());
        SQLiteDatabase dba= db.getWritableDatabase();
        ContentValues values= new ContentValues();

        values.put(db.KEY_NOTE_TYPE,type);
        values.put(db.KEY_NOTE_NOTE,note);
        values.put(db.KEY_NOTE_PERSONID,personId);





        dba.insertOrThrow("notes",null,values);
        dba.close();
    }


    /*public void uploadImage(String img){
        db = new DBHelper(myContext.getApplicationContext());
        SQLiteDatabase dba= db.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(db.KEY_NOTE_IMG,img);
        dba.insertOrThrow("notes",null,values);
        dba.close();

    }*/

    public void deleteNote(DBHelper db,int noteId,int person_id){
        db = new DBHelper(myContext.getApplicationContext());
        SQLiteDatabase dba= db.getWritableDatabase();

        dba.delete("notes","note_id=? and person_id_notes=?", new String[]{String.valueOf(noteId),String.valueOf(person_id)});
        dba.close();


    }

    public void updateNote(int noteId, int type,int person_id ,String note,String imgPath){
        db = new DBHelper(myContext.getApplicationContext());
        SQLiteDatabase dba= db.getWritableDatabase();
        ContentValues values= new ContentValues();


        values.put(db.KEY_NOTE_TYPE,type);
        values.put(db.KEY_NOTE_NOTE,note);
        values.put(db.KEY_NOTE_IMG,imgPath);
        Log.e("type",""+type);
        Log.e("note",""+note);

        dba.update("notes",values,"note_id=? and person_id_notes=?", new String[]{String.valueOf(noteId),String.valueOf(person_id)});
        dba.close();
    }
}

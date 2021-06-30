package com.helin.noteapplication2;

public class Notes {

    private int noteId;
    private int userId;
    private int type;
    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getNoteId() {
        return noteId;
    }

    public int getUserId() {
        return userId;
    }

    public int getType() {
        return type;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setType(int type) {
        this.type = type;
    }
}

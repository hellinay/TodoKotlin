package com.helin.noteapplication2;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.helin.noteapplication2.databinding.FragmentNotesBinding;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.CardViewNotesHolder> {

    private LayoutInflater inflater;
    public Context myContext;
    public ArrayList<Notes> notes;
    DBHelper db;
    public static Dialog myDialog;
    RadioButton rbtn;
    Person person;
    RecyclerView rv;
    int type;
    RadioGroup rgedit;


    Fragment fragment;


    public Adapter(Fragment fragment, Context myContext, ArrayList<Notes> notes, Person person) {
        this.myContext = myContext;
        this.inflater = LayoutInflater.from(myContext);
        this.notes = notes;
        this.person = person;
        this.fragment = fragment;
    }

    @Override
    public CardViewNotesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.cardview_notes, parent, false);
        CardViewNotesHolder cardViewNotesHolder = new CardViewNotesHolder(view);
        //= new NotesFragment();
        rv = view.findViewById(R.id.RecyclerViewNotes);
        rgedit = myDialog.findViewById(R.id.RadioGroupEdit);
        type = rgedit.getCheckedRadioButtonId();

        rbtn = (RadioButton) myDialog.findViewById(type);
        return cardViewNotesHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.CardViewNotesHolder holder, int position) {
        holder.setdata(notes.get(position), position);

    }

    @Override
    public int getItemCount() {
        return notes.size();

    }


    public class CardViewNotesHolder extends RecyclerView.ViewHolder {

        public Button deleteBtn;
        public Button editBtn;
        public RadioGroup radioGroup;
        public TextView noteTw;
        public RadioButton radioButtonToDo;
        public RadioButton radioButtonInProg;
        public RadioButton radioButtonDone;
        public TextView noteHeader;


        public CardViewNotesHolder(@NonNull View v) {
            super(v);
            radioGroup = v.findViewById(R.id.RadioGroupNoteAdd);
            noteHeader = v.findViewById(R.id.textViewNoteHeader);
            noteTw = v.findViewById(R.id.textViewNoteName);
            deleteBtn = v.findViewById(R.id.buttonDel);
            editBtn = v.findViewById(R.id.buttonEdit);
            radioButtonToDo = v.findViewById(R.id.radioToDo);
            radioButtonInProg = v.findViewById(R.id.radioInProgress);
            radioButtonDone = v.findViewById(R.id.radioDone);


        }

        public void allUnclickable() {
            radioButtonDone.setClickable(false);
            radioButtonToDo.setClickable(false);
            radioButtonInProg.setClickable(false);
        }

        public void setdata(Notes note, int position) {

            noteTw.setText(note.getNote());


            if (note.getType() == 1) {
                radioButtonDone.setChecked(true);
                noteHeader.setText("Done");
                allUnclickable();
                RadioButton done = myDialog.findViewById(R.id.radioDone);
                done.setChecked(true);

            } else if (note.getType() == 2) {
                radioButtonToDo.setChecked(true);
                noteHeader.setText("To Do");
                allUnclickable();
                RadioButton todo = myDialog.findViewById(R.id.radioToDo);
                todo.setChecked(true);
            } else if (note.getType() == 3) {
                radioButtonInProg.setChecked(true);
                noteHeader.setText("In Progress");
                allUnclickable();
                RadioButton prog = myDialog.findViewById(R.id.radioInProgress);
                prog.setChecked(true);

            }

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogEdit(note);
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NotesDao itemDelete = new NotesDao(myContext);
                    itemDelete.deleteNote(db, note.getNoteId(), person.getId());
                    notes.remove(position);
                    notifyDataSetChanged();

                }
            });


        }








        public void checkPermission(String permission, int requestCode) {
            if (ContextCompat.checkSelfPermission(myContext, permission) == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(myContext, "permission", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(fragment.requireActivity(), new String[]{permission}, requestCode);


            } else {
                Toast.makeText(myContext, "Permission already granted", Toast.LENGTH_SHORT).show();
                ((NotesFragment)fragment).selectImage();
            }
        }



        public void dialogEdit(Notes note) {
            Button saveBtn;
            Button uploadBtn;
            TextView editedNote;

            uploadBtn = myDialog.findViewById(R.id.buttonUpload);
            saveBtn = myDialog.findViewById(R.id.buttonSave);
            editedNote = myDialog.findViewById(R.id.editTextNoteEdited);
            editedNote.setText(note.getNote());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(myContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            myDialog.show();

            uploadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE,NotesFragment.READ_EXTERNAL_STORAGE);

                }
            });

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NotesDao itemUpdate = new NotesDao(myContext);
                    int type = rgedit.getCheckedRadioButtonId();
                    rbtn = (RadioButton) myDialog.findViewById(type);

                    if (rbtn.getText().toString().contains("ToDo")) {
                        note.setType(NoteType.TODO);
                        itemUpdate.updateNote(note.getNoteId(), NoteType.TODO, person.getId(), editedNote.getText().toString(),NotesFragment.selectedImagePath);
                        note.setNote(editedNote.getText().toString());
                    } else if (rbtn.getText().toString().contains("Done")) {
                        note.setType(NoteType.DONE);
                        itemUpdate.updateNote(note.getNoteId(), NoteType.DONE, person.getId(), editedNote.getText().toString(),NotesFragment.selectedImagePath);
                        note.setNote(editedNote.getText().toString());
                    } else if (rbtn.getText().toString().contains("In Progress")) {
                        note.setType(NoteType.INPROGRESS);
                        itemUpdate.updateNote(note.getNoteId(), NoteType.INPROGRESS, person.getId(), editedNote.getText().toString(),NotesFragment.selectedImagePath);
                        note.setNote(editedNote.getText().toString());
                    } else
                        myDialog.dismiss();

                    myDialog.dismiss();
                    notifyDataSetChanged();
                }
            });

        }

    }
}




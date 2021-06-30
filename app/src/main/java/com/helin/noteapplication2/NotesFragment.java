package com.helin.noteapplication2;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesFragment extends Fragment {
    RecyclerView recyclerView;
    private Adapter adapter;
    private Button addNote;
    DBHelper db;
    Dialog dialog;
    private View rootView;
    RadioButton radioButton;
    Person person;
    private ArrayList<Notes> notes = new ArrayList<>();
    private TextView info;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notes, container, false);
        db = new DBHelper(getContext());
        db.getWritableDatabase();
        info=rootView.findViewById(R.id.textViewInfo);
        addNote = rootView.findViewById(R.id.buttonAddNote);
        recyclerView = rootView.findViewById(R.id.RecyclerViewNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        person = NotesFragmentArgs.fromBundle(getArguments()).getPerson();
        notes = db.getAllNotes(person.getId());


        adapter = new Adapter(getContext(), notes, person);
        recyclerView.setAdapter(adapter);
        Log.e("personget", person.toString());
        info.setText("Welcome "+ person.getName()+" "+ person.getSurname());
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAdd();
            }
        });


        return rootView;
    }


    public void dialogAdd() {
        RadioGroup rg;
        Button okayBtn;

        dialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_box_add);
        rg = dialog.findViewById(R.id.RadioGroupNoteAdd);
        okayBtn = dialog.findViewById(R.id.buttonOkay);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dialog.show();
        okayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotesDao itemAdd = new NotesDao(getContext());
                EditText note = dialog.findViewById(R.id.editTextEnterNote);
                int type = rg.getCheckedRadioButtonId();
                radioButton = (RadioButton) dialog.findViewById(type);
                if (radioButton.getText().toString().contains("ToDo")) {
                    itemAdd.addNote(db, NoteType.TODO, note.getText().toString(), person.getId());
                } else if (radioButton.getText().toString().contains("Done")) {
                    itemAdd.addNote(db, NoteType.DONE, note.getText().toString(), person.getId());
                } else if (radioButton.getText().toString().contains("In Progress")) {
                    itemAdd.addNote(db, NoteType.INPROGRESS, note.getText().toString(), person.getId());
                }

                Toast.makeText(getContext(), "Login successful, redirecting to Notes Page.", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                notes.clear();
                notes.addAll(db.getAllNotes(person.getId()));
                adapter.notifyDataSetChanged();

            }
        });

    }


}

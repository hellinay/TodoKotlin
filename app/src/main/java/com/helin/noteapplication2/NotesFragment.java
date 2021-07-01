package com.helin.noteapplication2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.helin.noteapplication2.databinding.FragmentNotesBinding;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static androidx.core.app.ActivityCompat.startActivityForResult;
import static com.helin.noteapplication2.Adapter.myDialog;

public class NotesFragment extends Fragment {
    RecyclerView recyclerView;
    private Adapter adapter;
    private Button addNote;
    DBHelper db;
    Dialog dialogadd;
    Dialog dialogedit;
    RadioButton radioButton;
    Person person;
    private ArrayList<Notes> notes = new ArrayList<>();
    private TextView info;
    public static final int PICK_IMAGE = 5;
    public static final int READ_EXTERNAL_STORAGE = 101;//private  binding;
    ImageView uploadImg;
    public static String selectedImagePath;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentNotesBinding binding = FragmentNotesBinding.inflate(inflater, container, false);
        db = new DBHelper(getContext());
        db.getWritableDatabase();
        info = binding.textViewInfo;
        addNote = binding.buttonAddNote;
        recyclerView = binding.RecyclerViewNotes;

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myDialog = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        myDialog.setContentView(R.layout.dialog_box_edit);
        uploadImg=myDialog.findViewById(R.id.imageViewUpload);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        person = NotesFragmentArgs.fromBundle(getArguments()).getPerson();
        notes = db.getAllNotes(person.getId());


        adapter = new Adapter(NotesFragment.this, getContext(), notes, person);
        recyclerView.setAdapter(adapter);
        Log.e("personget", person.toString());
        info.setText("Welcome " + person.getName() + " " + person.getSurname());
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAdd();
            }
        });


        return binding.getRoot();
    }


    public void dialogAdd() {
        RadioGroup rg;
        Button okayBtn;

        dialogadd = new Dialog(getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);

        dialogadd.setContentView(R.layout.dialog_box_add);
        rg = dialogadd.findViewById(R.id.RadioGroupNoteAdd);
        okayBtn = dialogadd.findViewById(R.id.buttonOkay);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dialogadd.show();
        okayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotesDao itemAdd = new NotesDao(getContext());
                EditText note = dialogadd.findViewById(R.id.editTextEnterNote);
                int type = rg.getCheckedRadioButtonId();
                radioButton = dialogadd.findViewById(type);
                if (radioButton.getText().toString().contains("ToDo")) {
                    itemAdd.addNote(db, NoteType.TODO, note.getText().toString(), person.getId());
                } else if (radioButton.getText().toString().contains("Done")) {
                    itemAdd.addNote(db, NoteType.DONE, note.getText().toString(), person.getId());
                } else if (radioButton.getText().toString().contains("In Progress")) {
                    itemAdd.addNote(db, NoteType.INPROGRESS, note.getText().toString(), person.getId());
                }

                dialogadd.dismiss();
                notes.clear();
                notes.addAll(db.getAllNotes(person.getId()));
                adapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Storage Permission Granted", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getContext(), "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            selectedImagePath = getRealPathFromURIForGallery(selectedImageUri);
            //Log.d("onActivityResult: "+selectedImagePath);
            Bitmap myBitmap = BitmapFactory.decodeFile(selectedImagePath);
            uploadImg.setImageBitmap(myBitmap);

           //// notes.set(selectedImagePath);
            //binding.textViewImagePath.setText(selectedImagePath);
        }
    }
    public  void selectImage() {

        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(intent, "select image"),
                PICK_IMAGE);
    }
    public String getRealPathFromURIForGallery(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireContext().getContentResolver().query(uri, projection, null,
                null, null);
        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        assert false;
        cursor.close();
        return uri.getPath();
    }




}

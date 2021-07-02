package com.helin.noteapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import static android.text.TextUtils.isEmpty;

public class RegisterFragment extends Fragment {

    private EditText nameEt;
    private EditText surnameEt;
    private EditText idEt;
    private EditText passwordEt;
    private Button registerBtn;
    private View rootView;
    private DBHelper db;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register, container, false);
        init();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(nameEt.getText().toString(), surnameEt.getText().toString(), passwordEt.getText().toString(), idEt.getText().toString());

            }
        });

        return rootView;
    }


    public void goToLogin(View v, String id) {
        NavDirections act = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment();
        Navigation.findNavController(rootView).navigate(act);
    }

    public void register(String name, String surname, String password, String username) {
        PersonDao reg = new PersonDao(getContext());
        int i=checkDataEntered();
        if(i==0){
            if (db.hasObject(username) == true) {
                Toast.makeText(getContext(), "Please set another username", Toast.LENGTH_SHORT).show();
            } else if(db.hasObject(username) == false ) {
                reg.register(db, username, password, name, surname);
                goToLogin(rootView, username);

            }
        }
        else
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();

    }

    public void init() {
        db = new DBHelper(getContext());
        registerBtn = rootView.findViewById(R.id.buttonRegisterFrag);
        nameEt = rootView.findViewById(R.id.editTextRegisterName);
        surnameEt = rootView.findViewById(R.id.editTextRegisterSurname);
        passwordEt = rootView.findViewById(R.id.editTextRegisterPassword);
        idEt = rootView.findViewById(R.id.editTextRegisterId);
    }

    public int checkDataEntered() {
        int i=0;
        if (isEmpty(nameEt.getText().toString()))
            i++;

        if (isEmpty(surnameEt.getText().toString()))
            i++;

        if (isEmpty(idEt.getText().toString()))
            i++;

        if (isEmpty(passwordEt.getText().toString()))
            i++;

        return i;
    }

}
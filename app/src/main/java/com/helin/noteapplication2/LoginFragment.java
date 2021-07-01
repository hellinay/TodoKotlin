package com.helin.noteapplication2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {
    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutPassword;
    private EditText passwordEt;
    private Button loginBtn;
    private Button registerBtn;
    private View rootView;
    private String loginId;
    private PersonDao personDao;
    private String password;
    Boolean EditTextEmptyHolder;
    String TempPassword = "NOT_FOUND";
    private static final String TAG = "LoginFragment";

    DBHelper db;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        init();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginId = textInputLayoutUsername.getEditText().getText().toString();
                password= textInputLayoutPassword.getEditText().getText().toString();
                personDao= new PersonDao(requireContext());
                Person person = db.login(loginId, password);
                if (personDao.checkUser(loginId,password)) {
                    NavDirections act = LoginFragmentDirections.actionLoginFragmentToNotesFragment(person);
                    Navigation.findNavController(rootView).navigate(act);
                    Log.d("person", person.toString());
                    Toast.makeText(getContext(), "Login successful, redirecting to Notes Page.", Toast.LENGTH_SHORT).show();
                }
                else if(loginId==null || password==null)
                    textInputLayoutUsername.setError("Please fill all fields");
                else if (!(personDao.checkUser(loginId,password)))
                    textInputLayoutUsername.setError("Wrong username or password");


            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();

            }
        });


        return rootView;
    }



    public void CheckFinalResult() {

        if (TempPassword.equalsIgnoreCase(password)) {

            Toast.makeText(getContext(), "Login Successfully", Toast.LENGTH_LONG).show();
            NavDirections act = LoginFragmentDirections.actionLoginFragmentToNotesFragment(null);
            Navigation.findNavController(rootView).navigate(act);


        } else {
            Toast.makeText(getContext(), "UserName or Password is Wrong, Please Try Again.", Toast.LENGTH_LONG).show();
        }
        TempPassword = "NOT_FOUND";

    }


    public void register() {
        NavDirections act = LoginFragmentDirections.actionLoginFragmentToRegisterFragment();
        Navigation.findNavController(rootView).navigate(act);
    }

    public void init() {

        db = new DBHelper(getContext());
        db.getWritableDatabase();
        loginBtn = (Button) rootView.findViewById(R.id.buttonLogin);
        textInputLayoutPassword = rootView.findViewById(R.id.textInputLayoutPassword);
        textInputLayoutUsername = rootView.findViewById(R.id.textInputLayoutUsername);
        registerBtn = (Button) rootView.findViewById(R.id.buttonRegister);


    }


}

package com.helin.noteapplicationkotlin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputLayout
import com.helin.noteapplicationkotlin.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var textInputLayoutUsername: TextInputLayout? = null
    private var textInputLayoutPassword: TextInputLayout? = null
    private val passwordEt: EditText? = null
    private var loginBtn: Button? = null
    private var registerBtn: Button? = null
    private var rootView: View? = null
    private var loginId: String? = null
    private var personDao: PersonDao? = null
    private var password: String? = null
    var TempPassword = "NOT_FOUND"
    var db: DBHelper? = null
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //rootView = inflater.inflate(R.layout.fragment_login, container, false)
        binding=FragmentLoginBinding.inflate(layoutInflater)
        init()
        loginBtn!!.setOnClickListener {
            loginId = textInputLayoutUsername!!.editText!!.text.toString()
            password = textInputLayoutPassword!!.editText!!.text.toString()
            personDao = PersonDao(requireContext())
            val person = db!!.login(loginId!!, password!!)
            if (personDao!!.checkUser(loginId!!, password!!)) {
                val act: NavDirections = LoginFragmentDirections.actionLoginFragmentToNotesFragment(person)
                Navigation.findNavController(binding.root).navigate(act)
                Log.d("person", person.toString())
                Toast.makeText(context, "Login successful, redirecting to Notes Page.", Toast.LENGTH_SHORT).show()
            } else if (!personDao!!.checkUser(loginId!!, password!!) && (loginId !== "" || password !== "")) textInputLayoutUsername!!.error = "Wrong username or password" else if (loginId == null || password == null) textInputLayoutUsername!!.error = "Fill the fields"
        }
        registerBtn!!.setOnClickListener { register() }
        return binding.root
    }

    fun CheckFinalResult() {
        if (TempPassword.equals(password, ignoreCase = true)) {
            Toast.makeText(context, "Login Successfully", Toast.LENGTH_LONG).show()
            val act: NavDirections = LoginFragmentDirections.actionLoginFragmentToNotesFragment(null)
            Navigation.findNavController(binding.root).navigate(act)
        } else {
            Toast.makeText(context, "UserName or Password is Wrong, Please Try Again.", Toast.LENGTH_LONG).show()
        }
        TempPassword = "NOT_FOUND"
    }

    fun register() {
        val act = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        Navigation.findNavController(binding.root).navigate(act)
    }

    fun init() {
        db = DBHelper(context)
        db!!.writableDatabase
        loginBtn = binding.buttonLogin
        textInputLayoutPassword = binding.textInputLayoutPassword
        textInputLayoutUsername = binding.textInputLayoutUsername
        registerBtn = binding.buttonRegister
    }

    companion object {
        private const val TAG = "LoginFragment"
    }
}
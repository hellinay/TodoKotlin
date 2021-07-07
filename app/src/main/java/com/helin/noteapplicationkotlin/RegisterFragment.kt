package com.helin.noteapplicationkotlin

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

class RegisterFragment : Fragment() {
    private var nameEt: EditText? = null
    private var surnameEt: EditText? = null
    private var idEt: EditText? = null
    private var passwordEt: EditText? = null
    private var registerBtn: Button? = null
    private var rootView: View? = null
    private var db: DBHelper? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_register, container, false)
        init()
        registerBtn!!.setOnClickListener { register(nameEt!!.text.toString(), surnameEt!!.text.toString(), passwordEt!!.text.toString(), idEt!!.text.toString()) }
        return rootView
    }

    fun goToLogin(v: View?, id: String?) {
        val act = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        Navigation.findNavController(rootView!!).navigate(act)
    }

    fun register(name: String?, surname: String?, password: String?, username: String) {
        val reg = PersonDao(context)
        if (checkDataEntered()) {
            if (db!!.hasObject(username) == true) {
                Toast.makeText(context, "Please set another username", Toast.LENGTH_SHORT).show()
            } else if (db!!.hasObject(username) == false) {
                reg.register(db, username, password, name, surname)
                goToLogin(rootView, username)
            }
        } else Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
    }

    fun init() {
        db = DBHelper(context)
        registerBtn = rootView!!.findViewById(R.id.buttonRegisterFrag)
        nameEt = rootView!!.findViewById(R.id.editTextRegisterName)
        surnameEt = rootView!!.findViewById(R.id.editTextRegisterSurname)
        passwordEt = rootView!!.findViewById(R.id.editTextRegisterPassword)
        idEt = rootView!!.findViewById(R.id.editTextRegisterId)
    }

    fun checkDataEntered(): Boolean {
        return checkName() and checkPassword() and checkSurname() and checkUsername()
    }

    private fun checkName(): Boolean {
        val check = TextUtils.isEmpty(nameEt!!.text.toString())
        if (check) {
            nameEt!!.error = "fill name "
        }
        return !check
    }

    private fun checkSurname(): Boolean {
        val check = TextUtils.isEmpty(surnameEt!!.text.toString())
        if (check) {
            surnameEt!!.error = "fill surname "
        }
        return !check
    }

    private fun checkUsername(): Boolean {
        val check = TextUtils.isEmpty(idEt!!.text.toString())
        if (check) {
            idEt!!.error = "fill userename "
        }
        return !check
    }

    private fun checkPassword(): Boolean {
        val check = TextUtils.isEmpty(passwordEt!!.text.toString())
        if (check) {
            passwordEt!!.error = "fill passw "
        }
        return !check
    }
}
package com.helin.noteapplicationkotlin

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.helin.noteapplicationkotlin.databinding.FragmentNotesBinding
import java.io.FileNotFoundException
import java.util.*

class NotesFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    private var adapter: Adapter? = null
    private var addNote: Button? = null
    var db: DBHelper? = null
    var dialogadd: Dialog? = null
    lateinit var radioButton: RadioButton
    var person: Person? = null
    private var notes: ArrayList<Notes> = ArrayList()
    private var info: TextView? = null
    var uploadImg: ImageView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentNotesBinding.inflate(inflater, container, false)
        //val model: NotesViewModel by viewModels()
        db = DBHelper(context)
        db!!.writableDatabase
        info = binding.textViewInfo
        addNote = binding.buttonAddNote
        recyclerView = binding.RecyclerViewNotes
        init()
        Log.e("personget", person.toString())
        info!!.text = "Welcome " + person!!.name + " " + person!!.surname
        addNote!!.setOnClickListener { dialogAdd() }
        return binding.root
    }

    fun dialogAdd() {
        val rg: RadioGroup
        val okayBtn: Button
        dialogadd = Dialog(requireContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen)
        dialogadd!!.setContentView(R.layout.dialog_box_add)
        rg = dialogadd!!.findViewById(R.id.RadioGroupNoteAdd)
        okayBtn = dialogadd!!.findViewById(R.id.buttonOkay)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        dialogadd!!.show()
        okayBtn.setOnClickListener {
            val itemAdd = NotesDao(context)
            val note = dialogadd!!.findViewById<EditText>(R.id.editTextEnterNote)
            val type = rg.checkedRadioButtonId
            radioButton = dialogadd!!.findViewById(type)
            if (radioButton.getText().toString().contains("ToDo")) {
                itemAdd.addNote(db, NoteType.TODO, note.text.toString(), person!!.id)
            } else if (radioButton.getText().toString().contains("Done")) {
                itemAdd.addNote(db, NoteType.DONE, note.text.toString(), person!!.id)
            } else if (radioButton.getText().toString().contains("In Progress")) {
                itemAdd.addNote(db, NoteType.INPROGRESS, note.text.toString(), person!!.id)
            }
            dialogadd!!.dismiss()
            notes!!.clear()
            notes!!.addAll(db!!.getAllNotes(person!!.id))
            adapter!!.notifyDataSetChanged()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults)
        if (requestCode == READ_EXTERNAL_STORAGE) {
            if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Storage Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Storage Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data!!.data
            selectedImagePath = getRealPathFromURIForGallery(selectedImageUri)
            try {
                val bitmap = BitmapFactory.decodeStream(requireActivity().contentResolver.openInputStream(selectedImageUri!!))
                uploadImg!!.setImageBitmap(bitmap)
            } catch (e: FileNotFoundException) {
                Log.e(ContentValues.TAG, "onActivityResult: " + e.localizedMessage)
                e.printStackTrace()
            }
        }
    }

    fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(intent, "select image"),
                PICK_IMAGE)
    }

    fun getRealPathFromURIForGallery(uri: Uri?): String? {
        if (uri == null) {
            return null
        }
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(uri, projection, null,
                null, null)
        if (cursor != null) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        }
        assert(false)
        if (cursor != null) {
            cursor.close()
        }
        return uri.path
    }

    fun init() {
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        Adapter.Companion.myDialog = Dialog(requireContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen)
        Adapter.Companion.myDialog!!.setContentView(R.layout.dialog_box_edit)
        uploadImg = Adapter.Companion.myDialog!!.findViewById<ImageView>(R.id.imageViewUpload)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView!!.layoutManager = linearLayoutManager
        person = NotesFragmentArgs.fromBundle(requireArguments()!!).person
        notes = db!!.getAllNotes(person!!.id)
        adapter = Adapter(this@NotesFragment, requireContext(), notes, person!!)
        recyclerView!!.adapter = adapter
    }

    companion object {
        const val PICK_IMAGE = 5
        const val READ_EXTERNAL_STORAGE = 101
        var selectedImagePath: String? = null
    }
}
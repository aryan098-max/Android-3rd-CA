package com.example.notes
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.text.TextWatcher
import android.text.Editable
import java.util.HashSet

class NoteEditorActivity : AppCompatActivity() {
    var noteId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_editor)
        val editText = findViewById<EditText>(R.id.editText)

        // Fetch data that is passed from MainActivity
        val intent = intent

        // Accessing the data using key and value
        noteId = intent.getIntExtra("noteId", -1)
        if (noteId != -1) {
            editText.setText(MainActivity.notes[noteId])
        } else {
            MainActivity.notes.add("")
            noteId = MainActivity.notes.size - 1
            MainActivity.arrayAdapter!!.notifyDataSetChanged()
        }
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                MainActivity.notes[noteId] = charSequence.toString()
                MainActivity.arrayAdapter!!.notifyDataSetChanged()

                val sharedPreferences =
                    applicationContext.getSharedPreferences("com.example.notes", MODE_PRIVATE)
                val set: HashSet<String> = HashSet(MainActivity.notes)
                sharedPreferences.edit().putStringSet("notes", set).apply()
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
    }
}
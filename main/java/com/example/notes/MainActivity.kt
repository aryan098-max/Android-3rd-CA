package com.example.notes
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import java.util.ArrayList
import java.util.HashSet

class MainActivity : AppCompatActivity() {
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == R.id.add_note) {

            val intent = Intent(applicationContext, NoteEditorActivity::class.java)
            startActivity(intent)
            return true
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val listView = findViewById<ListView>(R.id.listView)
        val sharedPreferences =
            applicationContext.getSharedPreferences("com.example.notes", MODE_PRIVATE)
        val set = sharedPreferences.getStringSet("notes", null) as HashSet<String>?
        if (set == null) {
            notes.add("Example note")
        } else {
            notes = ArrayList(set)
        }

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, notes)
        listView.adapter = arrayAdapter
        listView.onItemClickListener =
            OnItemClickListener { adapterView, view, i, l -> // Going from MainActivity to NotesEditorActivity
                val intent = Intent(applicationContext, NoteEditorActivity::class.java)
                intent.putExtra("noteId", i)
                startActivity(intent)
            }
        listView.onItemLongClickListener = OnItemLongClickListener { adapterView, view, i, l ->
            val itemToDelete = i
            AlertDialog.Builder(this@MainActivity)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure?")
                .setMessage("Do you want to delete this note?")
                .setPositiveButton("Yes") { dialogInterface, i ->
                    notes.removeAt(itemToDelete)
                    arrayAdapter!!.notifyDataSetChanged()
                    val sharedPreferences =
                        applicationContext.getSharedPreferences("com.example.notes", MODE_PRIVATE)
                    val set: HashSet<String> = HashSet<String>(notes)
                    sharedPreferences.edit().putStringSet("notes", set).apply()
                }.setNegativeButton("No", null).show()
            true
        }
    }

    companion object {
        @JvmField
        var notes = ArrayList<String>()
        @JvmField
        var arrayAdapter: ArrayAdapter<String>? = null
    }
}
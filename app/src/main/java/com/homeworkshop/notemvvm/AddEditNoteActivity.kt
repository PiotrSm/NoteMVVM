package com.homeworkshop.notemvvm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_note.*

class AddEditNoteActivity : AppCompatActivity() {
    lateinit var currentNote: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        number_picker_priority.setMinValue(1)
        number_picker_priority.setMaxValue(10)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        //ustawiamiamy tytuł na belce i wyświetlane pola wartościami z intenta jeżeli przyjdzie on z intencją edytowania
        val intent = intent
        if (intent.hasExtra(EXTRA_NOTE)) {
            currentNote = intent.getSerializableExtra(EXTRA_NOTE) as Note
            Log.i(TAG, "onCreate: $currentNote")

            title = "Edit Note"
            edit_text_title.setText(currentNote.title)
            edit_text_description.setText(currentNote.description)
            number_picker_priority.setValue(currentNote.priority)
        } else {
            title = "Add note"
        }
    }

    private fun saveNote() {
        val title = edit_text_title.text.toString()
        val description = edit_text_description.text.toString()
        val priority = number_picker_priority.value
        if (title.trim { it <= ' ' }.isEmpty() || description.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show()
            return
        }
        val data = Intent()
        if (::currentNote.isInitialized) {
            currentNote.title = title
            currentNote.description = description
            currentNote.priority = priority
            data.putExtra(EXTRA_NOTE, currentNote)
        } else {
            val newNote = Note(title, description, priority)
            data.putExtra(EXTRA_NOTE, newNote)
        }
        setResult(RESULT_OK, data) //jeżeli wszystko pójdzie ok to wróci do poprzedniej actywności z tymi danymi
        finish()
    }

    //Dodaje menu na belce po prawej
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Obsługuje zdarzenia na elementach menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_note -> {
                saveNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val TAG = "AddEditNoteActivity"
        const val EXTRA_NOTE = "com.homeworkshop.notemvvm.EXTRA_NOTE"
    }
}
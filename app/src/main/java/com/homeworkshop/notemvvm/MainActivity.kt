package com.homeworkshop.notemvvm

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.homeworkshop.notemvvm.MainActivity

class MainActivity : AppCompatActivity() {
     lateinit var noteViewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<FloatingActionButton>(R.id.button_add_note)
        // Chcemy aby przeniosło nas do AddNoteActivity i wróciło z rezultatami
        button.setOnClickListener { //nie możemy przekazać poprostu 'this' bo 'this' wskazuje na OnClickListener. Musimy przekazać MainActivity.this
            val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
            //Podajemy różne request code żeby rozróżnić wywyłania dla różnych aktywności, dlatego najlepiej mieć stałą dla każdej wywoływanej aktywności
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        //Set it true if you know that recyclerView size won't change, because it make more efficient
        recyclerView.setHasFixedSize(true)

        //Tworzymy adapter i przypisujemy go do recyclerView
        val adapter = NoteAdapter()
        recyclerView.adapter = adapter // ale w tym momencie adapter jest pusty dlatego musimy go uzupełnić w onChanged method w ViewModel


        val database = NoteDatabase(this)
        val repository = NoteRepository(database)
        val factory = NoteViewModelFactory(repository)

        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)
        //ta metoda jest uruchamiana tylko jeżeli dostarczymy referencje do obecej activity
        noteViewModel!!.getAllNotes().observe(this, { notes -> //za każdym razem jak się zmienią dane w ViewModel zostanią uaktualnione notatki w adapterze
            adapter.submitList(notes)
        })
        //klasa która obsługuje zdarzenia wywoływane na SWIPE
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //usuwamy notatkę z viewModel z określonej pozycji wskazanej przez viewHolder i wybranej z Adaptera
                noteViewModel.delete(adapter.getNoteAt(viewHolder.adapterPosition))
                // nie potrzebujemy nic updatować bo update wykonuje się automatycznie sam
                Toast.makeText(this@MainActivity, "Note deleted", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(recyclerView) // na końcu musimy ItemTouchHelper dołączyć do recyclerView żeby działał

        adapter.setOnItemClickListener (object : NoteAdapter.OnItemClickListener{

            override fun onItemClick(note: Note) {
                val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
                //przekazujemy cała notatkę
                intent.putExtra(AddEditNoteActivity.EXTRA_NOTE, note)
                //uruchamiamy activity z osobnym numerem requestu aby rezultaty odbierać dotyczace tego konkretnego requestu (EDIT)
                startActivityForResult(intent, EDIT_NOTE_REQUEST)
            }
        })
    }


    //metoda wywoływana kiedy wrócą rezultaty z wywoływanej aktywności. W tym przypadku z AddNoteActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            val note = data?.getSerializableExtra(AddEditNoteActivity.EXTRA_NOTE) as Note
            if (note != null) {
                noteViewModel?.insert(note)
                Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Note can't be saved", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            val note = data!!.getSerializableExtra(AddEditNoteActivity.EXTRA_NOTE) as Note?
            if (note == null) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show()
            }
            noteViewModel!!.update(note!!)
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all_notes -> {
                noteViewModel!!.deleteAllNotes()
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        //stała indentyfikująca wywoływaną w intentcie aktywność, dla każdej wywoływanej aktywności inny kod.
        const val ADD_NOTE_REQUEST = 1
        private const val EDIT_NOTE_REQUEST = 2
    }
}
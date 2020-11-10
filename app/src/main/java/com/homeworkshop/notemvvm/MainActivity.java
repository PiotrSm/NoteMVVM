package com.homeworkshop.notemvvm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static com.homeworkshop.notemvvm.AddEditNoteActivity.*;

public class MainActivity extends AppCompatActivity {
    //stała indentyfikująca wywoływaną w intentcie aktywność, dla każdej wywoływanej aktywności inny kod.
    public static final int ADD_NOTE_REQUEST = 1;
    private static final int EDIT_NOTE_REQUEST = 2;
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton button = findViewById(R.id.button_add_note);
        // Chcemy aby przeniosło nas do AddNoteActivity i wróciło z rezultatami
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nie możemy przekazać poprostu 'this' bo 'this' wskazuje na OnClickListener. Musimy przekazać MainActivity.this
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                //Podajemy różne request code żeby rozróżnić wywyłania dla różnych aktywności, dlatego najlepiej mieć stałą dla każdej wywoływanej aktywności
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Set it true if you know that recyclerView size won't change, because it make more efficient
        recyclerView.setHasFixedSize(true);

        //Tworzymy adapter i przypisujemy go do recyclerView
        NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter); // ale w tym momencie adapter jest pusty dlatego musimy go uzupełnić w onChanged method w ViewModel


//        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class); // to jest depracated
        // nowy sposób inicjalizacji ViewModelu - konstruktorem
        // jeżeli używamy viewModel we Fragmencie jako ownera podajemy Fragment albo metodę getActivity()
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        //ta metoda jest uruchamiana tylko jeżeli dostarczymy referencje do obecej activity
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //za każdym razem jak się zmienią dane w ViewModel zostanią uaktualnione notatki w adapterze
                adapter.submitList(notes);
            }
        });
        //klasa która obsługuje zdarzenia wywoływane na SWIPE
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //usuwamy notatkę z viewModel z określonej pozycji wskazanej przez viewHolder i wybranej z Adaptera
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                // nie potrzebujemy nic updatować bo update wykonuje się automatycznie sam

                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView); // na końcu musimy ItemTouchHelper dołączyć do recyclerView żeby działał

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                //Alt  + F6 refactoring rename
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                //przekazujemy cała notatkę
                intent.putExtra(EXTRA_NOTE, note);
                //uruchamiamy activity z osobnym numerem requestu aby rezultaty odbierać dotyczace tego konkretnego requestu (EDIT)
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });
    }

    //metoda wywoływana kiedy wrócą rezultaty z wywoływanej aktywności. W tym przypadku z AddNoteActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {

            Note note = (Note) data.getSerializableExtra(EXTRA_NOTE);
            if (note != null) {
                noteViewModel.insert(note);
                Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Note can't be saved", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            Note note = (Note) data.getSerializableExtra(EXTRA_NOTE);
            if (note == null) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
            }
            noteViewModel.update(note);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
package com.homeworkshop.notemvvm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static com.homeworkshop.notemvvm.AddNoteActivity.*;

public class MainActivity extends AppCompatActivity {
    //stała indentyfikująca wywoływaną w intentcie aktywność, dla każdej wywoływanej aktywności inny kod.
    public static final int ADD_NOTE_REQUEST = 1;
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
                Intent intent = new Intent(MainActivity.this,AddNoteActivity.class);
                //Podajemy różne request code żeby rozróżnić wywyłania dla różnych aktywności, dlatego najlepiej mieć stałą dla każdej wywoływanej aktywności
                startActivityForResult(intent,ADD_NOTE_REQUEST);
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
                adapter.setNotes(notes);
            }
        });
    }

    //metoda wywoływana kiedy wrócą rezultaty z wywoływanej aktywności. W tym przypadku z AddNoteActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK){
            String title = data.getStringExtra(EXTRA_TITLE);
            String description = data.getStringExtra(EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(EXTRA_PRIRITY,1);

            Note note = new Note(title,description,priority);
            noteViewModel.insert(note);
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }
}
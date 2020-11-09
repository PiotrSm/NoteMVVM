package com.homeworkshop.notemvvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Set it true if you know that recyclerView size won't change, because it make more efficient
        recyclerView.setHasFixedSize(true);

        //Tworzymy adapter i przypisujemy go do recyclerView
        NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter); // ale w tym momencie adapter jest pusty dlatego musimy go uzupełnić w onChanged method w ViewModel


//        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class); // to jest depracated
        // nowy sposób inicjalizacji ViewModelu - konstruktorem
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
}
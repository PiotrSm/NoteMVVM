package com.homeworkshop.notemvvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class); // to jest depracated
        // nowy sposób inicjalizacji ViewModelu - konstruktorem
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        //ta metoda jest uruchamiana tylko jeżeli dostarczymy referencje do obecej activity
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //update RecylcerViere
                Toast.makeText(MainActivity.this, "onChanged", Toast.LENGTH_LONG).show();//testowo
            }
        });
    }
}
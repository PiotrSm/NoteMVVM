package com.homeworkshop.notemvvm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditNoteActivity extends AppCompatActivity {
    private static final String TAG = "AddEditNoteActivity";
    public static final String EXTRA_TITLE = "com.homeworkshop.notemvvm.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.homeworkshop.notemvvm.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRI0RITY = "com.homeworkshop.notemvvm.EXTRA_PRIRITY";
    public static final String EXTRA_ID = "com.homeworkshop.notemvvm.EXTRA_ID";
    public static final String EXTRA_NOTE = "com.homeworkshop.notemvvm.EXTRA_NOTE";
    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;
    Note currentNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);

        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        //ustawiamiamy tytuł na belce i wyświetlane pola wartościami z intenta jeżeli przyjdzie on z intencją edytowania
        Intent intent = getIntent();

        currentNote = (Note) intent.getSerializableExtra(EXTRA_NOTE);
        Log.i(TAG, "onCreate: "+ currentNote);

        if (currentNote != null) {
            setTitle("Edit Note");
            editTextTitle.setText(currentNote.getTitle());
            editTextDescription.setText((currentNote.getDescription()));
            numberPickerPriority.setValue(currentNote.getPriority());
        } else {
            setTitle("Add note");
        }
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = numberPickerPriority.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();

        if(currentNote != null){
            currentNote.setTitle(title);
            currentNote.setDescription(description);
            currentNote.setPriority(priority);
            data.putExtra(EXTRA_NOTE,currentNote);
        }else{
            Note newNote = new Note(title,description,priority);
            data.putExtra(EXTRA_NOTE,newNote);
        }

        setResult(RESULT_OK, data); //jeżeli wszystko pójdzie ok to wróci do poprzedniej actywności z tymi danymi
        finish();
    }

    //Dodaje menu na belce po prawej
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Obsługuje zdarzenia na elementach menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
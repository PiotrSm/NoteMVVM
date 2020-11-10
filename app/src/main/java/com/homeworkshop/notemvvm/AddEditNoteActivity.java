package com.homeworkshop.notemvvm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE = "com.homeworkshop.notemvvm.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.homeworkshop.notemvvm.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRI0RITY = "com.homeworkshop.notemvvm.EXTRA_PRIRITY";
    public static final String EXTRA_ID = "com.homeworkshop.notemvvm.EXTRA_ID";
    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;

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
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText((intent.getStringExtra(EXTRA_DESCRIPTION)));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRI0RITY,1));
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
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRI0RITY, priority);

        //pobieramy id z intent extra a jeżeli go nie ma to ustawiamy -1 wiedząc że takiego id nie będzie w realu
        int id = getIntent().getIntExtra(EXTRA_ID,-1);
        if(id != -1){
            //jeżeli w intencie była jakieś konkretne id to dadajemy je do rezultow do responsu
            data.putExtra(EXTRA_ID,id);
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
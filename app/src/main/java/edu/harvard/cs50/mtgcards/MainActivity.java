package edu.harvard.cs50.mtgcards;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String USER_SEARCH = "edu.harvard.cs50.mtgcards.USER_SEARCH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void searchCard(View v) {
        Intent intent = new Intent(this, SearchAdapter.class);
        EditText editText = findViewById(R.id.search_bar);
        String search = editText.getText().toString();
        intent.putExtra(USER_SEARCH, search);
        startActivity(intent);
    }
}

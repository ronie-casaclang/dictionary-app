package com.casa.dictionaryapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Components
    EditText txtWord;
    Button btnMeaning, btnSynonym, btnAntonym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize
        txtWord = findViewById(R.id.txtWord);
        btnMeaning = findViewById(R.id.btnMeaning);
        btnSynonym = findViewById(R.id.btnSynonym);
        btnAntonym = findViewById(R.id.btnAntonym);


        // Meaning Button
        btnMeaning.setOnClickListener(v -> {
            String word = txtWord.getText().toString().trim();
            if (!word.isEmpty()) {
                Toast.makeText(MainActivity.this,
                        "Meaning of " + word + ": (Example meaning here)",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this,
                        "Please enter a word!", Toast.LENGTH_SHORT).show();
            }
        });

        // Synonym Button
        btnSynonym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = txtWord.getText().toString().trim();
                if (!word.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "Synonym of " + word + ": (Example synonym here)",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Please enter a word!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Antonym Button
        btnAntonym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = txtWord.getText().toString().trim();
                if (!word.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "Antonym of " + word + ": (Example antonym here)",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Please enter a word!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
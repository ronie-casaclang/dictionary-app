package com.casa.dictionaryapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Components
    EditText txtWord;
    Button btnMeaning, btnExample, btnSynonym, btnAntonym;
    RequestQueue requestQueue;

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
        btnExample = findViewById(R.id.btnExample);
        btnSynonym = findViewById(R.id.btnSynonym);
        btnAntonym = findViewById(R.id.btnAntonym);
        requestQueue = Volley.newRequestQueue(this);

        // Button Clicks
        btnMeaning.setOnClickListener(v -> fetchWordData("meaning"));
        btnExample.setOnClickListener(v -> fetchWordData("example"));
        btnSynonym.setOnClickListener(v -> fetchWordData("synonym"));
        btnAntonym.setOnClickListener(v -> fetchWordData("antonym"));
    }


    // Fetch Word from Dictionary
    private void fetchWordData(String type) {
        String raw = txtWord.getText().toString().trim();
        if (raw.isEmpty()) {
            Toast.makeText(this, "Please enter a word!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Make the URL safe and normalize the word
        String url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + android.net.Uri.encode(raw.toLowerCase(java.util.Locale.US));

        com.android.volley.toolbox.JsonArrayRequest request =
                new com.android.volley.toolbox.JsonArrayRequest(
                        com.android.volley.Request.Method.GET,
                        url,
                        null,
                        response -> {
                            try {
                                // Use LinkedHashSet to keep order and avoid duplicates
                                java.util.Set<String> synSet = new java.util.LinkedHashSet<>();
                                java.util.Set<String> antSet = new java.util.LinkedHashSet<>();
                                String firstDefinition = null;
                                String firstExample = null;

                                // Iterate ALL entries, meanings, and definitions
                                for (int e = 0; e < response.length(); e++) {
                                    org.json.JSONObject entry = response.getJSONObject(e);
                                    org.json.JSONArray meanings = entry.optJSONArray("meanings");
                                    if (meanings == null) continue;

                                    for (int i = 0; i < meanings.length(); i++) {
                                        org.json.JSONObject meaning = meanings.getJSONObject(i);

                                        // meaning-level synonyms/antonyms
                                        org.json.JSONArray mSyn = meaning.optJSONArray("synonyms");
                                        if (mSyn != null) {
                                            for (int k = 0; k < mSyn.length(); k++) {
                                                synSet.add(mSyn.getString(k));
                                            }
                                        }
                                        org.json.JSONArray mAnt = meaning.optJSONArray("antonyms");
                                        if (mAnt != null) {
                                            for (int k = 0; k < mAnt.length(); k++) {
                                                antSet.add(mAnt.getString(k));
                                            }
                                        }

                                        // definitions-level synonyms/antonyms
                                        org.json.JSONArray definitions = meaning.optJSONArray("definitions");
                                        if (definitions == null) continue;

                                        for (int j = 0; j < definitions.length(); j++) {
                                            org.json.JSONObject def = definitions.getJSONObject(j);

                                            if (firstDefinition == null) {
                                                firstDefinition = def.optString("definition", null);
                                            }

                                            // ✅ Capture first sample example
                                            if (firstExample == null && def.has("example")) {
                                                firstExample = def.optString("example");
                                            }

                                            org.json.JSONArray dSyn = def.optJSONArray("synonyms");
                                            if (dSyn != null) {
                                                for (int k = 0; k < dSyn.length(); k++) {
                                                    synSet.add(dSyn.getString(k));
                                                }
                                            }

                                            org.json.JSONArray dAnt = def.optJSONArray("antonyms");
                                            if (dAnt != null) {
                                                for (int k = 0; k < dAnt.length(); k++) {
                                                    antSet.add(dAnt.getString(k));
                                                }
                                            }
                                        }
                                    }
                                }

                                switch (type) {
                                    case "meaning":
                                        if (firstDefinition != null && !firstDefinition.isEmpty()) {
                                            Toast.makeText(this, "Meaning: " + firstDefinition, Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(this, "No meaning found.", Toast.LENGTH_SHORT).show();
                                        }
                                        break;

                                    case "example":  // ✅ NEW
                                        if (firstExample != null) {
                                            Toast.makeText(this, "Example: " + firstExample, Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(this, "No example sentence found!", Toast.LENGTH_SHORT).show();
                                        }
                                        break;

                                    case "synonym":
                                        if (!synSet.isEmpty()) {
                                            String synText = android.text.TextUtils.join(", ", synSet);
                                            Toast.makeText(this, "Synonyms: " + synText, Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(this, "No synonym found!", Toast.LENGTH_SHORT).show();
                                        }
                                        break;

                                    case "antonym":
                                        if (!antSet.isEmpty()) {
                                            String antText = android.text.TextUtils.join(", ", antSet);
                                            Toast.makeText(this, "Antonyms: " + antText, Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(this, "No antonym found!", Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                }

                            } catch (org.json.JSONException ex) {
                                ex.printStackTrace();
                                Toast.makeText(this, "Error parsing data!", Toast.LENGTH_SHORT).show();
                            }
                        },
                        error -> {
                            Toast.makeText(this, "Word not found!", Toast.LENGTH_SHORT).show();
                        }
                );

        // Avoid stale cached responses just in case
        request.setShouldCache(false);
        requestQueue.add(request);
    }



}
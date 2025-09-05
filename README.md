# 1: Free Dictionary API

### DictionaryAPI.dev
- Free & open source, no API key needed.
- Supports meanings, synonyms, antonyms, phonetics.
- URL Example: https://api.dictionaryapi.dev/api/v2/entries/en/<WORD>

# 2: Add Internet Permission

Go to AndroidManifest.xml file
uses-permission android:name="android.permission.INTERNET"

# 3. Add Networking Library

Go to build.gradle.kts file
dependencies {
    implementation("com.android.volley:volley:1.2.1")
}

# 4: Update MainActivity.java

RequestQueue requestQueue;
requestQueue = Volley.newRequestQueue(this);
fetchWordData("keyword");

package com.example.demo_recorder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static Integer  audiorequest= 1;
    private SpeechRecognizer speechRecognizer;
    private EditText text1,text2;
    private ImageView mic,mic1;
    Spinner spin;String selected,lan,selected1;
    HashMap<String,String> list = new HashMap<>();
   Intent intent=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text1 = findViewById(R.id.text1);
        mic = findViewById(R.id.mic);
        text2 = findViewById(R.id.text2);

        spin=findViewById(R.id.spinner);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},audiorequest);
        }
        list.put("English","en");
        list.put("Tamil","ta");
        list.put("Hindi","hi");



        ArrayAdapter<CharSequence> aa = ArrayAdapter.createFromResource(this,R.array.languages,android.R.layout.simple_spinner_item);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        spin.setPrompt("");
        spin.setOnItemSelectedListener(this);
        lan=list.get(selected1);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
            }
            @Override
            public void onBeginningOfSpeech() {
                text1.setHint("Listening...");

                text2.setEnabled(false);
            }

            @Override
            public void onRmsChanged(float v) {}
            @Override
            public void onBufferReceived(byte[] bytes) {}
            @Override
            public void onEndOfSpeech() {}
            @Override
            public void onError(int i) {}
            @Override
            public void onResults(Bundle bundle) {

                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                text2.setEnabled(true);text2.setText(data.get(0));text1.setHint("Tap to Speak");
            }

            @Override
            public void onPartialResults(Bundle bundle) {}
            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speechRecognizer.startListening(intent);text2.setText("");
            }
        });

    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
      selected = (parent.getItemAtPosition(pos)).toString();
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                list.get(selected));
    }
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == audiorequest && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }
}



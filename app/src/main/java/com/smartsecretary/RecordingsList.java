package com.smartsecretary;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class RecordingsList extends AppCompatActivity {

    private TranscriptionService transcriptionService = new TranscriptionService();
    private RecordService recordService = new RecordService();
    private SummarizeService summarizeService = new SummarizeService();
    String results = null;
    private static final String FILEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/smart-secretary/";
    private static final String FILENAME = Environment.getExternalStorageDirectory().getAbsolutePath() + "smart-secretary/transcript.txt";
    ViewDialogue viewDialog;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager recycleLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        runPython(this);
        viewDialog = new ViewDialogue(this);
        super.onCreate(savedInstanceState);
        File[] files = getExternalFilesDirs(FILEPATH);
        System.out.println(files);

        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        /*

        mAdapter = new MyAdapter(files);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        recyclerView.setAdapter(mAdapter);
*/

        final TextView view = (TextView) findViewById(R.id.textView);

        //RECORD BUTTON
        final FloatingActionButton record = (FloatingActionButton) findViewById(R.id.Record);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
            }
        });

        //STOP BUTTON
        FloatingActionButton play = (FloatingActionButton) findViewById(R.id.Play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordService.play();
            }
        });

        //PLAY BUTTON
        FloatingActionButton stop = (FloatingActionButton) findViewById(R.id.Stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordService.stopRecording();
            }
        });

        //TRANSCRIBE BUTTON
        final FloatingActionButton transcribe = (FloatingActionButton) findViewById(R.id.Transcribe);
        transcribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AssetManager assetManager = getAssets();
                    InputStream jsonStream = null;
                    try { jsonStream = assetManager.open("google_application_credentials.json"); }
                    catch (IOException e) { e.printStackTrace(); }
                    GoogleCredentials credential = null;
                    try { credential = GoogleCredentials.fromStream(jsonStream); }
                    catch (IOException e) { e.printStackTrace(); }

                    results = transcriptionService.transcribe(credential);
                    writeUsingFileWriter(results);
                    view.setText(results);
                } catch (Exception e) { e.printStackTrace(); }
            }
        });

        //SUMMARIZE
        final FloatingActionButton summarize = (FloatingActionButton) findViewById(R.id.Summarize);
        summarize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String summary = summarizeService.summarize();
                view.setText(summary);
            }
        });
    }

    private void runPython(Context context){
        if(!Python.isStarted()) {
            Python.start(new AndroidPlatform(context));
        }
    }

    private void startRecording(){
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialogue_box, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        Button cancel = (Button) promptsView.findViewById(R.id.save_cancel);
        Button ok = (Button) promptsView.findViewById(R.id.save_ok);


        // set dialog message
        alertDialogBuilder
                .setCancelable(false);

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newFileName = userInput.getText().toString();
                recordService.startRecording(newFileName);
                    alertDialog.dismiss();
            }
        });
        // show it
        alertDialog.show();

    }

    private static void writeUsingFileWriter(String data) {
        File file = new File(FILEPATH + "/Sample-text.txt");
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //close resources
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
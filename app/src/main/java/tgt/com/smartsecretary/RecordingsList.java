package tgt.com.smartsecretary;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.auth.oauth2.GoogleCredentials;

import java.io.IOException;
import java.io.InputStream;

public class RecordingsList extends AppCompatActivity {

    private TranscriptionService transcriptionService = new TranscriptionService();
    private RecordService recordService = new RecordService();
    String results = null;

    private static final String FILENAME = Environment.getExternalStorageDirectory().getAbsolutePath() + "smart-secretary/transcript.txt";

    public RecordingsList() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final TextView view = (TextView) findViewById(R.id.textView);

        //RECORD BUTTON
        final FloatingActionButton record = (FloatingActionButton) findViewById(R.id.Record);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordService.startRecording();
            }
        });

        //STOP BUTTON
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
                    view.setText("Listening to Audio and Transcribing!!");
                    AssetManager assetManager = getAssets();
                    InputStream jsonStream = null;
                    try { jsonStream = assetManager.open("google_application_credentials.json"); }
                    catch (IOException e) { e.printStackTrace(); }
                    GoogleCredentials credential = null;
                    try { credential = GoogleCredentials.fromStream(jsonStream); }
                    catch (IOException e) { e.printStackTrace(); }

                    results = transcriptionService.transcribe(credential);
//                    Process p = Runtime.getRuntime().exec("/Users/z00295r/anaconda/bin/python", "");
                    view.setText(results);
                } catch (Exception e) { e.printStackTrace(); }
            }
        });
    }


//    //SUMMARIZE
//    final


    /*
    private void runPython(Context context){
        if(!Python.isStarted()) {
            Python.start(new AndroidPlatform(context));
        }
    }
    */
}
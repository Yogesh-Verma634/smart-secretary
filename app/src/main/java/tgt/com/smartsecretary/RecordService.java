package tgt.com.smartsecretary;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import com.google.cloud.speech.v1p1beta1.RecognitionMetadata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A sample program is to demonstrate how to record sound in Java
 * author: www.codejava.net
 */
public class RecordService {

    private MediaRecorder mediaRecorder = new MediaRecorder();
    private String outputFile = Environment.getExternalStorageDirectory().
            getAbsolutePath();

    AudioRecord audioRecord = null;
    private Thread recordingThread = null;
    // record duration, in milliseconds
    public void startRecording() {
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setOutputFile(outputFile);

        try {
            mediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("RecordTest", "Started Recording");
        mediaRecorder.start();
    }
    public void stopRecording(){
        mediaRecorder.stop();
        mediaRecorder.release();
        Log.i("RecordTest", "Stopped Recording");
    }


}

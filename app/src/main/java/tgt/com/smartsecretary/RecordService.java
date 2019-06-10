package tgt.com.smartsecretary;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;

/**
 * A sample program is to demonstrate how to record sound in Java
 * author: www.codejava.net
 */
public class RecordService {

    private MediaRecorder mediaRecorder = null;
    private MediaPlayer mediaPlayer = null;
    private String outputPath = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/smart-secretary/";
    // record duration, in milliseconds
    public void startRecording(String filename) {
        String outputFile = outputPath + filename;
        System.out.println(outputFile);
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setOutputFile(outputFile);
        try {
            mediaRecorder.prepare();
            System.out.println("media prepared");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("RecordTest", "Started Recording");
        mediaRecorder.start();
    }
    public void stopRecording(){
        if(mediaRecorder!=null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
        Log.i("RecordTest", "Stopped Recording");
    }


    public void play() {
        String outputFile = outputPath + "sample.wav";
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(outputFile);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e("MediaPlayer", "prepare() failed");
        }
    }
}

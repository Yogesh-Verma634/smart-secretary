package tgt.com.smartsecretary;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

/**
 * A sample program is to demonstrate how to record sound in Java
 * author: www.codejava.net
 */
public class RecordService {

    private MediaRecorder mediaRecorder = null;
    private String outputFile = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/sample.wav";
    // record duration, in milliseconds
    public void startRecording() {
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
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        Log.i("RecordTest", "Stopped Recording");
    }


}

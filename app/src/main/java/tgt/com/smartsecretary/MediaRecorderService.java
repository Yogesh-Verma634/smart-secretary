package tgt.com.smartsecretary;

import android.media.MediaRecorder;

import java.io.IOException;

public class MediaRecorderService {

    private MediaRecorder mediaRecorder = new MediaRecorder();

    private String outputFile = "/Users/z00295r/Documents/Work/TIHackathon/SmartSecretary/app/src/main/java/AudioRecorder/record1.wav";

    public void startRecording() throws IOException {
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(outputFile);
        mediaRecorder.prepare();
        mediaRecorder.start();
    }

    public void stopRecording(){
    }

}


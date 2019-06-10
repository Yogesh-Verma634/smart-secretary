package com.smartsecretary;

import android.os.Environment;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.cloud.speech.v1.SpeechSettings;
import com.google.protobuf.ByteString;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TranscriptionService {

    private static final String FILEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/smart-secretary";

    public String transcribe(GoogleCredentials credentials) throws Exception {
        FixedCredentialsProvider credentialsProvider = FixedCredentialsProvider.create(credentials);
        SpeechSettings speechSettings =
                SpeechSettings.newBuilder()
                        .setCredentialsProvider(credentialsProvider)
                        .build();
        // Instantiates a client
        SpeechClient speechClient = SpeechClient.create(speechSettings);
        String content = "";
        // The path to the audio file to transcribe
        String fileName = Environment.getExternalStorageDirectory().
                getAbsolutePath() + "/smart-secretary/sample.wav";

        // Reads the audio file into memory
        Path path = Paths.get(fileName);
        byte[] data = Files.readAllBytes(path);
        ByteString audioBytes = ByteString.copyFrom(data);

        // Builds the sync recognize request
        RecognitionConfig config = RecognitionConfig.newBuilder()
                .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                .setSampleRateHertz(8000)
                .setLanguageCode("en-US")
                .build();
        RecognitionAudio audio = RecognitionAudio.newBuilder()
                .setContent(audioBytes)
                .build();

        // Performs speech recognition on the audio file
        RecognizeResponse response = speechClient.recognize(config, audio);
        List<SpeechRecognitionResult> results = response.getResultsList();

        for (SpeechRecognitionResult result : results) {
            // There can be several alternative transcripts for a given chunk of speech. Just use the
            // first (most likely) one here.
            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
        }
        for (SpeechRecognitionResult result : results) {

            content += result.getAlternatives(0).getTranscript() + ". ";
//                            Log.i("Transcription: ", result.getAlternatives(0).getTranscript());
        }
//                        BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME));
        System.out.println(content);
        return content;
    }
}

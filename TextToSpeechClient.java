import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.sound.sampled.*;

public class TextToSpeechClient {
    private final String apiKey;
    private final String model = "facebook/fastspeech2-en-ljspeech";

    public TextToSpeechClient(String apiKey) { 
        this.apiKey = apiKey;
    }

    public void speakText(String text) throws IOException {
        System.out.println("HuggingFace integration has been removed. Text: " + text);
    }
} 
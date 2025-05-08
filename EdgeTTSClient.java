import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.sound.sampled.*;

public class EdgeTTSClient {
    private Process currentProcess;
    private Process afplayProcess;
    private Thread playbackThread;
    private static final String EDGE_TTS_COMMAND = "\"/Users/saahil/Documents/clearpass_ai sound copy bfr mod 2/venv/bin/edge-tts\"";
    private static final String VOICE = "en-US-JennyNeural"; // Default voice
    private static final String FFMPEG_COMMAND = "ffmpeg"; // Make sure ffmpeg is installed
    
    public EdgeTTSClient() {
        // Check if edge-tts is installed
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"bash", "-c", EDGE_TTS_COMMAND + " --list-voices"});
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String version = reader.readLine();
            System.out.println("Edge-TTS is installed");
            
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Edge-TTS is not installed. Please install it first using: pip install edge-tts");
            }

            // Check if ffmpeg is installed
            process = Runtime.getRuntime().exec(new String[]{"bash", "-c", "which ffmpeg"});
            exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("ffmpeg is not installed. Please install it first using: brew install ffmpeg");
            }
            System.out.println("ffmpeg is installed");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error checking dependencies: " + e.getMessage());
        }
    }

    public void speakText(String text) throws IOException {
        try {
            // Stop any existing speech
            stopSpeaking();
            
            System.out.println("Attempting to speak text: " + text);
            
            // Create temporary files
            File tempMp3File = File.createTempFile("speech", ".mp3");
            File tempWavFile = File.createTempFile("speech", ".wav");
            
            // Use edge-tts to generate speech
            ProcessBuilder pb = new ProcessBuilder(
                "bash",
                "-c",
                EDGE_TTS_COMMAND + " --voice " + VOICE + " --text \"" + text.replace("\"", "\\\"") + "\" --write-media " + tempMp3File.getAbsolutePath()
            );
            
            System.out.println("Starting Edge-TTS process...");
            currentProcess = pb.start();
            
            // Capture any error output
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(currentProcess.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println("Edge-TTS error: " + errorLine);
            }
            
            int exitCode = currentProcess.waitFor();
            System.out.println("Edge-TTS process completed with exit code: " + exitCode);
            
            if (exitCode != 0) {
                throw new IOException("Error generating speech: " + exitCode);
            }

            // Convert MP3 to WAV using ffmpeg
            pb = new ProcessBuilder(
                "bash",
                "-c",
                FFMPEG_COMMAND + " -y -i " + tempMp3File.getAbsolutePath() + " -acodec pcm_s16le -ar 44100 " + tempWavFile.getAbsolutePath()
            );
            
            System.out.println("Converting MP3 to WAV...");
            Process ffmpegProcess = pb.start();
            
            // Capture any error output from ffmpeg
            errorReader = new BufferedReader(new InputStreamReader(ffmpegProcess.getErrorStream()));
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println("ffmpeg error: " + errorLine);
            }
            
            exitCode = ffmpegProcess.waitFor();
            System.out.println("ffmpeg process completed with exit code: " + exitCode);
            
            if (exitCode != 0) {
                throw new IOException("Error converting audio: " + exitCode);
            }
            
            // Play the WAV audio file using afplay in a separate thread
            final File finalWavFile = tempWavFile;
            playbackThread = new Thread(() -> {
                try {
                    System.out.println("Attempting to play audio file: " + finalWavFile.getAbsolutePath());
                    afplayProcess = Runtime.getRuntime().exec(new String[]{"afplay", finalWavFile.getAbsolutePath()});
                    System.out.println("Waiting for playback to complete...");
                    afplayProcess.waitFor();
                    System.out.println("Playback completed");
                    
                    // Clean up the temporary files
                    tempMp3File.delete();
                    finalWavFile.delete();
                } catch (IOException | InterruptedException e) {
                    System.err.println("Error during playback: " + e.getMessage());
                }
            });
            playbackThread.start();
            
        } catch (InterruptedException e) {
            System.err.println("Speech generation interrupted: " + e.getMessage());
            throw new IOException("Speech generation interrupted: " + e.getMessage());
        }
    }

    public void stopSpeaking() {
        if (currentProcess != null && currentProcess.isAlive()) {
            currentProcess.destroy();
            try {
                currentProcess.waitFor();
            } catch (InterruptedException e) {
                System.err.println("Error stopping Edge-TTS process: " + e.getMessage());
            }
            currentProcess = null;
        }
        
        if (afplayProcess != null && afplayProcess.isAlive()) {
            afplayProcess.destroy();
            try {
                afplayProcess.waitFor();
            } catch (InterruptedException e) {
                System.err.println("Error stopping afplay process: " + e.getMessage());
            }
            afplayProcess = null;
        }
        
        if (playbackThread != null && playbackThread.isAlive()) {
            playbackThread.interrupt();
            try {
                playbackThread.join(1000); // Wait up to 1 second for the thread to finish
            } catch (InterruptedException e) {
                System.err.println("Error stopping playback thread: " + e.getMessage());
            }
            playbackThread = null;
        }
    }
}
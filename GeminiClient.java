import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class GeminiClient {
    private final String apiKey;
    private final String model;

    public GeminiClient(String apiKey, String model) {
        this.apiKey = apiKey;
        this.model = model;
    }
    
    public String generateResponse(String prompt) throws IOException {
        String inputJson = "{\n" +
                "  \"contents\": [\n" +
                "    {\n" +
                "      \"parts\": [\n" +
                "        { \"text\": \"" + prompt.replace("\"", "\\\"") + "\" }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        @SuppressWarnings("deprecation")
		URL url = new URL(
                "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key=" + apiKey);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = inputJson.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int status = conn.getResponseCode();
        InputStream stream = (status == 200) ? conn.getInputStream() : conn.getErrorStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "utf-8"));

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line.trim());
        }
        br.close();

        return extractTextFromResponse(response.toString());
    }

    private String extractTextFromResponse(String raw) {
        int startIndex = raw.indexOf("\"text\": \"");
        if (startIndex == -1)
            return "Error: Could not parse Gemini response.";
        startIndex += 9;
        int endIndex = raw.indexOf("\"", startIndex);
        if (endIndex == -1)
            return "Error: Response not complete.";
        return raw.substring(startIndex, endIndex).replace("\\n", "\n");
    }
}

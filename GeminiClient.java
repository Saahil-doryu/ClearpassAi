import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class GeminiClient {
    private final String apiKey;
    private final String model;

    public GeminiClient(String apiKey, String model) {
        this.apiKey = apiKey;
        this.model = model;
    }

    public String generateResponse(String prompt) throws IOException {
        String safePrompt = prompt.replace("\"", "\\\"");
        String inputJson = "{\n" +
                "  \"contents\": [\n" +
                "    {\n" +
                "      \"parts\": [\n" +
                "        { \"text\": \"" + safePrompt + "\" }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        URL url = new URL(
                "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key=" + apiKey);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(inputJson.getBytes("utf-8"));
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
        try {
            JSONObject json = new JSONObject(raw);
            JSONArray candidates = json.getJSONArray("candidates");
            JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");
            return parts.getJSONObject(0).getString("text");
        } catch (Exception e) {
            return "Error: Could not parse response.";
        }
    }
}

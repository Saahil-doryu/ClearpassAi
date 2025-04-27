import java.io.IOException;
import java.util.Scanner;

public class AiPrompt {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a job role as an argument.");
            return;
        }

        // Replace with your actual Gemini API key.
        String apiKey = "AIzaSyDjIVnRqT1mcY-mh7QsKBIUF2Eb-__tiac";
        
        // Choose the model, e.g., "gemini-1.5-flash"
        // I chose this because it's free
        String model = "gemini-1.5-flash";

        // Create an instance of the GeminiClient.
        GeminiClient geminiClient = new GeminiClient(apiKey, model);

        String jobRole = args[0];
        String prompt = "Generate an interview question suitable for the job role of " + jobRole;
        String question = "";
        try {
            question = geminiClient.generateResponse(prompt);
            System.out.println("\nInterview Question for " + jobRole + ":\n" + question);
        } catch (IOException e) {
            System.out.println("Error generating question: " + e.getMessage());
            return;
        }

        // Get user's answer I used scanner
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nYour Answer: ");
        String userAnswer = scanner.nextLine();

        // Create an evaluation prompt using the generated question and the user's
        // answer.
        String evaluationPrompt = "Evaluate this answer to the question: \"" + question + "\".\n\nAnswer: \""
                + userAnswer + "\"\n\nGive short, clear feedback on whether it's good or needs improvement. Also what he needs to improve";
        try {
            String feedback = geminiClient.generateResponse(evaluationPrompt);
            System.out.println("\nAI Feedback:\n" + feedback);
        } catch (IOException e) {
            System.out.println("Error evaluating answer: " + e.getMessage());
        }
        scanner.close();
    }
}

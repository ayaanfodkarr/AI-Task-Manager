package com.example.aitaskmanager;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GeminiHelper {

    // Replace with your actual Gemini API key from https://aistudio.google.com/
    private static final String API_KEY = "YOUR_GEMINI_API_KEY_HERE";
    private static final String API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public interface GeminiCallback {
        void onSuccess(String result);
        void onFailure(String error);
    }

    private final OkHttpClient client;

    public GeminiHelper() {
        this.client = new OkHttpClient();
    }

    public void analyzeTask(Task task, GeminiCallback callback) {
        String prompt = buildPrompt(task);
        String requestBody = buildRequestBody(prompt);

        Request request = new Request.Builder()
                .url(API_URL)
                .post(RequestBody.create(requestBody, JSON))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onFailure("API error: HTTP " + response.code());
                    return;
                }
                String body = response.body().string();
                String text = parseResponse(body);
                if (text != null) {
                    callback.onSuccess(text);
                } else {
                    callback.onFailure("Could not parse AI response.");
                }
            }
        });
    }

    private String buildPrompt(Task task) {
        return "You are a productivity expert. Analyze this task and respond in exactly this format:\n\n" +
               "PRIORITY: [HIGH/MEDIUM/LOW]\n" +
               "EFFORT: [short estimate, e.g. '2 hours']\n" +
               "STEPS:\n1. [step]\n2. [step]\n3. [step]\n" +
               "TIP: [one actionable productivity tip]\n\n" +
               "Task title: " + task.getTitle() + "\n" +
               "Description: " + task.getDescription();
    }

    private String buildRequestBody(String prompt) {
        JsonObject part = new JsonObject();
        part.addProperty("text", prompt);

        JsonArray parts = new JsonArray();
        parts.add(part);

        JsonObject content = new JsonObject();
        content.add("parts", parts);

        JsonArray contents = new JsonArray();
        contents.add(content);

        JsonObject body = new JsonObject();
        body.add("contents", contents);

        // Keep response concise
        JsonObject generationConfig = new JsonObject();
        generationConfig.addProperty("maxOutputTokens", 300);
        generationConfig.addProperty("temperature", 0.7);
        body.add("generationConfig", generationConfig);

        return body.toString();
    }

    private String parseResponse(String json) {
        try {
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            return root
                    .getAsJsonArray("candidates")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("content")
                    .getAsJsonArray("parts")
                    .get(0).getAsJsonObject()
                    .get("text").getAsString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Parses priority from AI response text.
     * Returns Task.Priority enum value.
     */
    public static Task.Priority extractPriority(String aiText) {
        if (aiText == null) return Task.Priority.MEDIUM;
        String upper = aiText.toUpperCase();
        if (upper.contains("PRIORITY: HIGH")) return Task.Priority.HIGH;
        if (upper.contains("PRIORITY: LOW"))  return Task.Priority.LOW;
        return Task.Priority.MEDIUM;
    }
}

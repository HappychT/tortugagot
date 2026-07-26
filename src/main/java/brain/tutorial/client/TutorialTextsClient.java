package brain.tutorial.client;

import java.util.HashMap;
import java.util.Map;

public class TutorialTextsClient {
    private static final Map<String, String> texts = new HashMap<>();

    public static void setTexts(Map<String, String> newTexts) {
        texts.clear();
        if (newTexts != null) {
            texts.putAll(newTexts);
        }
    }

    public static String get(String key) {
        return texts.getOrDefault(key, key); // Return key if not found, as fallback
    }
}

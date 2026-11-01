/**
 * BCS751 ARTIFICIAL INTELLIGENCE LAB
 * Practical 12: Remove punctuation and stop words from a paragraph (NLP in Java).
 */

import java.util.*;

public class Practical12 {

    // Standard English stop words corpus (from NLTK)
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
        "a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", 
        "aren't", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", 
        "but", "by", "can", "can't", "cannot", "could", "couldn't", "did", "didn't", "do", "does", 
        "doesn't", "doing", "don't", "down", "during", "each", "few", "for", "from", "further", 
        "had", "hadn't", "has", "hasn't", "have", "haven't", "having", "he", "he'd", "he'll", "he's", 
        "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", 
        "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "isn't", "it", "it's", "its", "itself", 
        "let's", "me", "more", "most", "mustn't", "my", "myself", "no", "nor", "not", "of", "off", 
        "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over", 
        "own", "same", "shan't", "she", "she'd", "she'll", "she's", "should", "shouldn't", "so", 
        "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", 
        "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", 
        "those", "through", "to", "too", "under", "until", "up", "very", "was", "wasn't", "we", 
        "we'd", "we'll", "we're", "we've", "were", "weren't", "what", "what's", "when", "when's", 
        "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", 
        "won't", "would", "wouldn't", "you", "you'd", "you'll", "you're", "you've", "your", "yours"
    ));

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("PRACTICAL 12: REMOVE PUNCTUATION AND STOP WORDS (NLP IN JAVA)");
        System.out.println("============================================================\n");

        String paragraph = "Artificial Intelligence (AI), in its broadest sense, is intelligence exhibited by machines, " +
                           "particularly computer systems! It is a synthesis of concepts from computer science, mathematics, " +
                           "and linguistics; can intelligent agents solve complex real-world tasks effectively?";

        System.out.println("--- Original Input Paragraph ---");
        System.out.println(paragraph);

        // Step 1: Remove punctuation using regex and tokenize
        // Replace all non-alphanumeric characters with spaces
        String cleanText = paragraph.replaceAll("[^a-zA-Z0-9\\s]", " ");
        String[] rawTokens = cleanText.trim().split("\\s+");

        List<String> allTokens = new ArrayList<>();
        for (String t : rawTokens) {
            if (!t.isEmpty()) {
                allTokens.add(t);
            }
        }

        System.out.println("\n--- Step 1: Tokenization & Punctuation Stripping ---");
        System.out.println("Total Tokens Extracted: " + allTokens.size());
        System.out.println("Token List: " + allTokens);

        // Step 2: Stop word removal
        List<String> filteredTokens = new ArrayList<>();
        List<String> removedStopWords = new ArrayList<>();

        for (String token : allTokens) {
            String lower = token.toLowerCase();
            if (STOP_WORDS.contains(lower)) {
                removedStopWords.add(token);
            } else {
                filteredTokens.add(token);
            }
        }

        System.out.println("\n--- Step 2: Stop Words Filtered Out ---");
        System.out.println("Removed Stop Words Count: " + removedStopWords.size());
        System.out.println("Removed Words: " + removedStopWords);

        System.out.println("\n--- Step 3: Final Filtered Keywords ---");
        System.out.println("Remaining Meaningful Tokens Count: " + filteredTokens.size());
        System.out.println("Remaining Tokens: " + filteredTokens);

        String reconstructed = String.join(" ", filteredTokens);
        System.out.println("\n--- Cleaned Text Representation ---");
        System.out.println(reconstructed);

        System.out.println("\n[SUCCESS] Practical 12 executed successfully.");
    }
}

/**
 * BCS751 ARTIFICIAL INTELLIGENCE LAB
 * Practical 13: Perform Stemming and Lemmatization on text in Java.
 */

import java.util.*;

public class Practical13 {

    // Porter Stemmer Rule Implementation
    public static String stem(String word) {
        String w = word.toLowerCase();
        if (w.length() <= 2) return w;

        // Step 1a: Plurals and past participles
        if (w.endsWith("sses")) w = w.substring(0, w.length() - 2);
        else if (w.endsWith("ies")) w = w.substring(0, w.length() - 2);
        else if (w.endsWith("ss")) {} // do nothing
        else if (w.endsWith("s")) w = w.substring(0, w.length() - 1);

        // Step 1b: -eed, -ed, -ing
        if (w.endsWith("eed")) {
            if (w.length() > 4) w = w.substring(0, w.length() - 1);
        } else if (w.endsWith("ing")) {
            String stem = w.substring(0, w.length() - 3);
            if (hasVowel(stem)) {
                w = stem;
                if (w.endsWith("at") || w.endsWith("bl") || w.endsWith("iz")) w += "e";
                else if (doubleConsonant(w) && !w.endsWith("l") && !w.endsWith("s") && !w.endsWith("z")) {
                    w = w.substring(0, w.length() - 1);
                }
            }
        } else if (w.endsWith("ed")) {
            String stem = w.substring(0, w.length() - 2);
            if (hasVowel(stem)) {
                w = stem;
                if (w.endsWith("at") || w.endsWith("bl") || w.endsWith("iz")) w += "e";
                else if (doubleConsonant(w) && !w.endsWith("l") && !w.endsWith("s") && !w.endsWith("z")) {
                    w = w.substring(0, w.length() - 1);
                }
            }
        }

        // Step 1c: y -> i
        if (w.endsWith("y") && w.length() > 2 && !isVowel(w.charAt(w.length() - 2))) {
            w = w.substring(0, w.length() - 1) + "i";
        }

        // Step 2 & 3: common suffixes
        if (w.endsWith("ational")) w = w.substring(0, w.length() - 7) + "ate";
        else if (w.endsWith("tional")) w = w.substring(0, w.length() - 6) + "tion";
        else if (w.endsWith("alize")) w = w.substring(0, w.length() - 5) + "al";
        else if (w.endsWith("ator")) w = w.substring(0, w.length() - 4) + "ate";
        else if (w.endsWith("ness")) w = w.substring(0, w.length() - 4);
        else if (w.endsWith("ful")) w = w.substring(0, w.length() - 3);
        else if (w.endsWith("ment")) w = w.substring(0, w.length() - 4);

        return w;
    }

    private static boolean isVowel(char c) {
        return "aeiou".indexOf(c) != -1;
    }

    private static boolean hasVowel(String s) {
        for (char c : s.toCharArray()) if (isVowel(c)) return true;
        return false;
    }

    private static boolean doubleConsonant(String s) {
        if (s.length() < 2) return false;
        char c1 = s.charAt(s.length() - 1);
        char c2 = s.charAt(s.length() - 2);
        return c1 == c2 && !isVowel(c1);
    }

    // Morphological Lemmatizer (Base dictionary + grammatical transformation rules)
    private static final Map<String, String> IRREGULAR_LEMMAS = new HashMap<>();
    static {
        IRREGULAR_LEMMAS.put("went", "go");
        IRREGULAR_LEMMAS.put("gone", "go");
        IRREGULAR_LEMMAS.put("going", "go");
        IRREGULAR_LEMMAS.put("goes", "go");
        IRREGULAR_LEMMAS.put("is", "be");
        IRREGULAR_LEMMAS.put("was", "be");
        IRREGULAR_LEMMAS.put("were", "be");
        IRREGULAR_LEMMAS.put("been", "be");
        IRREGULAR_LEMMAS.put("are", "be");
        IRREGULAR_LEMMAS.put("running", "run");
        IRREGULAR_LEMMAS.put("ran", "run");
        IRREGULAR_LEMMAS.put("better", "good");
        IRREGULAR_LEMMAS.put("best", "good");
        IRREGULAR_LEMMAS.put("worse", "bad");
        IRREGULAR_LEMMAS.put("worst", "bad");
        IRREGULAR_LEMMAS.put("mice", "mouse");
        IRREGULAR_LEMMAS.put("geese", "goose");
        IRREGULAR_LEMMAS.put("children", "child");
        IRREGULAR_LEMMAS.put("teeth", "tooth");
        IRREGULAR_LEMMAS.put("feet", "foot");
        IRREGULAR_LEMMAS.put("studied", "study");
        IRREGULAR_LEMMAS.put("studying", "study");
        IRREGULAR_LEMMAS.put("flying", "fly");
        IRREGULAR_LEMMAS.put("flies", "fly");
        IRREGULAR_LEMMAS.put("cars", "car");
        IRREGULAR_LEMMAS.put("computations", "computation");
        IRREGULAR_LEMMAS.put("programmed", "program");
        IRREGULAR_LEMMAS.put("programming", "program");
    }

    public static String lemmatize(String word) {
        String lower = word.toLowerCase();
        if (IRREGULAR_LEMMAS.containsKey(lower)) {
            return IRREGULAR_LEMMAS.get(lower);
        }
        // Regular morphology rules
        if (lower.endsWith("ies") && lower.length() > 4) {
            return lower.substring(0, lower.length() - 3) + "y";
        }
        if (lower.endsWith("es") && (lower.endsWith("shes") || lower.endsWith("ches") || lower.endsWith("xes"))) {
            return lower.substring(0, lower.length() - 2);
        }
        if (lower.endsWith("s") && !lower.endsWith("ss") && lower.length() > 3) {
            return lower.substring(0, lower.length() - 1);
        }
        if (lower.endsWith("ing") && lower.length() > 5) {
            return lower.substring(0, lower.length() - 3);
        }
        if (lower.endsWith("ed") && lower.length() > 4) {
            return lower.substring(0, lower.length() - 2);
        }
        return lower;
    }

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("PRACTICAL 13: STEMMING AND LEMMATIZATION IN JAVA");
        System.out.println("============================================================\n");

        String inputText = "The programmer was studying algorithms while computers were running multiple computations and children played";

        System.out.println("Input Sentence:");
        System.out.println("  \"" + inputText + "\"\n");

        String[] tokens = inputText.replaceAll("[^a-zA-Z\\s]", "").split("\\s+");

        System.out.printf("%-18s %-18s %-18s %-20s\n", "Original Word", "Stemmed (Porter)", "Lemmatized", "Linguistic Difference");
        System.out.println("-------------------------------------------------------------------------------");

        for (String w : tokens) {
            String s = stem(w);
            String l = lemmatize(w);
            String note = "";
            if (!s.equals(l)) {
                note = "Stem != Lemma";
            } else {
                note = "Identical form";
            }
            System.out.printf("%-18s %-18s %-18s %-20s\n", w, s, l, note);
        }
        System.out.println("-------------------------------------------------------------------------------");

        System.out.println("\nSummary Analysis:");
        System.out.println("  1. Stemming applies heuristic suffix-stripping rules (e.g. 'studying' -> 'studi').");
        System.out.println("  2. Lemmatization uses morphological and vocabulary analysis to return the true root/dictionary lemma (e.g. 'studying' -> 'study', 'children' -> 'child').");

        System.out.println("\n[SUCCESS] Practical 13 executed successfully.");
    }
}

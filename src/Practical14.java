/**
 * BCS751 ARTIFICIAL INTELLIGENCE LAB
 * Practical 14: Part of Speech (POS) Tagging on a Sentence in Java.
 */

import java.util.*;

public class Practical14 {

    // Penn Treebank POS Tag definitions and dictionary
    private static final Map<String, String> LEXICON = new HashMap<>();
    private static final Map<String, String> TAG_DESCRIPTIONS = new HashMap<>();

    static {
        // Tag Descriptions
        TAG_DESCRIPTIONS.put("DT", "Determiner");
        TAG_DESCRIPTIONS.put("NN", "Noun, Singular");
        TAG_DESCRIPTIONS.put("NNS", "Noun, Plural");
        TAG_DESCRIPTIONS.put("NNP", "Proper Noun, Singular");
        TAG_DESCRIPTIONS.put("VB", "Verb, Base Form");
        TAG_DESCRIPTIONS.put("VBD", "Verb, Past Tense");
        TAG_DESCRIPTIONS.put("VBG", "Verb, Gerund / Present Participle");
        TAG_DESCRIPTIONS.put("VBZ", "Verb, 3rd Person Singular Present");
        TAG_DESCRIPTIONS.put("VBP", "Verb, Non-3rd Person Singular Present");
        TAG_DESCRIPTIONS.put("JJ", "Adjective");
        TAG_DESCRIPTIONS.put("JJR", "Adjective, Comparative");
        TAG_DESCRIPTIONS.put("JJS", "Adjective, Superlative");
        TAG_DESCRIPTIONS.put("RB", "Adverb");
        TAG_DESCRIPTIONS.put("IN", "Preposition / Subordinating Conjunction");
        TAG_DESCRIPTIONS.put("PRP", "Personal Pronoun");
        TAG_DESCRIPTIONS.put("CC", "Coordinating Conjunction");
        TAG_DESCRIPTIONS.put("CD", "Cardinal Number");

        // Common Word -> Tag mapping
        LEXICON.put("the", "DT");
        LEXICON.put("a", "DT");
        LEXICON.put("an", "DT");
        LEXICON.put("quick", "JJ");
        LEXICON.put("brown", "JJ");
        LEXICON.put("fox", "NN");
        LEXICON.put("jumps", "VBZ");
        LEXICON.put("over", "IN");
        LEXICON.put("lazy", "JJ");
        LEXICON.put("dog", "NN");
        LEXICON.put("artificial", "JJ");
        LEXICON.put("intelligence", "NN");
        LEXICON.put("is", "VBZ");
        LEXICON.put("transforming", "VBG");
        LEXICON.put("modern", "JJ");
        LEXICON.put("technology", "NN");
        LEXICON.put("rapidly", "RB");
        LEXICON.put("and", "CC");
        LEXICON.put("efficiently", "RB");
        LEXICON.put("shivam", "NNP");
        LEXICON.put("in", "IN");
        LEXICON.put("on", "IN");
        LEXICON.put("at", "IN");
        LEXICON.put("with", "IN");
        LEXICON.put("he", "PRP");
        LEXICON.put("they", "PRP");
        LEXICON.put("we", "PRP");
    }

    public static String tagToken(String token, String prevTag) {
        String lower = token.toLowerCase();

        // 1. Lexicon lookup
        if (LEXICON.containsKey(lower)) {
            return LEXICON.get(lower);
        }

        // 2. Capitalization heuristic -> Proper Noun
        if (Character.isUpperCase(token.charAt(0)) && !prevTag.equals("START")) {
            return "NNP";
        }

        // 3. Morphological Suffix Rules
        if (lower.endsWith("ly")) return "RB";               // rapidly -> Adverb
        if (lower.endsWith("ing")) return "VBG";             // solving -> Verb Gerund
        if (lower.endsWith("ed")) return "VBD";              // solved -> Verb Past
        if (lower.endsWith("tion") || lower.endsWith("sion") || lower.endsWith("ment") || lower.endsWith("ness")) return "NN"; // Noun
        if (lower.endsWith("able") || lower.endsWith("ible") || lower.endsWith("al") || lower.endsWith("ic") || lower.endsWith("ous")) return "JJ"; // Adjective
        if (lower.endsWith("s")) return "NNS";               // Plural noun
        if (lower.matches("\\d+")) return "CD";              // Numbers

        // Default heuristic based on previous tag
        if (prevTag.equals("DT") || prevTag.equals("JJ")) return "NN";
        if (prevTag.equals("NN") || prevTag.equals("NNP") || prevTag.equals("PRP")) return "VB";

        return "NN"; // Default fallback
    }

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("PRACTICAL 14: PART OF SPEECH (POS) TAGGING IN JAVA");
        System.out.println("============================================================\n");

        String sentence = "Artificial intelligence is transforming modern technology rapidly and efficiently";

        System.out.println("Input Sentence:");
        System.out.println("  \"" + sentence + "\"\n");

        String[] tokens = sentence.split("\\s+");
        String prevTag = "START";

        System.out.printf("%-6s %-18s %-10s %-35s\n", "Index", "Token", "POS Tag", "Penn Treebank Description");
        System.out.println("-------------------------------------------------------------------------");

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i].replaceAll("[^a-zA-Z0-9]", "");
            String tag = tagToken(token, prevTag);
            prevTag = tag;
            String desc = TAG_DESCRIPTIONS.getOrDefault(tag, "General Word");

            System.out.printf("%-6d %-18s %-10s %-35s\n", (i + 1), token, tag, desc);
        }
        System.out.println("-------------------------------------------------------------------------");

        System.out.println("\n[SUCCESS] Practical 14 executed successfully.");
    }
}

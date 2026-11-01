/**
 * BCS751 ARTIFICIAL INTELLIGENCE LAB
 * Practical 15: Build a Simple Text Classifier (Spam vs. Ham) in Java.
 */

import java.util.*;

public class Practical15 {

    static class LabeledDoc {
        String label; // "spam" or "ham"
        String text;
        LabeledDoc(String label, String text) {
            this.label = label;
            this.text = text;
        }
    }

    static class NaiveBayesClassifier {
        Map<String, Integer> classDocCounts = new HashMap<>();
        Map<String, Map<String, Integer>> wordCounts = new HashMap<>();
        Map<String, Integer> totalWordsPerClass = new HashMap<>();
        Set<String> vocabulary = new HashSet<>();
        int totalDocs = 0;

        public void train(List<LabeledDoc> dataset) {
            for (LabeledDoc doc : dataset) {
                totalDocs++;
                classDocCounts.put(doc.label, classDocCounts.getOrDefault(doc.label, 0) + 1);

                wordCounts.putIfAbsent(doc.label, new HashMap<>());
                totalWordsPerClass.putIfAbsent(doc.label, 0);

                List<String> tokens = tokenize(doc.text);
                for (String t : tokens) {
                    vocabulary.add(t);
                    Map<String, Integer> counts = wordCounts.get(doc.label);
                    counts.put(t, counts.getOrDefault(t, 0) + 1);
                    totalWordsPerClass.put(doc.label, totalWordsPerClass.get(doc.label) + 1);
                }
            }
        }

        public String classify(String text) {
            List<String> tokens = tokenize(text);
            String bestClass = null;
            double maxLogProb = Double.NEGATIVE_INFINITY;

            int vocabSize = vocabulary.size();

            for (String c : classDocCounts.keySet()) {
                // Prior probability P(Class)
                double logProb = Math.log((double) classDocCounts.get(c) / totalDocs);

                int totalWordsInClass = totalWordsPerClass.getOrDefault(c, 0);
                Map<String, Integer> counts = wordCounts.get(c);

                // Likelihood product using log sum: log P(w|Class) with Laplace Add-1 smoothing
                for (String w : tokens) {
                    if (vocabulary.contains(w)) {
                        int wordCount = counts.getOrDefault(w, 0);
                        double pWordGivenClass = (double) (wordCount + 1) / (totalWordsInClass + vocabSize);
                        logProb += Math.log(pWordGivenClass);
                    }
                }

                if (logProb > maxLogProb) {
                    maxLogProb = logProb;
                    bestClass = c;
                }
            }
            return bestClass;
        }

        private List<String> tokenize(String text) {
            String[] raw = text.toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", " ").split("\\s+");
            List<String> tokens = new ArrayList<>();
            for (String r : raw) {
                if (!r.isEmpty()) tokens.add(r);
            }
            return tokens;
        }
    }

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("PRACTICAL 15: NAIVE BAYES SPAM/HAM TEXT CLASSIFIER IN JAVA");
        System.out.println("============================================================\n");

        // Curated Training Corpus
        List<LabeledDoc> trainingData = Arrays.asList(
            // Spam Messages
            new LabeledDoc("SPAM", "Congratulations! You have won a 1000 cash prize claim now"),
            new LabeledDoc("SPAM", "URGENT! Free credit loan approved call this number immediately"),
            new LabeledDoc("SPAM", "Win big lottery cash reward click here to claim award"),
            new LabeledDoc("SPAM", "Special offer free tickets subscribe now to win cash"),
            new LabeledDoc("SPAM", "Exclusive discount cheap luxury watches buy now"),
            
            // Ham (Legitimate) Messages
            new LabeledDoc("HAM", "Hey Shivam are we meeting for the project discussion today"),
            new LabeledDoc("HAM", "Please review the practical assignment pdf and submit tomorrow"),
            new LabeledDoc("HAM", "Can you send the lecture notes for artificial intelligence lab"),
            new LabeledDoc("HAM", "Let us have lunch together at the campus cafeteria"),
            new LabeledDoc("HAM", "The meeting has been rescheduled to tomorrow morning at 10 AM")
        );

        System.out.println("--- Training Naive Bayes Classifier on " + trainingData.size() + " messages ---");
        NaiveBayesClassifier classifier = new NaiveBayesClassifier();
        classifier.train(trainingData);

        System.out.println("Vocabulary Size (Unique Words): " + classifier.vocabulary.size());
        System.out.println("Classes Trained: " + classifier.classDocCounts.keySet());
        System.out.println("Class Prior Counts: " + classifier.classDocCounts);

        // Evaluation on Test Messages
        List<String> testMessages = Arrays.asList(
            "Claim your free lottery cash prize right now call urgent",
            "Hey are you coming to the college library for studying AI",
            "Win exclusive free discount coupon on luxury items",
            "Please check the email attachment for the practical file"
        );

        System.out.println("\n--- Classification Results on Unseen Test Messages ---");
        System.out.printf("%-6s %-62s %-10s\n", "No.", "Message Content", "Prediction");
        System.out.println("----------------------------------------------------------------------------------");

        for (int i = 0; i < testMessages.size(); i++) {
            String msg = testMessages.get(i);
            String prediction = classifier.classify(msg);
            System.out.printf("%-6d %-62s %-10s\n", (i + 1), "\"" + msg + "\"", "[" + prediction + "]");
        }
        System.out.println("----------------------------------------------------------------------------------");

        System.out.println("\n[SUCCESS] Practical 15 executed successfully.");
    }
}

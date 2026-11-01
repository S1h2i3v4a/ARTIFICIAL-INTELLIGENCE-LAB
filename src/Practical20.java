/**
 * BCS751 ARTIFICIAL INTELLIGENCE LAB
 * Practical 20: Build a simple chatbot using rules and regex intent matching in Java.
 */

import java.util.*;
import java.util.regex.*;

public class Practical20 {

    static class IntentRule {
        String intentName;
        Pattern pattern;
        List<String> responses;

        IntentRule(String intentName, String regexPattern, List<String> responses) {
            this.intentName = intentName;
            this.pattern = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);
            this.responses = responses;
        }

        public boolean matches(String userInput) {
            return pattern.matcher(userInput).find();
        }

        public String getResponse(Random rng) {
            return responses.get(rng.nextInt(responses.size()));
        }
    }

    static class AIChatbot {
        List<IntentRule> rules = new ArrayList<>();
        Random rng = new Random(42);

        public AIChatbot() {
            // 1. Greetings
            rules.add(new IntentRule("GREETING",
                "\\b(hello|hi|hey|greetings|good morning|good afternoon)\\b",
                Arrays.asList(
                    "Hello! I am your AI Lab Assistant. How can I help you today?",
                    "Hi there! Welcome to the Artificial Intelligence Practical Lab assistant. What topic are you exploring?"
                )
            ));

            // 2. Syllabus & Lab Info
            rules.add(new IntentRule("LAB_INFO",
                "\\b(syllabus|lab|bcs751|assignment|practicals|course)\\b",
                Arrays.asList(
                    "This is BCS751: Artificial Intelligence Lab covering 22 practicals including Search, Logic, NLP, Games, and Bayesian reasoning.",
                    "The AI Lab covers foundational algorithms from BFS/DFS and A* to Minimax, Prolog Logic, and NLP Text Classifiers."
                )
            ));

            // 3. Search Algorithms
            rules.add(new IntentRule("SEARCH_ALGORITHMS",
                "\\b(bfs|dfs|a\\*|a star|heuristic|8 puzzle|hill climbing|simulated annealing)\\b",
                Arrays.asList(
                    "Search algorithms explore the state space. BFS guarantees shortest path, DFS goes deep, while A* uses f(n)=g(n)+h(n) for optimal search.",
                    "Informed search algorithms like A* and Hill Climbing use heuristic evaluation functions to guide problem solving efficiently."
                )
            ));

            // 4. NLP Concepts
            rules.add(new IntentRule("NLP",
                "\\b(nlp|nltk|token|stemming|lemmatization|pos|tagging|spam)\\b",
                Arrays.asList(
                    "Natural Language Processing includes tokenization, stop-word removal, stemming/lemmatization, POS tagging, and Naive Bayes text classification.",
                    "Text preprocessing cleans raw text for machine learning models, transforming sentences into vocabulary tokens."
                )
            ));

            // 5. Game Playing & Minimax
            rules.add(new IntentRule("GAME_PLAYING",
                "\\b(minimax|alpha beta|tic tac toe|game|pruning)\\b",
                Arrays.asList(
                    "Minimax evaluates all game-tree branches to choose the optimal move. Alpha-Beta pruning cuts unpromising branches to speed up search.",
                    "In turn-based games, the Maximizer aims for highest utility while Minimizer aims for lowest. Alpha-Beta maintains identical decisions with fewer evaluations."
                )
            ));

            // 6. Help
            rules.add(new IntentRule("HELP",
                "\\b(help|assist|support|what can you do)\\b",
                Arrays.asList(
                    "You can ask me about AI Lab practicals, BFS/DFS, A* 8-puzzle, Minimax, NLP, or Prolog logic!",
                    "I can guide you through any topic in your AI syllabus. Try asking about 'search algorithms' or 'NLP'."
                )
            ));

            // 7. Farewells
            rules.add(new IntentRule("FAREWELL",
                "\\b(bye|goodbye|exit|quit|see you|thanks|thank you)\\b",
                Arrays.asList(
                    "Goodbye! Best of luck with your Artificial Intelligence practical submission!",
                    "You are welcome! Have a great time learning Artificial Intelligence. Bye!"
                )
            ));
        }

        public String respond(String userMessage) {
            for (IntentRule rule : rules) {
                if (rule.matches(userMessage)) {
                    return "[" + rule.intentName + "] " + rule.getResponse(rng);
                }
            }
            return "[FALLBACK] I am an AI Lab Assistant. Could you please rephrase or ask about search, logic, NLP, or game theory?";
        }
    }

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("PRACTICAL 20: RULE-BASED & INTENT-MATCHING CHATBOT IN JAVA");
        System.out.println("============================================================\n");

        AIChatbot bot = new AIChatbot();

        // Dialogue script showcasing automated conversation testing
        List<String> conversationScript = Arrays.asList(
            "Hello there, good morning!",
            "Can you tell me about the BCS751 lab syllabus?",
            "How does the A* search algorithm solve the 8 puzzle problem?",
            "What is the difference between stemming and lemmatization in NLP?",
            "Why is alpha beta pruning used in minimax game search?",
            "What can you do to help me?",
            "Thank you so much, goodbye!"
        );

        System.out.println("--- Interactive Multi-turn Dialogue Transcript ---\n");

        for (int i = 0; i < conversationScript.size(); i++) {
            String userQuery = conversationScript.get(i);
            System.out.println("User:   \"" + userQuery + "\"");
            String botReply = bot.respond(userQuery);
            System.out.println("Bot:    " + botReply + "\n");
        }

        System.out.println("[SUCCESS] Practical 20 executed successfully.");
    }
}

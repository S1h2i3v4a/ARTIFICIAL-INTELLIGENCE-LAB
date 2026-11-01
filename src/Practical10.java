/**
 * BCS751 ARTIFICIAL INTELLIGENCE LAB
 * Practical 10: Implement Forward and Backward Chaining in a rule-based system.
 */

import java.util.*;

public class Practical10 {

    static class Rule {
        String name;
        Set<String> premises;
        String conclusion;

        Rule(String name, List<String> premises, String conclusion) {
            this.name = name;
            this.premises = new LinkedHashSet<>(premises);
            this.conclusion = conclusion;
        }

        @Override
        public String toString() {
            return name + ": IF (" + String.join(" AND ", premises) + ") THEN " + conclusion;
        }
    }

    // Forward Chaining (Data-driven)
    public static boolean forwardChaining(List<Rule> rules, Set<String> facts, String goal, List<String> trace) {
        Set<String> knownFacts = new LinkedHashSet<>(facts);
        boolean changed = true;

        while (changed) {
            changed = false;
            if (knownFacts.contains(goal)) {
                return true;
            }

            for (Rule r : rules) {
                if (!knownFacts.contains(r.conclusion) && knownFacts.containsAll(r.premises)) {
                    knownFacts.add(r.conclusion);
                    trace.add("Applied " + r.name + " -> Facts " + r.premises + " satisfy condition => Inferred: '" + r.conclusion + "'");
                    changed = true;
                    if (r.conclusion.equals(goal)) {
                        return true;
                    }
                }
            }
        }
        return knownFacts.contains(goal);
    }

    // Backward Chaining (Goal-driven)
    public static boolean backwardChaining(List<Rule> rules, Set<String> initialFacts, String goal, Set<String> visited, List<String> trace, int depth) {
        String indent = "  ".repeat(depth);
        if (initialFacts.contains(goal)) {
            trace.add(indent + "[FACT] '" + goal + "' is directly verified from initial facts.");
            return true;
        }

        if (visited.contains(goal)) {
            trace.add(indent + "[CYCLE] '" + goal + "' is already in active verification stack.");
            return false;
        }

        visited.add(goal);
        trace.add(indent + "[SUB-GOAL] Attempting to prove: '" + goal + "'");

        // Find candidate rules concluding this goal
        List<Rule> candidates = new ArrayList<>();
        for (Rule r : rules) {
            if (r.conclusion.equals(goal)) candidates.add(r);
        }

        if (candidates.isEmpty()) {
            trace.add(indent + "[FAIL] No inference rules conclude '" + goal + "'");
            visited.remove(goal);
            return false;
        }

        for (Rule r : candidates) {
            trace.add(indent + "Evaluating rule " + r.name + " with premises: " + r.premises);
            boolean allPremisesProved = true;

            for (String prem : r.premises) {
                if (!backwardChaining(rules, initialFacts, prem, visited, trace, depth + 1)) {
                    allPremisesProved = false;
                    break;
                }
            }

            if (allPremisesProved) {
                trace.add(indent + "[SUCCESS] Sub-goal '" + goal + "' proved via " + r.name + "!");
                visited.remove(goal);
                return true;
            }
        }

        visited.remove(goal);
        return false;
    }

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("PRACTICAL 10: FORWARD AND BACKWARD CHAINING IN JAVA");
        System.out.println("============================================================\n");

        // Medical Diagnostic Knowledge Base
        List<Rule> rules = Arrays.asList(
            new Rule("Rule_1", Arrays.asList("sneezing", "runny_nose"), "nasal_congestion"),
            new Rule("Rule_2", Arrays.asList("nasal_congestion", "fever"), "infection"),
            new Rule("Rule_3", Arrays.asList("infection", "body_ache"), "influenza_flu"),
            new Rule("Rule_4", Arrays.asList("nasal_congestion", "itchy_eyes"), "allergy"),
            new Rule("Rule_5", Arrays.asList("nasal_congestion", "cough"), "common_cold")
        );

        Set<String> initialFacts = new LinkedHashSet<>(Arrays.asList("sneezing", "runny_nose", "fever", "body_ache"));
        String goal = "influenza_flu";

        System.out.println("--- Rule Base (Knowledge Base) ---");
        for (Rule r : rules) System.out.println("  " + r);

        System.out.println("\nInitial Observed Facts: " + initialFacts);
        System.out.println("Target Hypothesis (Goal): '" + goal + "'");

        // 1. Forward Chaining
        System.out.println("\n" + "=".repeat(25) + " FORWARD CHAINING " + "=".repeat(25));
        List<String> fcTrace = new ArrayList<>();
        boolean fcResult = forwardChaining(rules, initialFacts, goal, fcTrace);
        System.out.println("Forward Chaining Proved Goal: " + fcResult);
        System.out.println("Derivation Steps:");
        for (int i = 0; i < fcTrace.size(); i++) {
            System.out.println("  Step " + (i + 1) + ": " + fcTrace.get(i));
        }

        // 2. Backward Chaining
        System.out.println("\n" + "=".repeat(25) + " BACKWARD CHAINING " + "=".repeat(25));
        List<String> bcTrace = new ArrayList<>();
        boolean bcResult = backwardChaining(rules, initialFacts, goal, new HashSet<>(), bcTrace, 0);
        System.out.println("Backward Chaining Proved Goal: " + bcResult);
        System.out.println("Goal-Driven Proof Tree:");
        for (String step : bcTrace) {
            System.out.println(step);
        }

        System.out.println("\n[SUCCESS] Practical 10 executed successfully.");
    }
}

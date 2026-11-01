/**
 * BCS751 ARTIFICIAL INTELLIGENCE LAB
 * Practical 11: Demonstrate Resolution in Propositional Logic through a basic example.
 */

import java.util.*;

public class Practical11 {

    // Helper: Returns complement of a literal (e.g. "P" -> "~P", "~P" -> "P")
    public static String getComplement(String lit) {
        return lit.startsWith("~") ? lit.substring(1) : "~" + lit;
    }

    // Resolves two clauses (sets of literals)
    public static Set<Set<String>> resolve(Set<String> c1, Set<String> c2) {
        Set<Set<String>> resolvents = new HashSet<>();
        for (String lit : c1) {
            String comp = getComplement(lit);
            if (c2.contains(comp)) {
                Set<String> resolvent = new TreeSet<>(c1);
                resolvent.remove(lit);
                for (String l2 : c2) {
                    if (!l2.equals(comp)) {
                        resolvent.add(l2);
                    }
                }
                resolvents.add(resolvent);
            }
        }
        return resolvents;
    }

    public static String clauseToString(Set<String> clause) {
        if (clause.isEmpty()) return "[] (EMPTY CLAUSE / CONTRADICTION)";
        return "(" + String.join(" v ", clause) + ")";
    }

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("PRACTICAL 11: PROPOSITIONAL LOGIC RESOLUTION REFUTATION");
        System.out.println("============================================================\n");

        // Theorem to Prove:
        // Premise 1: If it rains, the ground is wet (R -> W)  => (~R v W)
        // Premise 2: It is raining (R)
        // Goal Query: The ground is wet (W)
        // Refutation: Add negated query (~W) to KB and derive contradiction []

        System.out.println("Theorem to Prove:");
        System.out.println("  Premise 1: Rain -> WetGround  [CNF: ~R v W]");
        System.out.println("  Premise 2: Rain               [CNF: R]");
        System.out.println("  Query Goal: WetGround (W)");
        System.out.println("  Refutation Strategy: Add Negated Query (~W) to KB and resolve towards []\n");

        List<Set<String>> clauseList = new ArrayList<>();
        clauseList.add(new TreeSet<>(Arrays.asList("~R", "W"))); // Clause 1
        clauseList.add(new TreeSet<>(Arrays.asList("R")));       // Clause 2
        clauseList.add(new TreeSet<>(Arrays.asList("~W")));      // Clause 3 (Negated Query)

        Map<Set<String>, Integer> clauseIndex = new HashMap<>();
        System.out.println("Initial Clause Set in CNF:");
        for (int i = 0; i < clauseList.size(); i++) {
            clauseIndex.put(clauseList.get(i), i + 1);
            System.out.println("  (" + (i + 1) + ") " + clauseToString(clauseList.get(i)));
        }

        System.out.println("\n--- Step-by-Step Resolution Inferences ---");

        boolean contradictionDerived = false;
        int nextIndex = clauseList.size() + 1;

        outer:
        while (true) {
            int currentSize = clauseList.size();
            List<Set<String>> newClauses = new ArrayList<>();

            for (int i = 0; i < currentSize; i++) {
                for (int j = i + 1; j < currentSize; j++) {
                    Set<String> ci = clauseList.get(i);
                    Set<String> cj = clauseList.get(j);

                    Set<Set<String>> resolvents = resolve(ci, cj);
                    for (Set<String> res : resolvents) {
                        if (res.isEmpty()) {
                            // Contradiction reached!
                            System.out.printf("  (%d) Resolved (%d) and (%d) ===> %s\n",
                                nextIndex, clauseIndex.get(ci), clauseIndex.get(cj), clauseToString(res));
                            contradictionDerived = true;
                            break outer;
                        }

                        if (!clauseIndex.containsKey(res)) {
                            clauseIndex.put(res, nextIndex);
                            System.out.printf("  (%d) Resolved (%d) and (%d) ===> %s\n",
                                nextIndex, clauseIndex.get(ci), clauseIndex.get(cj), clauseToString(res));
                            newClauses.add(res);
                            nextIndex++;
                        }
                    }
                }
            }

            if (newClauses.isEmpty()) {
                break; // No new inferences possible
            }
            clauseList.addAll(newClauses);
        }

        System.out.println("\n--- Verification Outcome ---");
        if (contradictionDerived) {
            System.out.println("Empty Clause [] (Contradiction) successfully derived!");
            System.out.println("Result: The assumption ~Query leads to inconsistency with Knowledge Base.");
            System.out.println("Therefore, Query 'W' (WetGround) is PROVED VALID & ENTAILED by Propositional Resolution.");
        } else {
            System.out.println("No contradiction could be derived. Query is not entailed.");
        }

        System.out.println("\n[SUCCESS] Practical 11 executed successfully.");
    }
}

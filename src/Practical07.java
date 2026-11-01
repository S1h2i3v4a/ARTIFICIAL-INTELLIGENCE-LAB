/**
 * BCS751 ARTIFICIAL INTELLIGENCE LAB
 * Practical 07: Define family relationships using predicates (Prolog Logic Engine in Java).
 */

import java.util.*;

public class Practical07 {

    static class FamilyKnowledgeBase {
        // Facts
        Set<String> males = new TreeSet<>(Arrays.asList("john", "bob", "mike", "david", "george"));
        Set<String> females = new TreeSet<>(Arrays.asList("mary", "lisa", "susan", "anna", "emma"));

        // parent(Parent, Child)
        List<Map.Entry<String, String>> parentFacts = new ArrayList<>();

        public FamilyKnowledgeBase() {
            addParent("john", "bob");
            addParent("mary", "bob");
            addParent("john", "lisa");
            addParent("mary", "lisa");
            addParent("bob", "mike");
            addParent("susan", "mike");
            addParent("bob", "anna");
            addParent("susan", "anna");
            addParent("david", "susan");
            addParent("emma", "susan");
        }

        private void addParent(String p, String c) {
            parentFacts.add(new AbstractMap.SimpleEntry<>(p, c));
        }

        // Predicates & Rules
        public boolean isMale(String p) { return males.contains(p); }
        public boolean isFemale(String p) { return females.contains(p); }

        public List<String> getParents(String child) {
            List<String> list = new ArrayList<>();
            for (Map.Entry<String, String> e : parentFacts) {
                if (e.getValue().equals(child)) list.add(e.getKey());
            }
            return list;
        }

        public List<String> getChildren(String parent) {
            List<String> list = new ArrayList<>();
            for (Map.Entry<String, String> e : parentFacts) {
                if (e.getKey().equals(parent)) list.add(e.getValue());
            }
            return list;
        }

        // father(X, Y) :- male(X), parent(X, Y).
        public List<String> getFather(String child) {
            List<String> res = new ArrayList<>();
            for (String p : getParents(child)) {
                if (isMale(p)) res.add(p);
            }
            return res;
        }

        // mother(X, Y) :- female(X), parent(X, Y).
        public List<String> getMother(String child) {
            List<String> res = new ArrayList<>();
            for (String p : getParents(child)) {
                if (isFemale(p)) res.add(p);
            }
            return res;
        }

        // sibling(X, Y) :- parent(Z, X), parent(Z, Y), X \= Y.
        public List<String> getSiblings(String person) {
            Set<String> sibs = new TreeSet<>();
            for (String p : getParents(person)) {
                for (String c : getChildren(p)) {
                    if (!c.equals(person)) sibs.add(c);
                }
            }
            return new ArrayList<>(sibs);
        }

        public List<String> getBrothers(String person) {
            List<String> res = new ArrayList<>();
            for (String s : getSiblings(person)) {
                if (isMale(s)) res.add(s);
            }
            return res;
        }

        public List<String> getSisters(String person) {
            List<String> res = new ArrayList<>();
            for (String s : getSiblings(person)) {
                if (isFemale(s)) res.add(s);
            }
            return res;
        }

        // grandparent(X, Z) :- parent(X, Y), parent(Y, Z).
        public List<String> getGrandparents(String person) {
            Set<String> gps = new TreeSet<>();
            for (String p : getParents(person)) {
                gps.addAll(getParents(p));
            }
            return new ArrayList<>(gps);
        }

        public List<String> getGrandfathers(String person) {
            List<String> res = new ArrayList<>();
            for (String gp : getGrandparents(person)) {
                if (isMale(gp)) res.add(gp);
            }
            return res;
        }

        public List<String> getGrandmothers(String person) {
            List<String> res = new ArrayList<>();
            for (String gp : getGrandparents(person)) {
                if (isFemale(gp)) res.add(gp);
            }
            return res;
        }

        // ancestor(X, Y) :- parent(X, Y).
        // ancestor(X, Y) :- parent(X, Z), ancestor(Z, Y).
        public List<String> getAncestors(String person) {
            Set<String> ancs = new TreeSet<>();
            collectAncestors(person, ancs);
            return new ArrayList<>(ancs);
        }

        private void collectAncestors(String curr, Set<String> ancs) {
            for (String p : getParents(curr)) {
                ancs.add(p);
                collectAncestors(p, ancs);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("PRACTICAL 07: PROLOG FAMILY RELATIONSHIPS LOGIC IN JAVA");
        System.out.println("============================================================\n");

        FamilyKnowledgeBase kb = new FamilyKnowledgeBase();

        System.out.println("--- Knowledge Base Predicates & Facts ---");
        System.out.println("Males:   " + kb.males);
        System.out.println("Females: " + kb.females);
        System.out.println("\nParent Predicates (parent(Parent, Child)):");
        for (Map.Entry<String, String> e : kb.parentFacts) {
            System.out.printf("  parent(%s, %s).\n", e.getKey(), e.getValue());
        }

        System.out.println("\n--- Querying Inferred Relationships ---");
        System.out.printf("?- %-28s => %s\n", "father(X, mike)", kb.getFather("mike"));
        System.out.printf("?- %-28s => %s\n", "mother(X, mike)", kb.getMother("mike"));
        System.out.printf("?- %-28s => %s\n", "siblings(X, bob)", kb.getSiblings("bob"));
        System.out.printf("?- %-28s => %s\n", "sister(X, bob)", kb.getSisters("bob"));
        System.out.printf("?- %-28s => %s\n", "brother(X, lisa)", kb.getBrothers("lisa"));
        System.out.printf("?- %-28s => %s\n", "grandparent(X, mike)", kb.getGrandparents("mike"));
        System.out.printf("?- %-28s => %s\n", "grandfather(X, mike)", kb.getGrandfathers("mike"));
        System.out.printf("?- %-28s => %s\n", "grandmother(X, mike)", kb.getGrandmothers("mike"));
        System.out.printf("?- %-28s => %s\n", "ancestor(X, mike)", kb.getAncestors("mike"));
        System.out.printf("?- %-28s => %s\n", "ancestor(X, anna)", kb.getAncestors("anna"));

        System.out.println("\n[SUCCESS] Practical 07 executed successfully.");
    }
}

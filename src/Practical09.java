/**
 * BCS751 ARTIFICIAL INTELLIGENCE LAB
 * Practical 09: Implement Unification Algorithm in Java (First-Order Logic).
 */

import java.util.*;

public class Practical09 {

    // Representation of a Term: Constant, Variable, or Compound (Function/Predicate)
    static class Term {
        enum Type { CONSTANT, VARIABLE, COMPOUND }
        Type type;
        String name;
        List<Term> args;

        // Constant or Variable
        Term(String name, boolean isVariable) {
            this.name = name;
            this.type = isVariable ? Type.VARIABLE : Type.CONSTANT;
            this.args = Collections.emptyList();
        }

        // Compound term: e.g. P(x, y), Mother(john)
        Term(String name, List<Term> args) {
            this.name = name;
            this.type = Type.COMPOUND;
            this.args = args;
        }

        public boolean isVar() { return type == Type.VARIABLE; }
        public boolean isConst() { return type == Type.CONSTANT; }
        public boolean isCompound() { return type == Type.COMPOUND; }

        @Override
        public String toString() {
            if (isCompound()) {
                StringBuilder sb = new StringBuilder();
                sb.append(name).append("(");
                for (int i = 0; i < args.size(); i++) {
                    sb.append(args.get(i).toString());
                    if (i < args.size() - 1) sb.append(", ");
                }
                sb.append(")");
                return sb.toString();
            }
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Term)) return false;
            Term t = (Term) o;
            return type == t.type && name.equals(t.name) && Objects.equals(args, t.args);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, name, args);
        }
    }

    // Occurs Check: Check if variable 'var' occurs inside expression 'x'
    public static boolean occursCheck(String varName, Term x, Map<String, Term> theta) {
        if (x.isVar() && x.name.equals(varName)) {
            return true;
        }
        if (x.isVar() && theta.containsKey(x.name)) {
            return occursCheck(varName, theta.get(x.name), theta);
        }
        if (x.isCompound()) {
            for (Term arg : x.args) {
                if (occursCheck(varName, arg, theta)) return true;
            }
        }
        return false;
    }

    public static Map<String, Term> unifyVar(String varName, Term x, Map<String, Term> theta) {
        if (theta.containsKey(varName)) {
            return unify(theta.get(varName), x, theta);
        }
        if (x.isVar() && theta.containsKey(x.name)) {
            return unify(new Term(varName, true), theta.get(x.name), theta);
        }
        if (occursCheck(varName, x, theta)) {
            return null; // Failure: cyclic reference
        }
        Map<String, Term> newTheta = new LinkedHashMap<>(theta);
        newTheta.put(varName, x);
        return newTheta;
    }

    public static Map<String, Term> unify(Term x, Term y, Map<String, Term> theta) {
        if (theta == null) return null;
        if (x.equals(y)) return theta;
        if (x.isVar()) return unifyVar(x.name, y, theta);
        if (y.isVar()) return unifyVar(y.name, x, theta);

        if (x.isCompound() && y.isCompound()) {
            // Functors must match and arity must match
            if (!x.name.equals(y.name) || x.args.size() != y.args.size()) {
                return null;
            }
            for (int i = 0; i < x.args.size(); i++) {
                theta = unify(x.args.get(i), y.args.get(i), theta);
                if (theta == null) return null;
            }
            return theta;
        }

        return null; // Mismatch between constants or different structures
    }

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("PRACTICAL 09: ROBINSON'S UNIFICATION ALGORITHM IN JAVA");
        System.out.println("============================================================\n");

        // Helper factories
        // Variables (lowercase): x, y, z
        Term x = new Term("x", true);
        Term y = new Term("y", true);
        // Constants (capitalized): A, B, Sam, Pizza
        Term A = new Term("A", false);
        Term B = new Term("B", false);
        Term sam = new Term("Sam", false);
        Term pizza = new Term("Pizza", false);

        List<Map.Entry<Term, Term>> testCases = new ArrayList<>();

        // Case 1: P(x, B) and P(A, y)
        testCases.add(new AbstractMap.SimpleEntry<>(
            new Term("P", Arrays.asList(x, B)),
            new Term("P", Arrays.asList(A, y))
        ));

        // Case 2: Knows(John, x) and Knows(y, Mother(y))
        testCases.add(new AbstractMap.SimpleEntry<>(
            new Term("Knows", Arrays.asList(new Term("John", false), x)),
            new Term("Knows", Arrays.asList(y, new Term("Mother", Arrays.asList(y))))
        ));

        // Case 3: Likes(x, Food(y)) and Likes(Sam, Food(Pizza))
        testCases.add(new AbstractMap.SimpleEntry<>(
            new Term("Likes", Arrays.asList(x, new Term("Food", Arrays.asList(y)))),
            new Term("Likes", Arrays.asList(sam, new Term("Food", Arrays.asList(pizza))))
        ));

        // Case 4: Predicate mismatch: P(x, y) and Q(A, B)
        testCases.add(new AbstractMap.SimpleEntry<>(
            new Term("P", Arrays.asList(x, y)),
            new Term("Q", Arrays.asList(A, B))
        ));

        // Case 5: Occurs-Check failure: P(x) and P(f(x))
        testCases.add(new AbstractMap.SimpleEntry<>(
            new Term("P", Arrays.asList(x)),
            new Term("P", Arrays.asList(new Term("f", Arrays.asList(x))))
        ));

        for (int i = 0; i < testCases.size(); i++) {
            Term t1 = testCases.get(i).getKey();
            Term t2 = testCases.get(i).getValue();

            Map<String, Term> mgu = unify(t1, t2, new LinkedHashMap<>());

            System.out.println("Test Case " + (i + 1) + ":");
            System.out.println("  Term 1: " + t1);
            System.out.println("  Term 2: " + t2);
            if (mgu != null) {
                System.out.println("  Result: UNIFIABLE");
                System.out.println("  MGU Substitution (Theta): " + mgu);
            } else {
                System.out.println("  Result: FAILED TO UNIFY (No valid unifier)");
            }
            System.out.println();
        }

        System.out.println("[SUCCESS] Practical 09 executed successfully.");
    }
}

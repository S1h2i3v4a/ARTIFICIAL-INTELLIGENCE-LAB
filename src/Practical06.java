/**
 * BCS751 ARTIFICIAL INTELLIGENCE LAB
 * Practical 06: Solve Water Jug Problem using state-space search (BFS or DFS).
 */

import java.util.*;

public class Practical06 {

    static class State {
        int x, y; // Water in Jug 1 and Jug 2
        String action;
        State parent;

        State(int x, int y, String action, State parent) {
            this.x = x;
            this.y = y;
            this.action = action;
            this.parent = parent;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof State)) return false;
            State s = (State) o;
            return x == s.x && y == s.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    public static List<State> solveWaterJugBFS(int capX, int capY, int target) {
        Queue<State> queue = new LinkedList<>();
        Set<State> visited = new HashSet<>();

        State initial = new State(0, 0, "Initial State", null);
        queue.add(initial);
        visited.add(initial);

        State goalState = null;

        while (!queue.isEmpty()) {
            State curr = queue.poll();

            // Check if goal condition met
            if (curr.x == target || curr.y == target) {
                goalState = curr;
                break;
            }

            int x = curr.x;
            int y = curr.y;

            // Generate 6 valid transition states:
            List<State> successors = new ArrayList<>();

            // 1. Fill Jug 1
            successors.add(new State(capX, y, "Fill Jug 1 (" + capX + " gal)", curr));
            // 2. Fill Jug 2
            successors.add(new State(x, capY, "Fill Jug 2 (" + capY + " gal)", curr));
            // 3. Empty Jug 1
            successors.add(new State(0, y, "Empty Jug 1", curr));
            // 4. Empty Jug 2
            successors.add(new State(x, 0, "Empty Jug 2", curr));
            // 5. Pour Jug 1 into Jug 2 until full or Jug 1 is empty
            int pour1to2 = Math.min(x, capY - y);
            successors.add(new State(x - pour1to2, y + pour1to2, "Pour Jug 1 -> Jug 2", curr));
            // 6. Pour Jug 2 into Jug 1 until full or Jug 2 is empty
            int pour2to1 = Math.min(y, capX - x);
            successors.add(new State(x + pour2to1, y - pour2to1, "Pour Jug 2 -> Jug 1", curr));

            for (State next : successors) {
                if (!visited.contains(next)) {
                    visited.add(next);
                    queue.add(next);
                }
            }
        }

        if (goalState != null) {
            List<State> path = new ArrayList<>();
            State curr = goalState;
            while (curr != null) {
                path.add(curr);
                curr = curr.parent;
            }
            Collections.reverse(path);
            return path;
        }
        return Collections.emptyList();
    }

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("PRACTICAL 06: WATER JUG PROBLEM STATE-SPACE SEARCH (BFS)");
        System.out.println("============================================================\n");

        int capX = 4;
        int capY = 3;
        int target = 2;

        System.out.println("Problem Specifications:");
        System.out.println("  Jug 1 Capacity: " + capX + " Gallons");
        System.out.println("  Jug 2 Capacity: " + capY + " Gallons");
        System.out.println("  Target Amount:  " + target + " Gallons");

        System.out.println("\nExecuting BFS State-Space Exploration...");
        List<State> solution = solveWaterJugBFS(capX, capY, target);

        if (!solution.isEmpty()) {
            System.out.println("\nGoal Reached in " + (solution.size() - 1) + " Operations!\n");
            System.out.printf("%-6s %-28s %-14s %-14s\n", "Step", "Rule / Operation", "Jug 1 (4G)", "Jug 2 (3G)");
            System.out.println("--------------------------------------------------------------");

            for (int i = 0; i < solution.size(); i++) {
                State s = solution.get(i);
                String goalMarker = (s.x == target || s.y == target) && i > 0 ? " <-- TARGET REACHED" : "";
                System.out.printf("%-6d %-28s %-14d %-14d%s\n", i, s.action, s.x, s.y, goalMarker);
            }
            System.out.println("--------------------------------------------------------------");
        } else {
            System.out.println("No solution exists for the given jug capacities and target.");
        }

        System.out.println("[SUCCESS] Practical 06 executed successfully.");
    }
}

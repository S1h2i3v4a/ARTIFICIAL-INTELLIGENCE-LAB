/**
 * BCS751 ARTIFICIAL INTELLIGENCE LAB
 * Practical 03: Solve the 8-Puzzle Problem using A* Search Algorithm.
 */

import java.util.*;

public class Practical03 {

    static class Node implements Comparable<Node> {
        int[] state;
        Node parent;
        String action;
        int g; // Path cost from start
        int h; // Heuristic cost to goal
        int f; // Total evaluation function f = g + h

        Node(int[] state, Node parent, String action, int g, int h) {
            this.state = state.clone();
            this.parent = parent;
            this.action = action;
            this.g = g;
            this.h = h;
            this.f = g + h;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.f, other.f);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;
            return Arrays.equals(this.state, ((Node) o).state);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(state);
        }
    }

    // Manhattan distance heuristic
    public static int manhattanDistance(int[] state, int[] goal) {
        int dist = 0;
        int[] goalRow = new int[9];
        int[] goalCol = new int[9];

        for (int i = 0; i < 9; i++) {
            goalRow[goal[i]] = i / 3;
            goalCol[goal[i]] = i % 3;
        }

        for (int i = 0; i < 9; i++) {
            int val = state[i];
            if (val != 0) {
                int r = i / 3;
                int c = i % 3;
                dist += Math.abs(r - goalRow[val]) + Math.abs(c - goalCol[val]);
            }
        }
        return dist;
    }

    // Generate successor nodes
    public static List<Node> getNeighbors(Node current, int[] goal) {
        List<Node> neighbors = new ArrayList<>();
        int blankIdx = -1;
        for (int i = 0; i < 9; i++) {
            if (current.state[i] == 0) {
                blankIdx = i;
                break;
            }
        }

        int r = blankIdx / 3;
        int c = blankIdx % 3;

        int[][] moves = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        String[] actions = {"UP", "DOWN", "LEFT", "RIGHT"};

        for (int m = 0; m < 4; m++) {
            int nr = r + moves[m][0];
            int nc = c + moves[m][1];
            if (nr >= 0 && nr < 3 && nc >= 0 && nc < 3) {
                int newBlank = nr * 3 + nc;
                int[] newState = current.state.clone();
                newState[blankIdx] = newState[newBlank];
                newState[newBlank] = 0;

                int g = current.g + 1;
                int h = manhattanDistance(newState, goal);
                neighbors.add(new Node(newState, current, actions[m], g, h));
            }
        }
        return neighbors;
    }

    public static void printBoard(int[] state) {
        for (int r = 0; r < 3; r++) {
            System.out.println("  +---+---+---+");
            System.out.print("  |");
            for (int c = 0; c < 3; c++) {
                int val = state[r * 3 + c];
                System.out.print(" " + (val == 0 ? " " : val) + " |");
            }
            System.out.println();
        }
        System.out.println("  +---+---+---+");
    }

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("PRACTICAL 03: 8-PUZZLE SOLVER USING A* SEARCH IN JAVA");
        System.out.println("============================================================\n");

        int[] startState = {
            1, 2, 3,
            0, 4, 6,
            7, 5, 8
        };

        int[] goalState = {
            1, 2, 3,
            4, 5, 6,
            7, 8, 0
        };

        System.out.println("Initial State:");
        printBoard(startState);
        System.out.println("Initial Manhattan Distance h(n): " + manhattanDistance(startState, goalState));

        System.out.println("\nGoal State:");
        printBoard(goalState);

        // A* Search
        PriorityQueue<Node> openQueue = new PriorityQueue<>();
        Set<String> closedSet = new HashSet<>();

        Node startNode = new Node(startState, null, "START", 0, manhattanDistance(startState, goalState));
        openQueue.add(startNode);

        Node goalNode = null;
        int expandedCount = 0;

        while (!openQueue.isEmpty()) {
            Node current = openQueue.poll();
            expandedCount++;

            if (Arrays.equals(current.state, goalState)) {
                goalNode = current;
                break;
            }

            String stateKey = Arrays.toString(current.state);
            if (closedSet.contains(stateKey)) {
                continue;
            }
            closedSet.add(stateKey);

            for (Node neighbor : getNeighbors(current, goalState)) {
                if (!closedSet.contains(Arrays.toString(neighbor.state))) {
                    openQueue.add(neighbor);
                }
            }
        }

        if (goalNode != null) {
            List<Node> path = new ArrayList<>();
            Node curr = goalNode;
            while (curr != null) {
                path.add(curr);
                curr = curr.parent;
            }
            Collections.reverse(path);

            System.out.println("\nOptimal Solution Found in " + (path.size() - 1) + " moves!");
            System.out.println("Total Nodes Expanded: " + expandedCount);
            System.out.println("\n--- Step-by-Step Solution Path ---");

            for (int i = 0; i < path.size(); i++) {
                Node n = path.get(i);
                System.out.println("Step " + i + " [Action: " + n.action + ", g=" + n.g + ", h=" + n.h + ", f=" + n.f + "]:");
                printBoard(n.state);
                System.out.println();
            }
        } else {
            System.out.println("No solution found.");
        }

        System.out.println("[SUCCESS] Practical 03 executed successfully.");
    }
}

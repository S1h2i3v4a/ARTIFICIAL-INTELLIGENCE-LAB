/**
 * BCS751 ARTIFICIAL INTELLIGENCE LAB
 * Practical 08: 4-Queens Problem using Backtracking Search (Prolog Logic Engine in Java).
 */

import java.util.*;

public class Practical08 {

    // Check if placement at (row, col) is safe against existing queen placements
    public static boolean isSafe(List<Integer> queens, int row, int col) {
        for (int prevCol = 0; prevCol < queens.size(); prevCol++) {
            int prevRow = queens.get(prevCol);
            // Same row
            if (prevRow == row) return false;
            // Diagonal conflict: |row1 - row2| == |col1 - col2|
            if (Math.abs(prevRow - row) == Math.abs(prevCol - col)) return false;
        }
        return true;
    }

    public static void solve4Queens(int col, List<Integer> current, List<List<Integer>> solutions, List<String> trace) {
        // Base case: all 4 queens successfully placed
        if (col == 4) {
            solutions.add(new ArrayList<>(current));
            trace.add("SUCCESS: Found Solution #" + solutions.size() + " -> " + current);
            return;
        }

        // Try placing in each row of column 'col'
        for (int row = 0; row < 4; row++) {
            if (isSafe(current, row, col)) {
                current.add(row);
                trace.add("Place Q" + (col + 1) + " at (Row " + row + ", Col " + col + ") [SAFE]");
                // Recursive descent
                solve4Queens(col + 1, current, solutions, trace);
                // Backtrack
                int removed = current.remove(current.size() - 1);
                trace.add("Backtrack from (Row " + removed + ", Col " + col + ")");
            } else {
                trace.add("Conflict Q" + (col + 1) + " at (Row " + row + ", Col " + col + ") [REJECTED]");
            }
        }
    }

    public static void printChessboard(List<Integer> solution) {
        System.out.println("   C0  C1  C2  C3");
        for (int r = 0; r < 4; r++) {
            System.out.print("R" + r + " ");
            for (int c = 0; c < 4; c++) {
                if (solution.get(c) == r) {
                    System.out.print("[Q] ");
                } else {
                    System.out.print(" .  ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("PRACTICAL 08: 4-QUEENS PROBLEM VIA BACKTRACKING IN JAVA");
        System.out.println("============================================================\n");

        List<List<Integer>> solutions = new ArrayList<>();
        List<String> trace = new ArrayList<>();
        List<Integer> current = new ArrayList<>();

        solve4Queens(0, current, solutions, trace);

        System.out.println("--- Backtracking Search Trace (First 14 Decisions) ---");
        for (int i = 0; i < Math.min(14, trace.size()); i++) {
            System.out.println("  " + trace.get(i));
        }
        System.out.println("  ... [Total decision points explored: " + trace.size() + "]");

        System.out.println("\nTotal Valid Distinct Solutions Found: " + solutions.size());

        for (int i = 0; i < solutions.size(); i++) {
            List<Integer> sol = solutions.get(i);
            List<Integer> oneBased = new ArrayList<>();
            for (int r : sol) oneBased.add(r + 1);

            System.out.println("\n--- Solution #" + (i + 1) + " (Rows 1-based: " + oneBased + ") ---");
            printChessboard(sol);
        }

        System.out.println("\n[SUCCESS] Practical 08 executed successfully.");
    }
}

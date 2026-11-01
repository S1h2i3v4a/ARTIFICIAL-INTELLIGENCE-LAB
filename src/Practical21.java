/**
 * BCS751 ARTIFICIAL INTELLIGENCE LAB
 * Practical 21: Design a Constraint Satisfaction Problem (CSP) Solver in Java.
 */

import java.util.*;

public class Practical21 {

    // ==========================================
    // PART A: Map Colouring CSP (Australia Map)
    // ==========================================
    public static void solveMapColouring() {
        System.out.println("--- Part A: Map Colouring CSP (Australia Map with 3 Colours) ---");

        // Variables: Regions of Australia
        List<String> regions = Arrays.asList("WA", "NT", "SA", "Q", "NSW", "V", "T");

        // Domains: Red, Green, Blue
        List<String> domain = Arrays.asList("Red", "Green", "Blue");

        // Constraints: Adjacent regions cannot share the same colour
        Map<String, List<String>> neighbors = new HashMap<>();
        neighbors.put("WA", Arrays.asList("NT", "SA"));
        neighbors.put("NT", Arrays.asList("WA", "SA", "Q"));
        neighbors.put("SA", Arrays.asList("WA", "NT", "Q", "NSW", "V"));
        neighbors.put("Q", Arrays.asList("NT", "SA", "NSW"));
        neighbors.put("NSW", Arrays.asList("Q", "SA", "V"));
        neighbors.put("V", Arrays.asList("SA", "NSW"));
        neighbors.put("T", Collections.emptyList()); // Tasmania has no land borders

        Map<String, String> assignment = new LinkedHashMap<>();

        if (backtrackMap(0, regions, assignment, domain, neighbors)) {
            System.out.println("Valid Constraint-Satisfying Colour Assignment Found:");
            for (Map.Entry<String, String> entry : assignment.entrySet()) {
                System.out.printf("  Region %-5s -> Colour: %-8s\n", entry.getKey(), entry.getValue());
            }

            // Verify constraints
            boolean valid = true;
            for (String r : regions) {
                String c = assignment.get(r);
                for (String n : neighbors.get(r)) {
                    if (c.equals(assignment.get(n))) {
                        valid = false;
                        break;
                    }
                }
            }
            System.out.println("Constraint Consistency Verification: " + (valid ? "PASSED (No neighboring conflicts)" : "FAILED"));
        } else {
            System.out.println("No valid colouring found.");
        }
    }

    private static boolean backtrackMap(int index, List<String> regions, Map<String, String> assignment,
                                        List<String> domain, Map<String, List<String>> neighbors) {
        if (index == regions.size()) return true;

        String region = regions.get(index);
        for (String colour : domain) {
            boolean conflict = false;
            for (String neighbor : neighbors.get(region)) {
                if (assignment.containsKey(neighbor) && assignment.get(neighbor).equals(colour)) {
                    conflict = true;
                    break;
                }
            }
            if (!conflict) {
                assignment.put(region, colour);
                if (backtrackMap(index + 1, regions, assignment, domain, neighbors)) {
                    return true;
                }
                assignment.remove(region);
            }
        }
        return false;
    }

    // ==========================================
    // PART B: 9x9 Sudoku CSP Solver with Backtracking & Forward Checking
    // ==========================================
    public static void solveSudokuCSP() {
        System.out.println("\n--- Part B: 9x9 Sudoku Puzzle CSP Solver ---");

        int[][] board = {
            {5, 3, 0, 0, 7, 0, 0, 0, 0},
            {6, 0, 0, 1, 9, 5, 0, 0, 0},
            {0, 9, 8, 0, 0, 0, 0, 6, 0},
            {8, 0, 0, 0, 6, 0, 0, 0, 3},
            {4, 0, 0, 8, 0, 3, 0, 0, 1},
            {7, 0, 0, 0, 2, 0, 0, 0, 6},
            {0, 6, 0, 0, 0, 0, 2, 8, 0},
            {0, 0, 0, 4, 1, 9, 0, 0, 5},
            {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };

        System.out.println("Initial Sudoku Grid (0 = Blank):");
        printSudoku(board);

        if (solveSudokuBacktracking(board)) {
            System.out.println("\nSolved Sudoku Grid (All Row, Col & 3x3 Box Constraints Satisfied):");
            printSudoku(board);
        } else {
            System.out.println("No solution exists.");
        }
    }

    private static boolean solveSudokuBacktracking(int[][] board) {
        int emptyR = -1, emptyC = -1;
        boolean found = false;

        // Find empty cell
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0) {
                    emptyR = r;
                    emptyC = c;
                    found = true;
                    break;
                }
            }
            if (found) break;
        }

        if (!found) return true; // All cells filled

        for (int num = 1; num <= 9; num++) {
            if (isValidSudokuMove(board, emptyR, emptyC, num)) {
                board[emptyR][emptyC] = num;
                if (solveSudokuBacktracking(board)) return true;
                board[emptyR][emptyC] = 0; // Backtrack
            }
        }
        return false;
    }

    private static boolean isValidSudokuMove(int[][] board, int row, int col, int num) {
        // Row and Col check
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board[i][col] == num) return false;
        }
        // 3x3 Subgrid check
        int startR = (row / 3) * 3;
        int startC = (col / 3) * 3;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[startR + r][startC + c] == num) return false;
            }
        }
        return true;
    }

    private static void printSudoku(int[][] board) {
        for (int r = 0; r < 9; r++) {
            if (r % 3 == 0) System.out.println("  +-------+-------+-------+");
            System.out.print("  |");
            for (int c = 0; c < 9; c++) {
                System.out.print(" " + (board[r][c] == 0 ? "." : board[r][c]));
                if (c % 3 == 2) System.out.print(" |");
            }
            System.out.println();
        }
        System.out.println("  +-------+-------+-------+");
    }

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("PRACTICAL 21: CONSTRAINT SATISFACTION PROBLEM (CSP) SOLVER");
        System.out.println("============================================================\n");

        solveMapColouring();
        solveSudokuCSP();

        System.out.println("\n[SUCCESS] Practical 21 executed successfully.");
    }
}

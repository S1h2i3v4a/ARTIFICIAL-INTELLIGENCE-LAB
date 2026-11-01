/**
 * BCS751 ARTIFICIAL INTELLIGENCE LAB
 * Practical 05: Implement Simulated Annealing Algorithm for constraint-based search problems.
 */

import java.util.*;

public class Practical05 {

    // Energy function: Number of pairs of attacking queens (Constraints violated)
    // Board[i] = row of queen in column i
    public static int calculateConflicts(int[] board) {
        int n = board.length;
        int conflicts = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Same row
                if (board[i] == board[j]) {
                    conflicts++;
                }
                // Same diagonal: |row_i - row_j| == |col_i - col_j|
                else if (Math.abs(board[i] - board[j]) == Math.abs(i - j)) {
                    conflicts++;
                }
            }
        }
        return conflicts;
    }

    public static void printBoard(int[] board) {
        int n = board.length;
        System.out.print("   ");
        for (int c = 0; c < n; c++) System.out.print(" C" + c);
        System.out.println();

        for (int r = 0; r < n; r++) {
            System.out.printf("R%d ", r);
            for (int c = 0; c < n; c++) {
                if (board[c] == r) {
                    System.out.print(" Q ");
                } else {
                    System.out.print(" . ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("PRACTICAL 05: SIMULATED ANNEALING FOR N-QUEENS IN JAVA");
        System.out.println("============================================================\n");

        int n = 8;
        double initialTemp = 100.0;
        double coolingRate = 0.985;
        int maxIterations = 3000;

        System.out.println("Problem: 8-Queens Constraint Satisfaction Problem");
        System.out.println("Energy Function: Count of attacking queen pairs (Goal: E = 0)");
        System.out.println("Initial Temperature T_0: " + initialTemp + ", Cooling Rate: " + coolingRate);

        Random rng = new Random(101); // Fixed seed for reproducible lab execution

        // Initialize random board
        int[] currentBoard = new int[n];
        for (int i = 0; i < n; i++) {
            currentBoard[i] = rng.nextInt(n);
        }

        int currentEnergy = calculateConflicts(currentBoard);
        int[] bestBoard = currentBoard.clone();
        int bestEnergy = currentEnergy;

        double temp = initialTemp;
        int totalIterations = 0;

        System.out.printf("\n%-12s %-16s %-16s %-12s\n", "Iteration", "Temperature", "Current Energy", "Best Energy");
        System.out.println("--------------------------------------------------------");

        for (int iter = 1; iter <= maxIterations; iter++) {
            totalIterations = iter;
            if (currentEnergy == 0) {
                break;
            }

            // Generate neighbor: pick a random column and change its row
            int col = rng.nextInt(n);
            int newRow = rng.nextInt(n);
            while (newRow == currentBoard[col]) {
                newRow = rng.nextInt(n);
            }

            int[] neighborBoard = currentBoard.clone();
            neighborBoard[col] = newRow;
            int neighborEnergy = calculateConflicts(neighborBoard);

            int deltaE = neighborEnergy - currentEnergy;

            // Metropolis acceptance criterion
            if (deltaE < 0) {
                currentBoard = neighborBoard;
                currentEnergy = neighborEnergy;
            } else {
                double acceptanceProb = Math.exp(-deltaE / Math.max(temp, 1e-8));
                if (rng.nextDouble() < acceptanceProb) {
                    currentBoard = neighborBoard;
                    currentEnergy = neighborEnergy;
                }
            }

            if (currentEnergy < bestEnergy) {
                bestBoard = currentBoard.clone();
                bestEnergy = currentEnergy;
            }

            if (iter == 1 || iter % 150 == 0) {
                System.out.printf("%-12d %-16.4f %-16d %-12d\n", iter, temp, currentEnergy, bestEnergy);
            }

            // Exponential cooling schedule
            temp *= coolingRate;
        }

        System.out.println("--------------------------------------------------------");
        System.out.println("Annealing Completed!");
        System.out.println("Total Iterations: " + totalIterations);
        System.out.println("Final Solution Energy (Conflicts): " + bestEnergy);
        System.out.println("Queen Column Placements (Row per column): " + Arrays.toString(bestBoard));

        System.out.println("\nFinal Chessboard Configuration:");
        printBoard(bestBoard);

        if (bestEnergy == 0) {
            System.out.println("\n[SUCCESS] Optimal zero-conflict state achieved!");
        } else {
            System.out.println("\n[NOTE] Sub-optimal state achieved with " + bestEnergy + " conflicts.");
        }

        System.out.println("[SUCCESS] Practical 05 executed successfully.");
    }
}

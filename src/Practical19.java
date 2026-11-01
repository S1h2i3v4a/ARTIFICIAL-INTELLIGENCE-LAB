/**
 * BCS751 ARTIFICIAL INTELLIGENCE LAB
 * Practical 19: Simulate a Vacuum Cleaner Agent in a 2D Environment in Java.
 */

import java.util.*;

public class Practical19 {

    // 2D Environment: 0 = Clean, 1 = Dirty
    static class Environment2D {
        int rows, cols;
        int[][] grid;
        int initialDirtyCount;

        Environment2D(int rows, int cols, double dirtProbability, long seed) {
            this.rows = rows;
            this.cols = cols;
            this.grid = new int[rows][cols];
            Random rng = new Random(seed);
            int count = 0;

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (rng.nextDouble() < dirtProbability) {
                        grid[r][c] = 1;
                        count++;
                    }
                }
            }
            if (count == 0) { // Guarantee at least a few dirty tiles
                grid[0][1] = 1; grid[1][2] = 1; grid[2][0] = 1; grid[2][2] = 1;
                count = 4;
            }
            this.initialDirtyCount = count;
        }

        public boolean isDirty(int r, int c) { return grid[r][c] == 1; }
        public void clean(int r, int c) { grid[r][c] = 0; }

        public int remainingDirt() {
            int count = 0;
            for (int[] row : grid) {
                for (int cell : row) if (cell == 1) count++;
            }
            return count;
        }

        public void printState(int agentR, int agentC) {
            System.out.print("     ");
            for (int c = 0; c < cols; c++) System.out.print(" C" + c + " ");
            System.out.println();
            for (int r = 0; r < rows; r++) {
                System.out.printf("  R%d ", r);
                for (int c = 0; c < cols; c++) {
                    if (r == agentR && c == agentC) {
                        System.out.print(grid[r][c] == 1 ? "[A*]" : "[ A]");
                    } else {
                        System.out.print(grid[r][c] == 1 ? "  * " : "  . ");
                    }
                }
                System.out.println();
            }
        }
    }

    // Intelligent Goal-Based Vacuum Agent
    static class VacuumAgent {
        int r, c;
        int energyConsumed = 0;
        int tilesCleaned = 0;

        VacuumAgent(int startR, int startC) {
            this.r = startR;
            this.c = startC;
        }

        public void runSimulation(Environment2D env) {
            System.out.println("Starting Simulation from Position: (" + r + ", " + c + ")");
            System.out.println("Initial Environment Layout (* = Dirty, . = Clean, [A] = Agent):");
            env.printState(r, c);
            System.out.println("Total Dirty Tiles to Clean: " + env.initialDirtyCount + "\n");

            System.out.printf("%-6s %-12s %-20s %-16s %-14s\n", "Step", "Position", "Percept (Dirt?)", "Action Taken", "Dirt Remaining");
            System.out.println("-------------------------------------------------------------------------");

            int step = 0;

            // Boustrophedon (lawnmower) systematic coverage path
            for (int row = 0; row < env.rows; row++) {
                boolean movingRight = (row % 2 == 0);
                int startCol = movingRight ? 0 : env.cols - 1;
                int endCol = movingRight ? env.cols : -1;
                int deltaCol = movingRight ? 1 : -1;

                // Move agent to row start if needed
                if (r != row) {
                    r = row;
                    energyConsumed++;
                    step++;
                    System.out.printf("%-6d (%d, %d)%-6s %-20s %-16s %-14d\n",
                        step, r, c, "", "Clean tile", "MOVE DOWN", env.remainingDirt());
                }

                int col = c;
                while (col != endCol) {
                    c = col;
                    // Sensory check: Is current tile dirty?
                    if (env.isDirty(r, c)) {
                        env.clean(r, c);
                        tilesCleaned++;
                        energyConsumed += 2; // Sucking costs 2 units
                        step++;
                        System.out.printf("%-6d (%d, %d)%-6s %-20s %-16s %-14d\n",
                            step, r, c, "", "DIRTY DETECTED", "SUCK (CLEAN)", env.remainingDirt());
                    }

                    // Move to next tile if not end of row
                    int nextCol = col + deltaCol;
                    if (nextCol != endCol) {
                        c = nextCol;
                        energyConsumed++;
                        step++;
                        String moveAction = movingRight ? "MOVE RIGHT" : "MOVE LEFT";
                        System.out.printf("%-6d (%d, %d)%-6s %-20s %-16s %-14d\n",
                            step, r, c, "", (env.isDirty(r, c) ? "DIRTY" : "CLEAN"), moveAction, env.remainingDirt());
                    }
                    col = nextCol;
                }
            }

            System.out.println("-------------------------------------------------------------------------");
            System.out.println("\nFinal Cleaned Environment State:");
            env.printState(r, c);

            System.out.println("\n--- Performance Metrics ---");
            System.out.println("Initial Dirty Tiles:  " + env.initialDirtyCount);
            System.out.println("Tiles Cleaned:        " + tilesCleaned);
            System.out.println("Remaining Dirty:      " + env.remainingDirt());
            System.out.println("Total Steps / Moves:  " + step);
            System.out.println("Total Energy Cost:    " + energyConsumed + " units");
            double cleanEfficiency = ((double) tilesCleaned / env.initialDirtyCount) * 100.0;
            System.out.printf("Cleaning Efficiency:  %.2f%%\n", cleanEfficiency);
        }
    }

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("PRACTICAL 19: 2D VACUUM CLEANER AGENT SIMULATION IN JAVA");
        System.out.println("============================================================\n");

        int rows = 3, cols = 3;
        Environment2D env = new Environment2D(rows, cols, 0.45, 12345);
        VacuumAgent agent = new VacuumAgent(0, 0);

        agent.runSimulation(env);

        System.out.println("\n[SUCCESS] Practical 19 executed successfully.");
    }
}

/**
 * BCS751 ARTIFICIAL INTELLIGENCE LAB
 * Practical 18: Enhance Minimax with Alpha-Beta Pruning in Java.
 */

import java.util.*;

public class Practical18 {

    static long standardMinimaxEvaluations = 0;
    static long alphaBetaEvaluations = 0;
    static int prunedBranches = 0;

    public static void printBoard(char[][] board) {
        for (int r = 0; r < 3; r++) {
            System.out.println("  +---+---+---+");
            System.out.print("  |");
            for (int c = 0; c < 3; c++) {
                System.out.print(" " + board[r][c] + " |");
            }
            System.out.println();
        }
        System.out.println("  +---+---+---+");
    }

    public static boolean checkWin(char[][] board, char player) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true;
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true;
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) return true;
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) return true;
        return false;
    }

    public static boolean hasMovesLeft(char[][] board) {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == ' ') return true;
            }
        }
        return false;
    }

    // Standard Minimax
    public static int standardMinimax(char[][] board, int depth, boolean isMax) {
        standardMinimaxEvaluations++;
        if (checkWin(board, 'O')) return 10 - depth;
        if (checkWin(board, 'X')) return depth - 10;
        if (!hasMovesLeft(board)) return 0;

        if (isMax) {
            int best = Integer.MIN_VALUE;
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (board[r][c] == ' ') {
                        board[r][c] = 'O';
                        best = Math.max(best, standardMinimax(board, depth + 1, false));
                        board[r][c] = ' ';
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (board[r][c] == ' ') {
                        board[r][c] = 'X';
                        best = Math.min(best, standardMinimax(board, depth + 1, true));
                        board[r][c] = ' ';
                    }
                }
            }
            return best;
        }
    }

    // Enhanced Minimax with Alpha-Beta Pruning
    public static int alphaBetaMinimax(char[][] board, int depth, int alpha, int beta, boolean isMax) {
        alphaBetaEvaluations++;
        if (checkWin(board, 'O')) return 10 - depth;
        if (checkWin(board, 'X')) return depth - 10;
        if (!hasMovesLeft(board)) return 0;

        if (isMax) {
            int best = Integer.MIN_VALUE;
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (board[r][c] == ' ') {
                        board[r][c] = 'O';
                        int val = alphaBetaMinimax(board, depth + 1, alpha, beta, false);
                        board[r][c] = ' ';
                        best = Math.max(best, val);
                        alpha = Math.max(alpha, best);

                        // Alpha-Beta Cut-off
                        if (beta <= alpha) {
                            prunedBranches++;
                            return best;
                        }
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (board[r][c] == ' ') {
                        board[r][c] = 'X';
                        int val = alphaBetaMinimax(board, depth + 1, alpha, beta, true);
                        board[r][c] = ' ';
                        best = Math.min(best, val);
                        beta = Math.min(beta, best);

                        // Alpha-Beta Cut-off
                        if (beta <= alpha) {
                            prunedBranches++;
                            return best;
                        }
                    }
                }
            }
            return best;
        }
    }

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("PRACTICAL 18: MINIMAX OPTIMIZATION WITH ALPHA-BETA PRUNING");
        System.out.println("============================================================\n");

        char[][] board = {
            {'X', ' ', ' '},
            {' ', 'O', ' '},
            {' ', ' ', 'X'}
        };

        System.out.println("Game State to Evaluate (Early Mid-game with 6 free cells):");
        printBoard(board);

        // 1. Run Standard Minimax
        standardMinimaxEvaluations = 0;
        int stdBestVal = Integer.MIN_VALUE;
        int[] stdBestMove = {-1, -1};

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == ' ') {
                    board[r][c] = 'O';
                    int val = standardMinimax(board, 0, false);
                    board[r][c] = ' ';
                    if (val > stdBestVal) {
                        stdBestVal = val;
                        stdBestMove[0] = r;
                        stdBestMove[1] = c;
                    }
                }
            }
        }

        // 2. Run Minimax with Alpha-Beta Pruning
        alphaBetaEvaluations = 0;
        prunedBranches = 0;
        int abBestVal = Integer.MIN_VALUE;
        int[] abBestMove = {-1, -1};
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == ' ') {
                    board[r][c] = 'O';
                    int val = alphaBetaMinimax(board, 0, alpha, beta, false);
                    board[r][c] = ' ';
                    if (val > abBestVal) {
                        abBestVal = val;
                        abBestMove[0] = r;
                        abBestMove[1] = c;
                    }
                    alpha = Math.max(alpha, abBestVal);
                }
            }
        }

        System.out.println("\n--- Comparative Efficiency Analysis ---");
        System.out.printf("%-30s %-20s %-20s\n", "Metric", "Standard Minimax", "Alpha-Beta Pruning");
        System.out.println("-----------------------------------------------------------------------");
        System.out.printf("%-30s %-20s %-20s\n", "Best Move Selected",
            "(" + stdBestMove[0] + ", " + stdBestMove[1] + ")",
            "(" + abBestMove[0] + ", " + abBestMove[1] + ")");
        System.out.printf("%-30s %-20d %-20d\n", "Optimal Utility Score", stdBestVal, abBestVal);
        System.out.printf("%-30s %-20d %-20d\n", "Total Nodes Evaluated", standardMinimaxEvaluations, alphaBetaEvaluations);
        System.out.printf("%-30s %-20s %-20d\n", "Pruned Sub-Trees (Cut-offs)", "N/A (0)", prunedBranches);

        double reduction = (1.0 - (double) alphaBetaEvaluations / standardMinimaxEvaluations) * 100.0;
        System.out.printf("%-30s %-20s %-20.2f%%\n", "Search Space Reduction", "0.00%", reduction);
        System.out.println("-----------------------------------------------------------------------");

        System.out.println("\nConclusion:");
        System.out.println("  Alpha-Beta Pruning produces the mathematically identical optimal move");
        System.out.printf("  while cutting node evaluations by %.2f%%, avoiding exhaustive subtree search.\n", reduction);

        System.out.println("\n[SUCCESS] Practical 18 executed successfully.");
    }
}

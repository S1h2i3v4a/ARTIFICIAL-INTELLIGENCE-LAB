/**
 * BCS751 ARTIFICIAL INTELLIGENCE LAB
 * Practical 17: Implement Min-Max (Minimax) Algorithm for Decision Making in Java.
 */

import java.util.*;

public class Practical17 {

    static long minimaxEvaluations = 0;

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

    // Minimax Algorithm
    // AI is Maximizer ('O'), Player is Minimizer ('X')
    public static int minimax(char[][] board, int depth, boolean isMax) {
        minimaxEvaluations++;

        if (checkWin(board, 'O')) return 10 - depth;  // AI wins
        if (checkWin(board, 'X')) return depth - 10;  // Player wins
        if (!hasMovesLeft(board)) return 0;           // Draw

        if (isMax) {
            int best = Integer.MIN_VALUE;
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    if (board[r][c] == ' ') {
                        board[r][c] = 'O';
                        best = Math.max(best, minimax(board, depth + 1, false));
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
                        best = Math.min(best, minimax(board, depth + 1, true));
                        board[r][c] = ' ';
                    }
                }
            }
            return best;
        }
    }

    public static int[] findBestMove(char[][] board) {
        int bestVal = Integer.MIN_VALUE;
        int[] bestMove = {-1, -1};

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == ' ') {
                    board[r][c] = 'O';
                    int moveVal = minimax(board, 0, false);
                    board[r][c] = ' ';

                    if (moveVal > bestVal) {
                        bestMove[0] = r;
                        bestMove[1] = c;
                        bestVal = moveVal;
                    }
                }
            }
        }
        return new int[]{bestMove[0], bestMove[1], bestVal};
    }

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("PRACTICAL 17: MINIMAX DECISION MAKING ALGORITHM IN JAVA");
        System.out.println("============================================================\n");

        // Strategic Mid-game board position:
        // X has played two moves threatening a fork
        char[][] board = {
            {'X', 'O', 'X'},
            {' ', 'O', ' '},
            {' ', 'X', ' '}
        };

        System.out.println("Current Game State (AI 'O' to move):");
        printBoard(board);

        minimaxEvaluations = 0;
        int[] result = findBestMove(board);

        System.out.println("\n--- Minimax Evaluation Analysis ---");
        System.out.println("Total Game-Tree Nodes Evaluated: " + minimaxEvaluations);
        System.out.printf("Optimal Move for AI ('O'): Row %d, Col %d\n", result[0], result[1]);
        System.out.println("Calculated Utility / Score: " + result[2]);

        // Apply best move
        board[result[0]][result[1]] = 'O';
        System.out.println("\nBoard After AI ('O') Minimax Optimal Move:");
        printBoard(board);

        System.out.println("\n[SUCCESS] Practical 17 executed successfully.");
    }
}

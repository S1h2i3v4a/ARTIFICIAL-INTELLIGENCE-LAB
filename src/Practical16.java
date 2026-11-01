/**
 * BCS751 ARTIFICIAL INTELLIGENCE LAB
 * Practical 16: Implement Tic-Tac-Toe game with a basic AI opponent in Java.
 */

import java.util.*;

public class Practical16 {

    // Board representation: 3x3 array of chars (' ', 'X', 'O')
    // Player: 'X', AI: 'O'

    public static void printBoard(char[][] board) {
        System.out.println("     0   1   2 ");
        System.out.println("   +---+---+---+");
        for (int r = 0; r < 3; r++) {
            System.out.print(" " + r + " |");
            for (int c = 0; c < 3; c++) {
                System.out.print(" " + board[r][c] + " |");
            }
            System.out.println();
            System.out.println("   +---+---+---+");
        }
    }

    public static boolean checkWin(char[][] board, char player) {
        // Rows and Columns
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true;
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true;
        }
        // Diagonals
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) return true;
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) return true;
        return false;
    }

    public static boolean isBoardFull(char[][] board) {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == ' ') return false;
            }
        }
        return true;
    }

    // Heuristic Rule-Based AI Opponent:
    // 1. Win if possible
    // 2. Block player if player is about to win
    // 3. Take Center (1, 1)
    // 4. Take Corners
    // 5. Take any open side
    public static int[] getHeuristicAIMove(char[][] board) {
        // 1. Check if AI can win immediately
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == ' ') {
                    board[r][c] = 'O';
                    if (checkWin(board, 'O')) {
                        board[r][c] = ' ';
                        return new int[]{r, c, 1}; // Priority 1: Immediate win
                    }
                    board[r][c] = ' ';
                }
            }
        }

        // 2. Check if Player is about to win and block them
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == ' ') {
                    board[r][c] = 'X';
                    if (checkWin(board, 'X')) {
                        board[r][c] = ' ';
                        return new int[]{r, c, 2}; // Priority 2: Immediate block
                    }
                    board[r][c] = ' ';
                }
            }
        }

        // 3. Take center
        if (board[1][1] == ' ') {
            return new int[]{1, 1, 3}; // Priority 3: Center
        }

        // 4. Take corners
        int[][] corners = {{0, 0}, {0, 2}, {2, 0}, {2, 2}};
        for (int[] corner : corners) {
            if (board[corner[0]][corner[1]] == ' ') {
                return new int[]{corner[0], corner[1], 4}; // Priority 4: Corner
            }
        }

        // 5. Take edges
        int[][] edges = {{0, 1}, {1, 0}, {1, 2}, {2, 1}};
        for (int[] edge : edges) {
            if (board[edge[0]][edge[1]] == ' ') {
                return new int[]{edge[0], edge[1], 5}; // Priority 5: Edge
            }
        }

        return null;
    }

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("PRACTICAL 16: TIC-TAC-TOE GAME WITH HEURISTIC AI OPPONENT");
        System.out.println("============================================================\n");

        char[][] board = {
            {' ', ' ', ' '},
            {' ', ' ', ' '},
            {' ', ' ', ' '}
        };

        System.out.println("Game Setup: Player ('X') vs AI Agent ('O')");
        System.out.println("Initial Empty Grid:");
        printBoard(board);

        // Simulated game match demonstrating AI strategic moves
        int[][] playerMoves = {{0, 0}, {2, 2}, {0, 2}, {1, 0}};
        int moveIdx = 0;

        String[] priorityNames = {
            "", "Winning Move", "Blocking Move", "Center Control", "Corner Move", "Edge Move"
        };

        while (!isBoardFull(board)) {
            // Player X Move
            if (moveIdx < playerMoves.length) {
                int[] pm = playerMoves[moveIdx++];
                board[pm[0]][pm[1]] = 'X';
                System.out.println("\n>>> Turn: Player ('X') plays at (" + pm[0] + ", " + pm[1] + ")");
                printBoard(board);

                if (checkWin(board, 'X')) {
                    System.out.println("\nResult: Player 'X' Wins!");
                    break;
                }
                if (isBoardFull(board)) {
                    System.out.println("\nResult: Game Drawn!");
                    break;
                }
            }

            // AI O Move
            int[] aiMove = getHeuristicAIMove(board);
            if (aiMove != null) {
                board[aiMove[0]][aiMove[1]] = 'O';
                System.out.println("\n>>> Turn: AI Opponent ('O') plays at (" + aiMove[0] + ", " + aiMove[1] + ") [Rule: " + priorityNames[aiMove[2]] + "]");
                printBoard(board);

                if (checkWin(board, 'O')) {
                    System.out.println("\nResult: AI Opponent 'O' Wins!");
                    break;
                }
                if (isBoardFull(board)) {
                    System.out.println("\nResult: Game Drawn!");
                    break;
                }
            }
        }

        System.out.println("\n[SUCCESS] Practical 16 executed successfully.");
    }
}

package Org.Example;
import java.awt.Point;


//Beklager lidt mange kommentarer men gjorde det nemmere for mig selv at forstå koden og hvad der var næste trin.



public class TicTacToe {
    private final char[][] board; // Selve Boardet
    private final char playerSymbol; // Spiller symbol enten X eller O
    private final char aiSymbol; // Computers symbol (modsat af spiller)
    private final int searchDepth; //Computers søge dybde


    //Constructor til at lave et nyt spil
    public TicTacToe(char playerSymbol, int searchDepth) {
        this.playerSymbol = playerSymbol;
        // Hvis spilleren er X, så er computeren O og omvendt
        this.aiSymbol = (playerSymbol == 'X') ? 'O' : 'X';
        this.searchDepth = searchDepth;
        this.board = new char[3][3];

        // Laver nyt board med 3x3 tomme felter
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }


    //Metode til at lave et træk
    public void makeMove(int row, int col, char player) {
        board[row][col] = player;
    }


    //Metode til at finde det bedste træk
    public Point findBestMove() {
        int bestValue = Integer.MIN_VALUE;
        Point bestMove = new Point(-1, -1);

        // Her prøver den alle mulige træk og "evaluates/beregner" med min/max
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') { // Kun tomme felter
                    board[i][j] = aiSymbol;
                    int moveValue = minimax(searchDepth, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    board[i][j] = ' ';

                    // OPDATERE bedste træk hvis det er et bedre
                    if (moveValue > bestValue) {
                        bestValue = moveValue;
                        bestMove = new Point(i, j);
                    }
                }
            }
        }
        return bestMove;
    }

    //Minimax algoritme med Alpha-Beta pruning
    private int minimax(int depth, boolean isMaximizing, int alpha, int beta) {

        int score = evaluate();


        if (score == 1000 || score == -1000 || !isMovesLeft() || depth == 0) {
            return score;
        }

        if (isMaximizing) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = aiSymbol;
                        best = Math.max(best, minimax(depth - 1, false, alpha, beta));
                        board[i][j] = ' ';
                        alpha = Math.max(alpha, best);
                        if (beta <= alpha) break;
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = playerSymbol;
                        best = Math.min(best, minimax(depth - 1, true, alpha, beta));
                        board[i][j] = ' ';
                        beta = Math.min(beta, best);
                        if (beta <= alpha) break;
                    }
                }
            }
            return best;
        }
    }


    //Metode til at evaluere boardet med vægtede værider
    // Computeren får positive point for gode positioner
    // Spilleren får negative point for at optage gode positioner
    // Returnere en heuristisk score af boardet
    public int evaluate() {
        // Felt værider er sat ud fra krav som følgende
        int[][] fieldValues = {
                {3, 2, 3},
                {2, 4, 2},
                {3, 2, 3}
        };

        int score = 0;

        // Her tjekkes for "Sejr eller tabt spil"
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != ' ') {
                return (board[i][0] == aiSymbol) ? 1000 : -1000; // Win/Loss
            }
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != ' ') {
                return (board[0][i] == aiSymbol) ? 1000 : -1000; // Win/Loss
            }
        }
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != ' ') {
            return (board[0][0] == aiSymbol) ? 1000 : -1000; // Win/Loss
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != ' ') {
            return (board[0][2] == aiSymbol) ? 1000 : -1000; // Win/Loss
        }

        // Vægtet field "evaluation"
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == aiSymbol) {
                    score += fieldValues[i][j]; // Computeren får point for at optage gode felter
                } else if (board[i][j] == playerSymbol) {
                    score -= fieldValues[i][j]; // spiller reducere computerens styrke ved at optage gode felter
                }
            }
        }
        return score; // Returnerer den heuristiske score
    }


    //Tjekker om der er nogle træk tilbage på boardet
    //return "true" hvis der er træk tilbage.
    public boolean isMovesLeft() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') return true;
            }
        }
        return false;
    }
}

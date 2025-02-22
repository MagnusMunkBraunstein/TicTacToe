package Org.Example;
import javax.swing.*;
import java.awt.*;

public class TicTacToeGUI {

    private final JFrame window; // Selve gui vinduet
    private final JButton[][] buttons; // Knapperne på boardet
    private final TicTacToe game; // selve spillet der spilles
    private final char playerSymbol; // Spillerens symbol
    private final char aiSymbol; // Computerens symbol
    private boolean playerTurn; // Om det er spillerens tur


    // Konstruktør til at starte spillet med instillinger
    // Spiller vælger "search dept"
    // Spiller vælger X eller O
    // boardet bliver lavet

    public TicTacToeGUI() {
        window = new JFrame("The Tic Tac Toe Game");
        window.setSize(400, 400);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new GridLayout(3, 3));

        // Spørger spiller om søgedybde og symbol
        int searchDepth = selectSearchDepth();
        playerSymbol = selectPlayerSymbol();
        aiSymbol = (playerSymbol == 'X') ? 'O' : 'X';
        playerTurn = (playerSymbol == 'X');
        game = new TicTacToe(playerSymbol, searchDepth);

        // Knapper på board laves og tilføjes til vinduet
        buttons = new JButton[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton(" ");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 40));

                // Fix the unwanted focus border issue on first button
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setContentAreaFilled(false);

                final int row = i, col = j;
                buttons[i][j].addActionListener(e -> playerMove(row, col));
                window.add(buttons[i][j]);
            }
        }
        window.setVisible(true);

        // hvis spilleren vælger "O" laver ai første træk
        if (!playerTurn) {
            aiMove();
        }
    }

    // håndtere spiller træk og opdaterer boardet
    // skifter tur hvis ikke der er vundet
    private void playerMove(int row, int col) {
        if (buttons[row][col].getText().equals(" ") && playerTurn) {
            buttons[row][col].setText(String.valueOf(playerSymbol));
            game.makeMove(row, col, playerSymbol);
            playerTurn = false;

            if (!checkGameOver()) {
                aiMove();
            }
        }
    }

    //håndtere Computerens træk og opdaterer boardet
    // skifter til spillers tur hvis ikke der er vundet
    private void aiMove() {
        Point bestMove = game.findBestMove();
        if (bestMove.x != -1 && bestMove.y != -1) {
            buttons[bestMove.x][bestMove.y].setText(String.valueOf(aiSymbol));
            game.makeMove(bestMove.x, bestMove.y, aiSymbol);
        }
        playerTurn = true;
        checkGameOver();
    }


    // Tjekker om spillet er slut, hvis der er uafgjort eller en sejr
    // retunere "true" hvis spillet er slut
    private boolean checkGameOver() {
        int result = game.evaluate();

        if (result == 10) {
            JOptionPane.showMessageDialog(window, (playerSymbol == 'X' ? "Du Har vundet" : "Computeren vandt - prøv igen"));
            window.dispose();
            return true;
        } else if (result == -10) {
            JOptionPane.showMessageDialog(window, (playerSymbol == 'O' ? "Du Har vundet" : "Computeren vandt - prøv igen"));
            window.dispose();
            return true;
        } else if (!game.isMovesLeft()) {
            JOptionPane.showMessageDialog(window, "Det blev en Uafgjort");
            window.dispose();
            return true;
        }
        return false;
    }

    /**
     * Prompts the user to select the AI's search depth.
     * - The depth determines how far ahead AI looks (1-10).
     * @return The chosen depth.
     */
    private int selectSearchDepth() {
        String input = JOptionPane.showInputDialog("Indtast den søgedybde du vil spille med (1-10):");
        try {
            return Math.max(1, Math.min(Integer.parseInt(input), 10));
        } catch (Exception e) {
            return 3; // Deafult søgedybde hvis der er en fejl i indtastningen(ugyldigt input)
        }
    }

    //Her skal spille vælge om de vil være X eller O
    // hvis spiller er x starter spilleren
    // hvis spiller er O starter computeren
    private char selectPlayerSymbol() {
        return JOptionPane.showOptionDialog(null, "Vælg om du vil være 'X' eller '0'", "Vælg symbol",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"X", "O"}, "X") == 0 ? 'X' : 'O';
    }

    //start spillet
    public static void main(String[] args) {
        new TicTacToeGUI();
    }
}

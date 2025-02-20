package Org.Example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeGUI {
    private JFrame frame;
    private JButton[][] buttons;
    private TicTacToe game;
    private boolean playerTurn = true; // Player starts as X

    public TicTacToeGUI() {
        game = new TicTacToe();
        frame = new JFrame("Tic Tac Toe");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 3));

        buttons = new JButton[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton(" ");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
                buttons[i][j].setFocusPainted(false);
                final int row = i, col = j;
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (buttons[row][col].getText().equals(" ") && playerTurn) {
                            buttons[row][col].setText("X");
                            game.makeMove(row, col, 'X');
                            playerTurn = false;
                            if (!checkGameOver()) {
                                aiMove();
                            }
                        }
                    }
                });
                frame.add(buttons[i][j]);
            }
        }
        frame.setVisible(true);
    }

    private void aiMove() {
        Point bestMove = game.findBestMove();
        if (bestMove.x != -1 && bestMove.y != -1) {
            buttons[bestMove.x][bestMove.y].setText("O");
            game.makeMove(bestMove.x, bestMove.y, 'O');
        }
        playerTurn = true;
        checkGameOver();
    }

    private boolean checkGameOver() {
        int result = game.evaluate();
        if (result == 10) {
            JOptionPane.showMessageDialog(frame, "Du har vundet!");
            frame.dispose();
            return true;
        } else if (result == -10) {
            JOptionPane.showMessageDialog(frame, "Din computer har vundet");
            frame.dispose();
            return true;
        } else if (!game.isMovesLeft()) {
            JOptionPane.showMessageDialog(frame, "Den blev uafgjort!");
            frame.dispose();
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        new TicTacToeGUI();
    }
}

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.*;

public class Game_TicTacToe_Exam extends JFrame implements ActionListener, FocusListener {
    JPanel hauptPanel, obenPanel;
    JTextField inputSize, inputNeededToWin;
    JButton addBtn, removeBtn;
    JButton[][] field;
    HashSet<Point> pointList;

    boolean[][] isPlayed;
    int size, neededNumberToWin;
    int player = 0;
    boolean isNotField;

    public Game_TicTacToe_Exam() {
        super("GAME TIC TAC TOE");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 500);
        this.setLocation(500, 500);

        this.obenPanel = this.createObenPanel();
        this.getContentPane().add(obenPanel, BorderLayout.NORTH);

        this.hauptPanel = new JPanel();
        this.getContentPane().add(this.hauptPanel, BorderLayout.CENTER);

        this.pointList = new HashSet<>();

        this.setVisible(true);
    }

    private JPanel createObenPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.PINK);

        this.inputSize = new JTextField("Size",10);
        this.inputSize.addFocusListener(this);
        this.inputNeededToWin = new JTextField("Needed Number To Win", 15);
        this.inputNeededToWin.addFocusListener(this);

        this.addBtn = new JButton("add");
        this.addBtn.addActionListener(this);
        this.removeBtn = new JButton("remove");
        this.removeBtn.addActionListener(this);


        panel.add(this.inputSize);
        panel.add(this.inputNeededToWin);
        panel.add(this.addBtn);
        panel.add(this.removeBtn);
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src instanceof JButton) {
            JButton srcBtn = (JButton) src;
            srcBtn.setEnabled(false);
            if (srcBtn == this.addBtn) {
                this.isNotField = false;
                this.neededNumberToWin = Integer.parseInt(this.inputNeededToWin.getText());
                this.size = Integer.parseInt(this.inputSize.getText());

                if(this.neededNumberToWin > this.size || this.neededNumberToWin < 3){
                    int dialogChange;
                    if(this.size < 3){
                        dialogChange = JOptionPane.showConfirmDialog(
                                null,
                                " Size should bigger than 3 ",
                                "Attention",
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                    else{
                        dialogChange = JOptionPane.showConfirmDialog(
                                null,
                                " Needed number to win must between 3 and " + this.size,
                                "Attention", JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.WARNING_MESSAGE
                        );
                    }

                    if (dialogChange == JOptionPane.OK_OPTION){
                        new Game_TicTacToe_Exam();
                        dispose();
                    }
                    else System.exit(0);
                }

                this.hauptPanel.setLayout(new GridLayout(this.size, this.size));

                field = new JButton[this.size][this.size];
                isPlayed = new boolean[this.size][this.size];
                for (int i = 0; i < this.size; i++) {
                    for (int j = 0; j < this.size; j++) {
                        field[i][j] = new JButton(" ");
                        field[i][j].setFont(new Font("Verdana", Font.BOLD, 20));
                        field[i][j].setActionCommand(i + " " + j);

                        this.hauptPanel.add(field[i][j]);
                        isPlayed[i][j] = true;
                        field[i][j].addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent actionEvent) {
                                String s = actionEvent.getActionCommand();
                                int k = s.indexOf(" ");
                                int i = Integer.parseInt(s.substring(0, k));
                                int j = Integer.parseInt(s.substring(k + 1, s.length()));

                                if (isPlayed[i][j]) {
                                    setPlayer(i, j);
                                }

                                if (checkWin(i, j)) {
                                    Iterator<Point> iter = pointList.iterator();
                                    System.out.println(pointList.size());

                                    while (iter.hasNext()) {

                                        Point next = iter.next();
                                        field[next.getRow()][next.getCol()].setOpaque(true);
                                        field[next.getRow()][next.getCol()].setBackground(Color.PINK);
                                    }

                                    JOptionPane pane = new JOptionPane();
                                    int dialogResult = JOptionPane.showConfirmDialog(
                                            pane,
                                            field[i][j].getText() + " wins. Would you like to play again?",
                                            "Game over.",
                                            JOptionPane.YES_NO_OPTION
                                    );

                                    if (dialogResult == JOptionPane.YES_OPTION){
                                        new Game_TicTacToe_Exam();
                                        dispose();
                                    }
                                    else System.exit(0);
                                }
                            }
                        });
                    }
                }
            } else if(srcBtn == this.removeBtn) {
                 if(!this.isNotField){
                    new Game_TicTacToe_Exam();
                    dispose();
                    this.isNotField = true;
                }

            }
            this.hauptPanel.revalidate();
            this.hauptPanel.repaint();
        }
    }

    @Override
    public void focusGained(FocusEvent focusEvent) {
        Object src = focusEvent.getSource();

        if (src instanceof JTextField) {
            JTextField srcBtn = (JTextField) src;

            if (srcBtn == this.inputSize) {
                this.inputSize.selectAll();
            }
            else  this.inputNeededToWin.selectAll();
        }
    }

    @Override
    public void focusLost(FocusEvent focusEvent) {}

    private boolean checkWin(int row, int col) {
        this.neededNumberToWin = Integer.parseInt(this.inputNeededToWin.getText());

        if (countToVictory( row, col, 1, 0 ) >= this.neededNumberToWin)
            return true;
        if (countToVictory(row, col, 0, 1 ) >= this.neededNumberToWin)
            return true;
        if (countToVictory(row, col, 1, -1 ) >= this.neededNumberToWin)
            return true;
        if (countToVictory( row, col, 1, 1 ) >= this.neededNumberToWin)
            return true;
        return false;
    }

    private int countToVictory(int row, int col, int i, int j) {
        pointList = new HashSet<>();
        int victory = 1, nextRow, nextColumn;

        nextRow = row + i;
        nextColumn = col + j;
        while ( nextRow >= 0 && nextRow < field.length
                && nextColumn >= 0 && nextColumn < field.length
                && field[nextRow][nextColumn].getText() == field[row][col].getText()
        ) {
            pointList.add(new Point(row, col));
            pointList.add(new Point(nextRow, nextColumn));

            victory++;
            nextRow += i;
            nextColumn += j;

        }

        nextRow = row - i;
        nextColumn = col - j;
        while ( nextRow >= 0 && nextRow < field.length
                && nextColumn >= 0 && nextColumn < field.length
                && field[nextRow][nextColumn].getText() == field[row][col].getText()
        ) {
            pointList.add(new Point(row, col));
            pointList.add(new Point(nextRow, nextColumn));
            victory++;
            nextRow -= i;
            nextColumn -= j;
        }

        return victory;
    }

    private void setPlayer(int i, int j) {
        if (player % 2 == 0) {
            field[i][j].setText("X");
            field[i][j].setForeground(Color.RED);
        }
        else {
            field[i][j].setText("O");
            field[i][j].setForeground(Color.BLUE);
        }
        isPlayed[i][j] = false;
        player = 1 - player;
    }

    public static void main(String[] args)
    {
            new Game_TicTacToe_Exam();
    }
}
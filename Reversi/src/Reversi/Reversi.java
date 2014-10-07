/*
Reversi
 
Reversi (Othello) is a game based on a grid with eight rows and eight columns, played between you and the computer, by adding pieces with two sides: black and white.
At the beginning of the game there are 4 pieces in the grid, the player with the black pieces is the first one to place his piece on the board.
Each player must place a piece in a position that there exists at least one straight (horizontal, vertical, or diagonal) line between the new piece and another piece of the same color, with one or more contiguous opposite pieces between them. 
 
Usage:  java Reversi
 
10-12-2006 version 0.1:     initial release
26-12-2006 version 0.15:    added support for applet
01-11-2007 version 0.16:    minor improvement in level handling
06-19-2014 version 1.0:     redesigned user interface
                            added undo and save/load features

 
Requirement: Java 1.5 or later
 
future features:
- logging of moves
- autoplay
- sound
 
This software is released under the GNU GENERAL PUBLIC LICENSE, see attached file gpl.txt
*/
package Reversi;

import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

/**
 * The source files are downloaded from http://trovami.altervista.org/ at Jun 16 2014
 * This software is redesigned hereafter.
 * @author Justin Hu
 */
public class Reversi extends javax.swing.JFrame {

    /**
     * Creates new form Reversi
     */
    static final int Square = 33; // length in pixel of a square in the grid
    static final int Width = 8 * Square; // Width of the game board
    static final int Height = 8 * Square;
    Board board = new Board();
    int gameLevel = 2;
    String gameTheme = "classic";
    Move hint = null;
    Piece current = Piece.black;
    boolean gameOn = false, moveAllowed = true;
    ImageIcon button_black = new ImageIcon(Reversi.class.getResource("button_black.jpg"));
    ImageIcon button_white = new ImageIcon(Reversi.class.getResource("button_white.jpg"));
    saveGame lastMove;

    public Reversi() {
        initComponents();
    }

    public void drawPanel(Graphics g) {
        for (int i = 1; i < 8; i++) {
            g.drawLine(i * Reversi.Square, 0, i * Reversi.Square, Reversi.Height);
        }
        g.drawLine(Reversi.Width, 0, Reversi.Width, Reversi.Height);
        for (int i = 1; i < 8; i++) {
            g.drawLine(0, i * Reversi.Square, Reversi.Width, i * Reversi.Square);
        }
        g.drawLine(0, Reversi.Height, Reversi.Width, Reversi.Height);
        if (gameOn) {
            drawPiece(g);
        }
    }

    public void drawPiece(Graphics g) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                switch (board.get(i, j)) {
                    case white:
                        if (gameTheme.equals("Flat")) {
                            g.setColor(Color.white);
                            g.fillOval(1 + i * Reversi.Square, 1 + j * Reversi.Square, Reversi.Square - 1, Reversi.Square - 1);
                        } else {
                            g.drawImage(button_white.getImage(), 1 + i * Reversi.Square, 1 + j * Reversi.Square, this);
                        }
                        break;
                    case black:
                        if (gameTheme.equals("Flat")) {
                            g.setColor(Color.black);
                            g.fillOval(1 + i * Reversi.Square, 1 + j * Reversi.Square, Reversi.Square - 1, Reversi.Square - 1);
                        } else {
                            g.drawImage(button_black.getImage(), 1 + i * Reversi.Square, 1 + j * Reversi.Square, this);
                        }
                        break;
                }
            }
        }
        if (hint != null) {
            g.setColor(Color.darkGray);
            g.drawOval(hint.i * Reversi.Square + 3, hint.j * Reversi.Square + 3, Reversi.Square - 6, Reversi.Square - 6);
        }

    }

    /*public Dimension getPreferredSize() {
     return new Dimension(Reversi.Width, Reversi.Height);
     }*/
    public void showWinner() {
        moveAllowed = false;
        gameOn = false;
        if (board.counter[0] > board.counter[1]) {
            JOptionPane.showMessageDialog(this, "Black win!", "Reversi", JOptionPane.INFORMATION_MESSAGE);
        } else if (board.counter[0] < board.counter[1]) {
            JOptionPane.showMessageDialog(this, "White win!", "Reversi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Drawn!", "Reversi", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void setHint(Move h) {
        hint = h;
    }

    public void clear() {
        board.clear();
        Score_B.setText(Integer.toString(board.getCounter(Piece.black)));
        Score_W.setText(Integer.toString(board.getCounter(Piece.white)));
        moveAllowed = true;
        gameOn = true;
    }

    public void computerMove() {
        if (board.gameEnd()) {
            showWinner();
            return;
        }
        Move move = new Move();
        if (board.findMove(Piece.white, gameLevel, move)) {
            board.move(move, Piece.white);
            Score_B.setText(Integer.toString(board.getCounter(Piece.black)));
            Score_W.setText(Integer.toString(board.getCounter(Piece.white)));
            repaint();
            if (board.gameEnd()) {
                showWinner();
            } else if (!board.userCanMove(Piece.black)) {
                JOptionPane.showMessageDialog(this, "You pass...", "Reversi", JOptionPane.INFORMATION_MESSAGE);
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        computerMove();
                    }
                });
            }
        } else if (board.userCanMove(Piece.black)) {
            JOptionPane.showMessageDialog(this, "I pass...", "Reversi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            showWinner();
        }
    }
    public void gameLevelAdjust(){
        switch(gameLevel){
            case 2:
                Level1.setSelected(true);
                break;
            case 3:
                Level2.setSelected(true);
                break;
            case 4:
                Level3.setSelected(true);
                break;
            case 5:
                Level4.setSelected(true);
                break;
            case 6:
                Level5.setSelected(true);
                break;
        }
    
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LevelButtons = new javax.swing.ButtonGroup();
        ThemeButtons = new javax.swing.ButtonGroup();
        OpenFC = new javax.swing.JFileChooser();
        SaveFC = new javax.swing.JFileChooser();
        Score_B = new javax.swing.JLabel();
        Score_W = new javax.swing.JLabel();
        BoardPanel = new javax.swing.JPanel(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawPanel(g);
            }
        };
        LevelLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        GameMenu = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        NewHuman = new javax.swing.JMenuItem();
        NewAI = new javax.swing.JMenuItem();
        SaveMenu = new javax.swing.JMenuItem();
        LoadMenu = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        Undo = new javax.swing.JMenuItem();
        Hint = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        Quit = new javax.swing.JMenuItem();
        LevelMenu = new javax.swing.JMenu();
        Level1 = new javax.swing.JRadioButtonMenuItem();
        Level2 = new javax.swing.JRadioButtonMenuItem();
        Level3 = new javax.swing.JRadioButtonMenuItem();
        Level4 = new javax.swing.JRadioButtonMenuItem();
        Level5 = new javax.swing.JRadioButtonMenuItem();
        ThemeMenu = new javax.swing.JMenu();
        ClassicTheme = new javax.swing.JRadioButtonMenuItem();
        ModernTheme = new javax.swing.JRadioButtonMenuItem();
        FlatTheme = new javax.swing.JRadioButtonMenuItem();
        HelpMenu = new javax.swing.JMenu();
        Help = new javax.swing.JMenuItem();
        About = new javax.swing.JMenuItem();

        SaveFC.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        SaveFC.setApproveButtonText("Save");
        String fileName = "Reversi_" + new SimpleDateFormat("yyyyMMddhhmm'.rs'").format(new Date());
        SaveFC.setSelectedFile(new java.io.File(fileName));
        SaveFC.setDragEnabled(true);
        SaveFC.setRequestFocusEnabled(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Reversi");
        setResizable(false);

        Score_B.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        Score_B.setText("Score_B");

        Score_W.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        Score_W.setForeground(new java.awt.Color(255, 255, 255));
        Score_W.setText("Score_W");

        BoardPanel.setBackground(java.awt.Color.green);
        BoardPanel.setMaximumSize(new java.awt.Dimension(265, 265));
        BoardPanel.setMinimumSize(new java.awt.Dimension(264, 264));
        BoardPanel.setName(""); // NOI18N
        BoardPanel.setPreferredSize(new java.awt.Dimension(265, 265));
        BoardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BoardPanelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout BoardPanelLayout = new javax.swing.GroupLayout(BoardPanel);
        BoardPanel.setLayout(BoardPanelLayout);
        BoardPanelLayout.setHorizontalGroup(
            BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        BoardPanelLayout.setVerticalGroup(
            BoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 264, Short.MAX_VALUE)
        );

        LevelLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LevelLabel.setText("Welcome");
        LevelLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        LevelLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jMenuBar1.setBackground(new java.awt.Color(255, 255, 255));
        jMenuBar1.setForeground(new java.awt.Color(255, 255, 255));
        jMenuBar1.setDoubleBuffered(true);

        GameMenu.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        GameMenu.setText("Game");
        GameMenu.setBorderPainted(true);

        jMenu1.setText("New");

        NewHuman.setText("Human");
        NewHuman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewHumanActionPerformed(evt);
            }
        });
        jMenu1.add(NewHuman);

        NewAI.setText("AI");
        NewAI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewAIActionPerformed(evt);
            }
        });
        jMenu1.add(NewAI);

        GameMenu.add(jMenu1);

        SaveMenu.setText("Save");
        SaveMenu.setEnabled(false);
        SaveMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveMenuActionPerformed(evt);
            }
        });
        GameMenu.add(SaveMenu);

        LoadMenu.setText("Load");
        LoadMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoadMenuActionPerformed(evt);
            }
        });
        GameMenu.add(LoadMenu);
        GameMenu.add(jSeparator1);

        Undo.setText("Undo");
        Undo.setEnabled(false);
        Undo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UndoActionPerformed(evt);
            }
        });
        GameMenu.add(Undo);

        Hint.setText("Hint");
        Hint.setEnabled(false);
        Hint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HintActionPerformed(evt);
            }
        });
        GameMenu.add(Hint);
        GameMenu.add(jSeparator2);

        Quit.setText("Quit");
        Quit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QuitActionPerformed(evt);
            }
        });
        GameMenu.add(Quit);

        jMenuBar1.add(GameMenu);

        LevelMenu.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        LevelMenu.setText("Level");
        LevelMenu.setBorderPainted(true);
        LevelMenu.setEnabled(false);
        LevelMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LevelMenuActionPerformed(evt);
            }
        });

        LevelButtons.add(Level1);
        Level1.setSelected(true);
        Level1.setText("1");
        Level1.setFocusPainted(true);
        Level1.setFocusable(true);
        Level1.setRolloverEnabled(true);
        Level1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Level1ActionPerformed(evt);
            }
        });
        LevelMenu.add(Level1);

        LevelButtons.add(Level2);
        Level2.setText("2");
        Level2.setFocusPainted(true);
        Level2.setFocusable(true);
        Level2.setRolloverEnabled(true);
        Level2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Level2ActionPerformed(evt);
            }
        });
        LevelMenu.add(Level2);

        LevelButtons.add(Level3);
        Level3.setText("3");
        Level3.setFocusPainted(true);
        Level3.setFocusable(true);
        Level3.setRolloverEnabled(true);
        Level3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Level3ActionPerformed(evt);
            }
        });
        LevelMenu.add(Level3);

        LevelButtons.add(Level4);
        Level4.setText("4");
        Level4.setFocusPainted(true);
        Level4.setFocusable(true);
        Level4.setRolloverEnabled(true);
        Level4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Level4ActionPerformed(evt);
            }
        });
        LevelMenu.add(Level4);

        LevelButtons.add(Level5);
        Level5.setText("5");
        Level5.setFocusPainted(true);
        Level5.setFocusable(true);
        Level5.setRolloverEnabled(true);
        Level5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Level5ActionPerformed(evt);
            }
        });
        LevelMenu.add(Level5);

        jMenuBar1.add(LevelMenu);

        ThemeMenu.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ThemeMenu.setText("Theme");
        ThemeMenu.setBorderPainted(true);

        ThemeButtons.add(ClassicTheme);
        ClassicTheme.setSelected(true);
        ClassicTheme.setText("Classic");
        ClassicTheme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClassicThemeActionPerformed(evt);
            }
        });
        ThemeMenu.add(ClassicTheme);

        ThemeButtons.add(ModernTheme);
        ModernTheme.setText("Modern");
        ModernTheme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModernThemeActionPerformed(evt);
            }
        });
        ThemeMenu.add(ModernTheme);

        ThemeButtons.add(FlatTheme);
        FlatTheme.setText("Flat");
        FlatTheme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FlatThemeActionPerformed(evt);
            }
        });
        ThemeMenu.add(FlatTheme);

        jMenuBar1.add(ThemeMenu);

        HelpMenu.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        HelpMenu.setText("Help");
        HelpMenu.setBorderPainted(true);

        Help.setText("Help");
        Help.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HelpActionPerformed(evt);
            }
        });
        HelpMenu.add(Help);

        About.setText("About");
        About.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AboutActionPerformed(evt);
            }
        });
        HelpMenu.add(About);

        jMenuBar1.add(HelpMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Score_B)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LevelLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Score_W)
                .addContainerGap())
            .addComponent(BoardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(BoardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Score_B)
                    .addComponent(Score_W)
                    .addComponent(LevelLabel)))
        );

        BoardPanel.getAccessibleContext().setAccessibleName("");

        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents
// <editor-fold defaultstate="collapsed" desc="actionlistener">   
    private void NewAIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewAIActionPerformed
        gameOn = true;
        Hint.setEnabled(true);
        LevelMenu.setEnabled(true);
        SaveMenu.setEnabled(true);
        gameLevel = 2;
        LevelLabel.setText("Level:" + Integer.toString(gameLevel - 1));
        current = Piece.black;
        clear();
        lastMove = new saveGame(board, gameLevel, current);
        gameLevelAdjust();
        repaint();

    }//GEN-LAST:event_NewAIActionPerformed

    private void HintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HintActionPerformed
        if (gameOn && gameLevel != 0) {
            Move move = new Move();
            if (board.findMove(Piece.black, gameLevel, move)) {
                setHint(move);
            }
            repaint();
        } else {
            Hint.setEnabled(false);
        }
    }//GEN-LAST:event_HintActionPerformed

    private void UndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UndoActionPerformed
        System.out.println(board.getCounter(Piece.black));
        System.out.println(lastMove.saved.getCounter(Piece.black));
        board.copyBoard(lastMove.saved);
        Score_B.setText(Integer.toString(board.getCounter(Piece.black)));
        Score_W.setText(Integer.toString(board.getCounter(Piece.white)));
        current = lastMove.currentPlayer;
        Undo.setEnabled(false);
        repaint();

    }//GEN-LAST:event_UndoActionPerformed

    private void ClassicThemeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClassicThemeActionPerformed
        BoardPanel.setBackground(Color.green);
        button_black = new ImageIcon(Reversi.class.getResource("button_black.jpg"));
        button_white = new ImageIcon(Reversi.class.getResource("button_white.jpg"));
        gameTheme = "classic";
        repaint();
    }//GEN-LAST:event_ClassicThemeActionPerformed

    private void ModernThemeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModernThemeActionPerformed
        BoardPanel.setBackground(Color.white);
        button_black = new ImageIcon(Reversi.class.getResource("button_blu.jpg"));
        button_white = new ImageIcon(Reversi.class.getResource("button_red.jpg"));
        gameTheme = "Modern";
        repaint();
    }//GEN-LAST:event_ModernThemeActionPerformed

    private void QuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QuitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_QuitActionPerformed

    private void HelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HelpActionPerformed
        new Help().setVisible(true);
    }//GEN-LAST:event_HelpActionPerformed

    private void AboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AboutActionPerformed
        new About(this, true).setVisible(true);
    }//GEN-LAST:event_AboutActionPerformed

    private void Level1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Level1ActionPerformed
        gameLevel = 2;
        LevelLabel.setText("Level:" + Integer.toString(gameLevel - 1));
    }//GEN-LAST:event_Level1ActionPerformed

    private void Level2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Level2ActionPerformed
        gameLevel = 3;
        LevelLabel.setText("Level:" + Integer.toString(gameLevel - 1));
    }//GEN-LAST:event_Level2ActionPerformed

    private void Level3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Level3ActionPerformed
        gameLevel = 4;
        LevelLabel.setText("Level:" + Integer.toString(gameLevel - 1));
    }//GEN-LAST:event_Level3ActionPerformed

    private void Level4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Level4ActionPerformed
        gameLevel = 5;
        LevelLabel.setText("Level:" + Integer.toString(gameLevel - 1));
    }//GEN-LAST:event_Level4ActionPerformed

    private void Level5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Level5ActionPerformed
        gameLevel = 6;
        LevelLabel.setText("Level:" + Integer.toString(gameLevel - 1));
    }//GEN-LAST:event_Level5ActionPerformed

    private void BoardPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BoardPanelMouseClicked
        if (moveAllowed) {
            hint = null;
            int i = evt.getX() / Reversi.Square;
            int j = evt.getY() / Reversi.Square;
            lastMove.update(board, gameLevel, current);
            Undo.setEnabled(true);
            if ((i < 8) && (j < 8) && (board.get(i, j) == Piece.none) && (board.move(new Move(i, j), current) != 0)) {
                Score_B.setText(Integer.toString(board.getCounter(Piece.black)));
                Score_W.setText(Integer.toString(board.getCounter(Piece.white)));
                repaint();
                if (gameLevel != 0) {
                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            Cursor savedCursor = getCursor();
                            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                            computerMove();
                            setCursor(savedCursor);
                        }
                    });
                } else {
                    if (current == Piece.black) {
                        current = Piece.white;
                        LevelLabel.setText("White's Move");
                    } else {
                        current = Piece.black;
                        LevelLabel.setText("Black's Move");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Illegal move", "Reversi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_BoardPanelMouseClicked

    private void FlatThemeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FlatThemeActionPerformed
        BoardPanel.setBackground(Color.green);
        gameTheme = "Flat";
        repaint();
    }//GEN-LAST:event_FlatThemeActionPerformed

    private void LevelMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LevelMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LevelMenuActionPerformed

    private void NewHumanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewHumanActionPerformed
        gameOn = true;
        Hint.setEnabled(false);
        LevelMenu.setEnabled(false);
        SaveMenu.setEnabled(true);
        gameLevel = 0;
        LevelLabel.setText("Black's Move");
        current = Piece.black;
        clear();
        lastMove = new saveGame(board, gameLevel, current);
        repaint();
    }//GEN-LAST:event_NewHumanActionPerformed

    private void SaveMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveMenuActionPerformed
        int returnVal = SaveFC.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try (FileWriter fw = new FileWriter(SaveFC.getSelectedFile())) {
                board.printBoard(fw);
                fw.write(current.toString() + "\n");
                fw.write(Integer.toString(gameLevel) + "\n");
            } catch (Exception ex) {
            }
        } else {
            System.out.println("File access cancelled by user.");
        }
    }//GEN-LAST:event_SaveMenuActionPerformed

    private void LoadMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoadMenuActionPerformed
        int returnVal = OpenFC.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader br = new BufferedReader(new FileReader(OpenFC.getSelectedFile()))) {
                clear();
                board.readBoard(br);
                String piece = br.readLine();
                if (piece == "black") {
                    current = Piece.black;
                } else if (piece == "white") {
                    current = Piece.white;
                }
                gameLevel = Integer.parseInt(br.readLine());
            } catch (Exception ex) {
            }
            if(gameLevel!=0){
                Hint.setEnabled(true);
                LevelMenu.setEnabled(true);
                SaveMenu.setEnabled(true);
                LevelLabel.setText("Level:" + Integer.toString(gameLevel - 1));
            }else{
                Hint.setEnabled(false);
                LevelMenu.setEnabled(false);
                SaveMenu.setEnabled(true);
                if (current == Piece.black) {
                   LevelLabel.setText("Black's Move");
                } else {
                    LevelLabel.setText("White's Move");
                }
            }
            lastMove = new saveGame(board, gameLevel, current);
                    Score_B.setText(Integer.toString(board.getCounter(Piece.black)));
        Score_W.setText(Integer.toString(board.getCounter(Piece.white)));
        gameLevelAdjust();
        
            repaint();
        } else {
            System.out.println("File access cancelled by user.");
        }
    }//GEN-LAST:event_LoadMenuActionPerformed
// </editor-fold>   

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Reversi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Reversi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Reversi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Reversi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Reversi().setVisible(true);
            }
        });
    }
//<editor-fold defaultstate="collapsed" desc=" Variables declaration ">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem About;
    private javax.swing.JPanel BoardPanel;
    private javax.swing.JRadioButtonMenuItem ClassicTheme;
    private javax.swing.JRadioButtonMenuItem FlatTheme;
    private javax.swing.JMenu GameMenu;
    private javax.swing.JMenuItem Help;
    private javax.swing.JMenu HelpMenu;
    private javax.swing.JMenuItem Hint;
    private javax.swing.JRadioButtonMenuItem Level1;
    private javax.swing.JRadioButtonMenuItem Level2;
    private javax.swing.JRadioButtonMenuItem Level3;
    private javax.swing.JRadioButtonMenuItem Level4;
    private javax.swing.JRadioButtonMenuItem Level5;
    private javax.swing.ButtonGroup LevelButtons;
    private javax.swing.JLabel LevelLabel;
    private javax.swing.JMenu LevelMenu;
    private javax.swing.JMenuItem LoadMenu;
    private javax.swing.JRadioButtonMenuItem ModernTheme;
    private javax.swing.JMenuItem NewAI;
    private javax.swing.JMenuItem NewHuman;
    private javax.swing.JFileChooser OpenFC;
    private javax.swing.JMenuItem Quit;
    private javax.swing.JFileChooser SaveFC;
    private javax.swing.JMenuItem SaveMenu;
    private javax.swing.JLabel Score_B;
    private javax.swing.JLabel Score_W;
    private javax.swing.ButtonGroup ThemeButtons;
    private javax.swing.JMenu ThemeMenu;
    private javax.swing.JMenuItem Undo;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    // End of variables declaration//GEN-END:variables
//</editor-fold>
}

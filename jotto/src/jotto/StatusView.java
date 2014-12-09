/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jotto;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Justin Hu
 */
public class StatusView extends JPanel implements Observer {

    private final JottoModel m;
    //private JLabel level;
    private final JLabel guesses;
    //private JLabel word;

    StatusView(JottoModel model) {
        setBorder(BorderFactory.createTitledBorder(null, "Status", TitledBorder.LEADING, TitledBorder.TOP, new Font("SansSerif", Font.PLAIN, 12), Color.GRAY));
        //setBorder(BorderFactory.createTitledBorder("Status"));
        setBackground(Color.WHITE);

        // set the model
        m = model;
        setLayout(new FlowLayout(FlowLayout.CENTER));

        //word = new JLabel(m.getWord().getWord());
        //word.setMaximumSize(new Dimension(100, 50));
        //word.setPreferredSize(new Dimension(100, 50));
        //word.setBorder(new LineBorder(Color.yellow));
        //this.add(word);
        guesses = new JLabel("Guesses Left: " + Integer.toString(m.getGuesses()));
        guesses.setFont(new Font("SansSerif", Font.PLAIN, 20));

        this.add(guesses);

        /*// setup the event to go to the "controller"
         // (this anonymous class is essentially the controller)		
         addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
					
         }
         });*/
    }

    @Override
    public void update(Observable o, Object o1) {
        //word.setText(m.getWord().getWord());
        if (m.isWin()) {
            guesses.setText("CONGRATS! YOU WIN");
        } else if (m.getGuesses() == 0) {
            guesses.setText("Your word is " + m.getWord().getWord() + ".  Good luck next time");
        } else if (m.getGuesses() < 0) {
            guesses.setText("Welcome! Please start a new game");
        } else {
            guesses.setText("Guesses Left: " + Integer.toString(m.getGuesses()));
        }
        repaint();
    }

}

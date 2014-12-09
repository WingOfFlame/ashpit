/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jotto;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Justin Hu
 */
public class LetterView extends JPanel implements Observer {

    private final JottoModel m;
    private final JButton letters[] = new JButton[26];
    private LineBorder gray;
    private LineBorder red;
    private LineBorder green;

    LetterView(JottoModel model) {
        m = model;
        //setBorder(BorderFactory.createTitledBorder("Letters"));
        setBorder(BorderFactory.createTitledBorder(null, "Letters", TitledBorder.LEADING, TitledBorder.TOP, new Font("SansSerif", Font.PLAIN, 12), Color.GRAY));
        setBackground(Color.WHITE);
        setLayout(new GridLayout(2, 13, 5, 5));
        gray = new LineBorder(Color.LIGHT_GRAY, 2);
        red = new LineBorder(Color.red, 2);
        green = new LineBorder(Color.green, 2);
        //this.setMaximumSize(600,50);
        for (int i = 0; i < 26; i++) {
            char a = (char) ('A' + i);
            final JButton letter = new JButton(Character.toString(a));
            letter.setHorizontalAlignment(SwingConstants.CENTER);
            letter.setVerticalAlignment(SwingConstants.CENTER);
            letter.setPreferredSize(new Dimension(40, 30));
            letter.setBackground(Color.WHITE);
            letter.setMargin(new Insets(0, 0, 0, 0));
            letter.setBorder(gray);
            letter.setEnabled(false);
            letters[i] = letter;
            letter.setFont(new Font("SansSerif", Font.PLAIN, 15));
            letter.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //System.out.println("clicked");
                    if (letter.isEnabled()) {
                        if (letter.getBorder() == gray) {
                            letter.setBorder(green);
                        } else if (letter.getBorder() == green) {
                            letter.setBorder(red);
                        } else if (letter.getBorder() == red) {
                            letter.setBorder(gray);
                        }
                    }
                }
            });
            this.add(letter);
        }

    }

    @Override
    public void update(Observable o, Object o1) {
        for (int i = 0; i < 26; i++) {
            if (m.letterGuessed(i)) {
                letters[i].setEnabled(true);
            } else {
                letters[i].setEnabled(false);
            }
        }
        if (m.getGuesses() == 10) {
            for (JButton letter : letters) {
                letter.setBorder(gray);
            }
        }
    }

}

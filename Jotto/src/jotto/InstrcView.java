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
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Justin Hu
 */
public class InstrcView extends JPanel {

    //private JottoModel m;
    private final JTextArea instruction;
    private final JScrollPane scroll;

    InstrcView() {
        //m = model;
        //setBorder(BorderFactory.createTitledBorder("Instruction"));
        setBorder(BorderFactory.createTitledBorder(null, "Instruction", TitledBorder.LEADING, TitledBorder.TOP, new Font("SansSerif", Font.PLAIN, 12), Color.GRAY));

        setLayout(new GridLayout(1, 0));
        setPreferredSize(new Dimension(200, 150));

        instruction = new JTextArea("OBJECTIVE: \n" + ""
                + "- Try to guess the five-letter target.\n\n"
                + "RULES: \n"
                + "- Guesses must be in a dictionary\n"
                + "- must not be a proper noun (a name), and must not be a contraction (like “isn't”).\n"
                + "- It may contain repeated characters.\n\n"
                + "GAMEPLAY: \n"
                + "- Start a new game by first selecting desired difficulty or by setting the target word, and press 'New Game' \n"
                + "- When you guess a valid word, computer will indicate two values: the number of exact matches and the number of partial matches.\n"
                + "- You have at most 10 guesses.\n"
                + "- You can use the letters panel to keep track of all the possible letters. "
                + "You can mark any guessed letter to be green or red by clicking on it.\n"
                + "- On the right-hand side, you can search in the dictionary of all words that match your input letters at corresponding position\n\n");
        instruction.setLineWrap(true);
        instruction.setWrapStyleWord(true);
        instruction.setEditable(false);
        instruction.setFont(new Font("SansSerif", Font.PLAIN, 12));

        scroll = new JScrollPane(instruction);
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBackground(Color.white);
        // scroll.setBorder(BorderFactory.createLineBorder(Color.black));
        scroll.setPreferredSize(new Dimension(250, 180));
        scroll.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
        this.add(scroll);

    }

}

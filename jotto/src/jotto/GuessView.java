/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jotto;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author Justin Hu
 */
public class GuessView extends JPanel implements Observer {

    private JottoModel m;
    private JTextField input;
    private JButton guess = new JButton("GUESS");
    //private JLabel msg;

    GuessView(JottoModel model) {
        UIManager.put("Button.disabledText", new ColorUIResource(Color.LIGHT_GRAY));
        m = model;
        // create UI
        //setBackground(Color.WHITE);
        setLayout(new FlowLayout(FlowLayout.CENTER));
        //setBorder(BorderFactory.createTitledBorder("Guess"));
        setBorder(BorderFactory.createTitledBorder(null, "Guess", TitledBorder.LEADING, TitledBorder.TOP, new Font("SansSerif", Font.PLAIN, 12), Color.GRAY));

        setBackground(Color.WHITE);

        //this.setMaximumSize(600,50);
        input = new JTextField();
        input.setDocument(new JTextFormat(5));
        input.setPreferredSize(new Dimension(80, 30));
        input.setFont(new Font("SansSerif", Font.BOLD, 20));
        input.setForeground(Color.black);
        input.setEnabled(false);
        //input.setBackground(Color.lightGray);
        //input.setBackground(Color.black);
        //input.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.LIGHT_GRAY, Color.DARK_GRAY));
        //input.setHorizontalAlignment(JTextField.CENTER);
        //input.setText("");

        guess.setPreferredSize(new Dimension(80, 30));
        guess.setBackground(Color.WHITE);
        guess.setEnabled(false);
        guess.setMargin(new Insets(2, 2, 2, 2));
        guess.setFont(new Font("SansSerif", Font.PLAIN, 15));
        //guess.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED,Color.WHITE, Color.black));
        input.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent de) {

                if (input.getText().length() == 5 && input.getText().matches("[a-zA-Z]+")) {
                    if (m.repeatedGuess(input.getText())) {
                        guess.setText("REPEATED");
                        UIManager.put("Button.disabledText", new ColorUIResource(Color.red));
                        //guess.setForeground(Color.PINK);
                    } else if (m.checkGuesses(input.getText())) {
                        guess.setEnabled(true);
                        guess.setBackground(Color.WHITE);
                        UIManager.put("Button.disabledText", new ColorUIResource(Color.LIGHT_GRAY));
                        //guess.setForeground(Color.BLACK);
                    } else {
                        guess.setText("INVALID");
                        UIManager.put("Button.disabledText", new ColorUIResource(Color.red));
                        //guess.setForeground(Color.PINK);
                    }

                } else {
                    guess.setEnabled(false);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent de) {

                check();
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                //guess.setText("GUESS");
                check();
            }

            public void check() {
                guess.setText("GUESS");
                UIManager.put("Button.disabledText", new ColorUIResource(Color.LIGHT_GRAY));
                if (input.getText().length() != 5 || m.getGuesses() <= 0) {
                    guess.setEnabled(false);
                    guess.setBackground(Color.WHITE);
                } else {
                    guess.setEnabled(true);
                    guess.setBackground(Color.WHITE);
                }
            }
        });

        input.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent fe) {
                //input.set
                //System.out.println("text is:"+input.getText()+".");
                input.selectAll();//To change body of generated methods, choose Tools | Templates.
                //input.setCaret(new BasicTextUI.BasicCaret());
            }

            @Override
            public void focusLost(FocusEvent fe) {
                //System.out.println(input.getValue());
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        input.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && guess.isEnabled()) {
                    m.guessWord(input.getText());
                    //input.setValue(null);
                    input.setText("");
                }
            }
        });

        this.add(input);

        guess.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (guess.isEnabled()) {
                    m.guessWord(input.getText());
                    //input.setValue(null);
                    input.setText("");
                }

            }
        });

        this.add(guess);

        //msg = new JLabel();
        //this.add(msg);
    }

    @Override
    public void update(Observable o, Object o1) {

        if (m.getGuesses() > 0) {
            input.setEnabled(true);
            input.setText("");
            input.setBackground(Color.WHITE);
        } else {
            input.setEnabled(false);
            guess.setEnabled(false);
            //input.setBackground(Color.lightGray);

        }
        repaint();//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private MaskFormatter createFormatter(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
        } catch (java.text.ParseException exc) {
            System.err.println("formatter is bad: " + exc.getMessage());
            System.exit(-1);
        }
        return formatter;
    }

}

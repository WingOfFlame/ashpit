/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jotto;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.ColorUIResource;

/**
 *
 * @author Justin Hu
 */
public class NewGameView extends JPanel implements Observer {

    private JottoModel m;
    private final JButton newGame, endGame;
    private JComboBox level;
    private JTextField target;
    private final JRadioButton setLevel, setTarget;
    private final ButtonGroup option;

    NewGameView(JottoModel model) {
        m = model;
        // create UI
        //setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder("NewGame"));
        setBorder(BorderFactory.createTitledBorder(null, "NewGame", TitledBorder.LEADING, TitledBorder.TOP, new Font("SansSerif", Font.PLAIN, 12), Color.GRAY));

        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setMaximumSize(new Dimension(200, 150));

        this.add(Box.createVerticalGlue());
        //this.setMaximumSize(600,50);

        option = new ButtonGroup();

        JPanel games = new JPanel();
        games.setBackground(Color.white);
        newGame = new JButton("New Game");
        newGame.setMaximumSize(new Dimension(100, 25));
        newGame.setPreferredSize(new Dimension(100, 30));
        newGame.setFont(new Font("SansSerif", Font.PLAIN, 15));
        //newGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGame.setBackground(Color.WHITE);
        newGame.setMargin(new Insets(0, 0, 0, 0));
        games.add(newGame);
        // (this anonymous class is essentially the controller)
        newGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //System.out.println("clicked");
                if (setLevel.isSelected()) {
                    m.newWord(level.getSelectedIndex());
                } else if (target.getText().length() == 5 && target.getText().matches("[a-zA-Z]+")) {
                    m.setWord(target.getText().toUpperCase());
                    target.setText(null);
                    setLevel.doClick();
                    //setLevel.setSelected(true);
                    //level.setEnabled(true);
                }
            }
        });

        endGame = new JButton("End Game");
        endGame.setEnabled(false);
        endGame.setMaximumSize(new Dimension(100, 25));
        endGame.setPreferredSize(new Dimension(100, 30));
        //endGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        endGame.setBackground(Color.WHITE);
        endGame.setMargin(new Insets(0, 0, 0, 0));
        endGame.setFont(new Font("SansSerif", Font.PLAIN, 15));
        games.add(endGame);
        // (this anonymous class is essentially the controller)
        endGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //System.out.println("clicked");
                if (endGame.isEnabled()) {
                    UIManager.put("Button.disabledText", new ColorUIResource(Color.LIGHT_GRAY));
                    m.end();
                }
                //setLevel.setSelected(true);
                //level.setEnabled(true);
            }
        });

        this.add(games);
        this.add(Box.createVerticalGlue());

        setLevel = new JRadioButton("Difficulty:        ");
        setLevel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        setLevel.setBackground(Color.white);
        setLevel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                level.setEnabled(true);
                target.setEnabled(false);

            }
        });
        //this.add(setLevel);

        level = new JComboBox(model.getLevels());
        level.setMaximumSize(new Dimension(100, 25));
        level.setPreferredSize(new Dimension(100, 25));
        level.setBackground(Color.white);
        //level.setAlignmentX(Component.CENTER_ALIGNMENT);
        //this.add(level);
        level.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                //System.out.println(level.getSelectedItem());

            }
        });
        JPanel one = new JPanel();
        one.add(Box.createHorizontalGlue());
        one.add(setLevel);
        one.add(level);
        one.add(Box.createHorizontalGlue());
        this.add(one);

        this.add(Box.createVerticalGlue());
        setTarget = new JRadioButton("Target Word: ");
        setTarget.setBackground(Color.white);
        setTarget.setFont(new Font("SansSerif", Font.PLAIN, 15));
        setTarget.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                target.setEnabled(true);
                level.setEnabled(false);

            }
        });
        //this.add(setTarget);
        target = new JTextField();
        target.setDocument(new JTextFormat(5));
        target.setMaximumSize(new Dimension(100, 25));
        target.setPreferredSize(new Dimension(100, 25));
        //target.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, WIDTH, true));
        //this.add(target);

        option.add(setLevel);
        option.add(setTarget);
        setLevel.setSelected(true);
        target.setEnabled(false);

        JPanel two = new JPanel();
        two.add(Box.createHorizontalGlue());
        two.add(setTarget);
        two.add(target);
        two.add(Box.createHorizontalGlue());
        this.add(two);
        one.setBackground(Color.white);
        two.setBackground(Color.white);
        one.setLayout(new BoxLayout(one, BoxLayout.X_AXIS));
        two.setLayout(new BoxLayout(two, BoxLayout.X_AXIS));

        //one.setBorder(new LineBorder(Color.black));
        setTarget.setAlignmentX(Component.LEFT_ALIGNMENT);
        setLevel.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(Box.createVerticalGlue());

    }

    @Override
    public void update(Observable o, Object o1) {
        if (m.getGuesses() > 0) {
            endGame.setEnabled(true);
            newGame.setEnabled(false);
        } else {
            endGame.setEnabled(false);
            newGame.setEnabled(true);
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

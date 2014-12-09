/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jotto;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Justin Hu
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        try {
//            UIManager.setLookAndFeel(
//                    UIManager.getSystemLookAndFeelClassName());
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (UnsupportedLookAndFeelException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }

        JFrame frame = new JFrame("Jotto");
        JPanel root = new JPanel();

        // create the window
        JPanel p = new JPanel();
        frame.getContentPane().add(root);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        // create Model and initialize it
        JottoModel model = new JottoModel();

        NewGameView ng = new NewGameView(model);
        model.addObserver(ng);

        StatusView sv = new StatusView(model);
        model.addObserver(sv);

        LetterView lv = new LetterView(model);
        model.addObserver(lv);

        GuessView gv = new GuessView(model);
        model.addObserver(gv);

        HistoryView hv = new HistoryView(model);
        model.addObserver(hv);

        InstrcView iv = new InstrcView();
        iv.setBackground(Color.WHITE);

        SearchView sr = new SearchView(model);
        model.addObserver(sr);

        model.notifyObservers();

        JPanel north = new JPanel();
        //north.setBorder(BorderFactory.createLineBorder(Color.green));
        north.setLayout(new GridLayout(1, 2));
        north.setPreferredSize(new Dimension(700, 150));
        north.add(iv);
        north.add(ng);

        JPanel east = new JPanel();
        //east.setBorder(BorderFactory.createLineBorder(Color.cyan));

//        JPanel west = new JPanel();
//        west.setBorder(BorderFactory.createLineBorder(Color.white));
        JPanel south = new JPanel();
        //south.setBorder(BorderFactory.createLineBorder(Color.red));
        south.setLayout(new BorderLayout());
        south.add(gv, BorderLayout.PAGE_START);
        south.add(hv, BorderLayout.CENTER);

        JPanel center = new JPanel();

        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(sv);
        center.add(lv);
//        center.add(hv);

        p.add(north);
        //p.add(east, BorderLayout.EAST);
        //p.add(west,BorderLayout.WEST);

        p.add(center);
        p.add(south);
        //p.setPreferredSize(new Dimension(500, 650));
        //p.setMaximumSize(new Dimension(600, 650));
        root.setLayout(new BoxLayout(root, BoxLayout.X_AXIS));
        root.add(p);
        root.setBackground(Color.white);

        root.add(sr);

        //p.add(view2);
        frame.setPreferredSize(new Dimension(840, 650));
        frame.setMinimumSize(new Dimension(800, 550));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}

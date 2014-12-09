/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jotto;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Justin Hu
 */
public class HistoryView extends JPanel implements Observer {

    private final JottoModel m;
    private final JTable history;
    private final JScrollPane scrollPane;

    HistoryView(JottoModel model) {
        m = model;
        //setBorder(BorderFactory.createTitledBorder("History"));
        setBorder(BorderFactory.createTitledBorder(null, "History", TitledBorder.LEADING, TitledBorder.TOP, new Font("SansSerif", Font.PLAIN, 12), Color.GRAY));
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        history = new JTable(m.getHistoryModel());
        history.setFillsViewportHeight(true);
        history.getColumnModel().getColumn(0).setPreferredWidth(300);
        history.setRowHeight(24);
        history.setFont(new Font("SansSerif", Font.PLAIN, 15));

        scrollPane = new JScrollPane(history);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void update(Observable o, Object o1) {
        scrollPane.revalidate();
        repaint();
    }
}

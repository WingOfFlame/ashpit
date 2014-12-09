/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jotto;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Justin Hu
 */
public class SearchView extends JPanel implements Observer {

    private final JottoModel m;
    private final JTable result;
    private final JButton search;
    private final JScrollPane scrollPane;
    private final JTextField[] inputs = new JTextField[5];
//    String [] letters = {
//        "A","B","C","D","E",
//        "F","G","H","I","J",
//        "K","L","M","N","O",
//        "P","Q","R","S","T",
//        "U","V","W","X","Y","Z"};

    SearchView(JottoModel model) {
        m = model;
        setLayout(new BorderLayout());
        //setBorder(BorderFactory.createTitledBorder("Search"));
        setBorder(BorderFactory.createTitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, new Font("SansSerif", Font.PLAIN, 12), Color.GRAY));
        setBackground(Color.WHITE);

        search = new JButton("Search");
        search.setFont(new Font("SansSerif", Font.PLAIN, 15));
        search.setBackground(Color.WHITE);
        search.setMargin(new Insets(0, 0, 0, 0));
        search.setMinimumSize(new Dimension(200, 550));

        search.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String s = "";
                for (int i = 0; i < 5; i++) {
                    if (inputs[i].getText().length() == 1) {
                        s = s + inputs[i].getText();
                    } else {
                        s = s + " ";
                    }
                }

                m.getSearch(s.toCharArray());
            }
        });
        JPanel top = new JPanel();
        top.setBackground(Color.WHITE);
        for (int i = 0; i < 5; i++) {
            JTextField input = new JTextField();
            input.setPreferredSize(new Dimension(25, 25));
            inputs[i] = input;
            input.setFont(new Font("SansSerif", Font.PLAIN, 25));
            //input.setHorizontalAlignment(JTextField.CENTER);
            input.setDocument(new JTextFormat(1));
            //input.getDocument().setDocumentFilter(filter);
            top.add(input);
        }
        top.add(search);

        result = new JTable(m.getSearchModel());
        result.setFont(new Font("SansSerif", Font.PLAIN, 15));
        result.setFillsViewportHeight(true);
        //result.setBackground(Color.WHITE);
        scrollPane = new JScrollPane(result);
        //scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //scrollPane.setBackground(Color.WHITE);
        this.add(top, BorderLayout.PAGE_START);
        this.add(scrollPane, BorderLayout.CENTER);

    }

    @Override

    public void update(Observable o, Object o1) {
        scrollPane.revalidate();
        repaint();
    }

}

package jotto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

interface IView {

    public void updateView();
}

class Guess {

    String guess;
    int exact;
    int partial;

    public Guess(String letters, int exact, int partial) {
        this.guess = letters;
        this.exact = exact;
        this.partial = partial;
    }

    @Override
    public String toString() {
        return "[" + this.guess + ", " + this.exact + ", "
                + this.partial + "]";
    }
}

public class JottoModel extends Observable {

    public static int NUM_LETTERS = 5;
    public static final String[] LEVELS = {
        "Easy", "Medium", "Hard", "Any Difficulty"};

    private WordList wordList;
    private Word word;
    private int guesses = -1;
    private AbstractTableModel HistoryModel;
    private AbstractTableModel SearchModel;
    private ArrayList<Guess> history = new ArrayList<Guess>();
    private ArrayList<Word> search = new ArrayList<Word>();
    private boolean win = false;
    private boolean[] letters;

    JottoModel() {
        this.wordList = new WordList();
        this.word = wordList.randomWord();
        this.letters = new boolean[26];
        this.HistoryModel = new AbstractTableModel() {
            // our data, note we can call these whatever we want
            private final String[] myDataColumnNames = {"Guesses",
                "Exact",
                "Partial"};

            // need to define these three methods:
            @Override
            public int getColumnCount() {
                return 3;
            }

            @Override
            public int getRowCount() {
                return history.size();
            }

            @Override
            public Object getValueAt(int row, int col) {
                switch (col) {
                    case 0:
                        return history.get(row).guess;
                    case 1:
                        return history.get(row).exact;
                    case 2:
                        return history.get(row).partial;
                }
                return null;	// for the compiler :(
            }

            // define this if you don't want default 'A', 'B', ... names
            @Override
            public String getColumnName(int col) {
                return myDataColumnNames[col];
            }

            /*
             * JTable uses this method to determine the default renderer/
             * editor for each cell.  If we didn't implement this method,
             * then the last column would contain text ("true"/"false"),
             * rather than a check box.
             */
            @Override
            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass(); // reflection!
            }

        };

        this.SearchModel = new AbstractTableModel() {
            private final String[] ColumnNames = {"All words that matches"};

            // need to define these three methods:
            @Override
            public int getColumnCount() {
                return 1;
            }

            @Override
            public int getRowCount() {
                return search.size();
            }

            @Override
            public Object getValueAt(int row, int col) {
                return search.get(row).getWord();
            }

            // define this if you don't want default 'A', 'B', ... names
            @Override
            public String getColumnName(int col) {
                return ColumnNames[col];
            }

            /*
             * JTable uses this method to determine the default renderer/
             * editor for each cell.  If we didn't implement this method,
             * then the last column would contain text ("true"/"false"),
             * rather than a check box.
             */
            @Override
            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass(); // reflection!
            }

        };

        setChanged();
    }

    public Word getWord() {
        return this.word;
    }

    public int getGuesses() {
        return this.guesses;
    }

    public boolean checkGuesses(String s) {
        return wordList.contains(s);
    }

    public boolean repeatedGuess(String s) {
        for (Guess history1 : history) {
            if (s.equals(history1.guess)) {
                return true;
            }
        }
        return false;

    }

    public void newWord(int level) {
        if (level == 3) {
            this.word = wordList.randomWord();
        } else {
            this.word = wordList.randomWord(level);
        }
        guesses = 10;
        win = false;
        Arrays.fill(letters, Boolean.FALSE);
        history.clear();
        //search.clear();
        setChanged();
        notifyObservers();
    }

    public void setWord(String s) {

        this.word = new Word(s, guesses);

        guesses = 10;
        win = false;
        history.clear();
        setChanged();
        notifyObservers();
    }

    public String[] getLevels() {
        return LEVELS;
    }

    public void guessWord(String guess) {
        char[] c = guess.toCharArray();
        char[] b = word.getWord().toCharArray();
        int exact = 0;
        int partial = 0;
        for (int i = 0; i < 5; i++) {
            letters[c[i] - 65] = true;
            if (c[i] == b[i]) {
                exact++;
                c[i] = '+';
                b[i] = '-';
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (c[i] == b[j]) {
                    partial++;
                    c[i] = '+';
                    b[j] = '-';
                }
            }
        }
        history.add(0, new Guess(guess, exact, partial));
        guesses--;
        if (exact == 5) {
            win = true;
            guesses = 0;
        }
        setChanged();
        notifyObservers();
    }

    public TableModel getHistoryModel() {
        return HistoryModel;
    }

    public TableModel getSearchModel() {
        return SearchModel;
    }

    public boolean isWin() {
        return win;
    }

    public void end() {
        guesses = 0;
        setChanged();
        notifyObservers();
    }

    public boolean letterGuessed(int i) {
        return letters[i];
    }

    public void getSearch(char[] c) {
        search.clear();
        WordPredicate wp = new WordPredicate(c);
        Word[] wa = wordList.getWords(wp);
        search.addAll(Arrays.asList(wa));
        setChanged();
        notifyObservers();
    }
}

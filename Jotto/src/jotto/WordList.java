package jotto;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * A list of words that can grow as needed.
 *
 * @author Byron Weber Becker
 */
public final class WordList extends Object implements IWordList {

    private Word[] words = new Word[10]; // must not be 0
    private int numUsed = 0;
    private static final String WORDS_FILE = "words.txt";

    /**
     * Create a word list from a file.
     *
     * @param in
     */
    public WordList(Scanner in) {
        super();
        this.readWords(in);
    }

    public WordList() {
        super();
        try {
            Scanner s = new Scanner(new File(WORDS_FILE));
            this.readWords(s);
            s.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + WORDS_FILE + " not found.");
            System.exit(-1);
        }
    }

    private void readWords(Scanner in) {
        while (in.hasNextLine()) {
            this.add(new Word(in));
        }
    }

    /**
     * Add a new word to the list.
     *
     * @param wd
     */
    public void add(Word wd) {
        if (this.numUsed >= this.words.length) {
            Word[] newList = new Word[this.words.length * 2];
            System.arraycopy(this.words, 0, newList, 0, this.numUsed);
            this.words = newList;
        }
        this.words[this.numUsed] = wd;
        this.numUsed++;
    }

    @Override
    public boolean contains(String aWord) {
        for (int i = 0; i < this.numUsed; i++) {
            if (this.words[i].getWord().equals(aWord)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int numWords() {
        return this.numUsed;
    }

    public Word get(int i) {
        if (i >= this.numUsed) {
            throw new IllegalArgumentException(
                    "words in list = " + this.numUsed + "; requested " + i);
        }
        return this.words[i];
    }

    @Override
    public Word randomWord() {
        int r = (int) (Math.random() * this.numUsed);
        return this.words[r];
    }

    @Override
    public Word randomWord(int difficulty) {
        int r = (int) (Math.random() * this.numUsed);
        // check the end of the array
        for (int i = r; i < this.numUsed; i++) {
            if (this.words[i].getDifficulty() == difficulty) {
                return this.words[i];
            }
        }

        // nothing in the end of the array;  check the beginning
        for (int i = 0; i < r; i++) {
            if (this.words[i].getDifficulty() == difficulty) {
                return this.words[i];
            }
        }
        return null;
    }

    @Override
    public Word getWord(IWordPredicate test) {
        int r = (int) (Math.random() * this.numUsed);

        // check the end of the array
        for (int i = r; i < this.numUsed; i++) {
            if (test.isOK(this.words[i])) {
                return this.words[i];
            }
        }

        // nothing in the end of the array;  check the beginning
        for (int i = 0; i < r; i++) {
            if (test.isOK(this.words[i])) {
                return this.words[i];
            }
        }

        // didn't find one
        return null;
    }

    @Override
    public Word[] getWords(IWordPredicate test) {
        int count = 0;
        for (int i = 0; i < this.numUsed; i++) {
            if (test.isOK(this.words[i])) {
                count++;
            }
        }

        Word[] answer = new Word[count];
        int next = 0;
        for (int i = 0; i < this.numUsed; i++) {
            if (test.isOK(this.words[i])) {
                answer[next] = this.words[i];
                next++;
            }
        }
        return answer;
    }

    @Override
    public Word[] getWords(int maxDesired, IWordPredicate test) {
        int count = 0;
        final int start = (int) (Math.random() * this.numUsed);
        for (int i = 0; i < this.numUsed && count < maxDesired; i++) {
            if (test.isOK(this.words[(start + i) % this.numUsed])) {
                count++;
            }
        }

        int numToGet = Math.min(count, maxDesired);
        Word[] answer = new Word[numToGet];
        int next = 0;
        for (int i = 0; i < this.numUsed && next < numToGet; i++) {
            int w = (start + i) % this.numUsed;
            if (test.isOK(this.words[w])) {
                answer[next] = this.words[w];
                next++;
            }
        }
        return answer;
    }
}

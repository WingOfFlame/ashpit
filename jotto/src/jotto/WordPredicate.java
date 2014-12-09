/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jotto;

/**
 *
 * @author Justin Hu
 */
public class WordPredicate extends Object implements IWordPredicate {

    private char[] letters = new char[5];
    boolean empty = true;

    public WordPredicate(char[] letter) {
        this.letters = letter;
        for (int i = 0; i < 5; i++) {
            //System.out.println((int)letters[i]);
            if (letters[i] != (char) 32) {
                empty = false;
            }

        }
    }

    @Override
    public boolean isOK(Word w) {
        if (empty) {
            return false;
        }
        char[] a = w.getWord().toCharArray();
        for (int i = 0; i < 5; i++) {
            //System.out.println((int)letters[i]);
            if (letters[i] >= 'A' && letters[i] <= 'Z' && letters[i] != a[i]) {
                return false;
            }

        }
        return true;
    }
}

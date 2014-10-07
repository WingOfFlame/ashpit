package Reversi_UI;

import java.io.*;
import java.io.IOException;
import java.util.Random;

enum Piece {

    none, black, white
}; // don't change the order for any reason!

public class Board {

    Piece[][] board = new Piece[8][8];
    int[] counter = new int[2]; // 0 = black, 1 = white
    boolean PassCounter;

    public Board() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = Piece.none;
            }
        }
        counter[0] = 0;
        counter[1] = 0;
        PassCounter = false;
    }
     public void copyBoard(Board another) {
        for (int i = 0; i < 8; i++) {
           for (int j = 0; j < 8; j++) {
               this.board[i][j] = another.board[i][j];
           
        }
        }
        this.counter[0]= another.counter[0];
        this.counter[1]= another.counter[1];
        this.PassCounter = another.PassCounter;
    }
    public Piece get(int i, int j) {
        return board[i][j];
    }

    public void set(Move move, Piece player) {
        switch (board[move.i][move.j]) {
            case white:
                counter[1]--;
                break;
            case black:
                counter[0]--;
                break;
        }
        board[move.i][move.j] = player;
        switch (player) {
            case white:
                counter[1]++;
                break;
            case black:
                counter[0]++;
                break;
        }
    }

    public int getCounter(Piece player) {
        return counter[player.ordinal() - 1];
    }

    public void clear() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = Piece.none;
            }
        }
        board[3][4] = Piece.black;
        board[4][3] = Piece.black;
        board[3][3] = Piece.white;
        board[4][4] = Piece.white;
        counter[0] = 2;
        counter[1] = 2;
        PassCounter = false;
    }

    public void println() {
        System.out.print("[");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] + ",");
            }
            System.out.println((i == 7 ? "]" : ""));
        }
    }
    
    public void printBoard(FileWriter fw) throws IOException {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                fw.write(board[i][j] + ",");
                fw.write("\n");
            } 
        }
        fw.write(counter[0]+"\n");
        fw.write(counter[1]+"\n");
    }
        
    public void readBoard(BufferedReader br) throws IOException {
        String piece = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                piece = br.readLine();
                System.out.println(piece);
                switch (piece) {
                    case "none,":
                        System.out.println(1);
                        board[i][j]=Piece.none;
                        break;
                    case "black,":
                        System.out.println(2);
                        board[i][j] = Piece.black;
                        break;
                    case "white,":
                        System.out.println(3);
                        board[i][j] = Piece.white;
                        break;
                    default:
                        System.out.println(4);
                        break;
                }
            }
        }
        counter[0] =Integer.parseInt(br.readLine());
        counter[1] =Integer.parseInt(br.readLine());
    }
    
    public int move(Move move, Piece kind) {
        return checkBoard(move, kind);
    }

    public boolean gameEnd() {
        return counter[0] + counter[1] == 64;
    }

    private int Check(Move move, int incx, int incy, Piece kind, boolean set) {
        Piece opponent;
        int x = move.i;
        int y = move.j;
        if (kind == Piece.black) {
            opponent = Piece.white;
        } else {
            opponent = Piece.black;
        }
        int n_inc = 0;
        x += incx;
        y += incy;
        while ((x < 8) && (x >= 0) && (y < 8) && (y >= 0) && (board[x][y] == opponent)) {
            x += incx;
            y += incy;
            n_inc++;
        }
        if ((n_inc != 0) && (x < 8) && (x >= 0) && (y < 8) && (y >= 0) && (board[x][y] == kind)) {
            if (set) {
                for (int j = 1; j <= n_inc; j++) {
                    x -= incx;
                    y -= incy;
                    set(new Move(x, y), kind);
                }
            }
            return n_inc;
        } else {
            return 0;
        }
    }

    private int checkBoard(Move move, Piece kind) {
        // check increasing x
        int j = Check(move, 1, 0, kind, true);
        // check decreasing x
        j += Check(move, -1, 0, kind, true);
        // check increasing y
        j += Check(move, 0, 1, kind, true);
        // check decreasing y
        j += Check(move, 0, -1, kind, true);
        // check diagonals
        j += Check(move, 1, 1, kind, true);
        j += Check(move, -1, 1, kind, true);
        j += Check(move, 1, -1, kind, true);
        j += Check(move, -1, -1, kind, true);
        if (j != 0) {
            set(move, kind);
        }
        return j;
    }

    private boolean isValid(Move move, Piece kind) {
        // check increasing x 
        if (Check(move, 1, 0, kind, false) != 0) {
            return true;
        }
        // check decreasing x 
        if (Check(move, -1, 0, kind, false) != 0) {
            return true;
        }
        // check increasing y 
        if (Check(move, 0, 1, kind, false) != 0) {
            return true;
        }
        // check decreasing y 
        if (Check(move, 0, -1, kind, false) != 0) {
            return true;
        }
        // check diagonals 
        if (Check(move, 1, 1, kind, false) != 0) {
            return true;
        }
        if (Check(move, -1, 1, kind, false) != 0) {
            return true;
        }
        if (Check(move, 1, -1, kind, false) != 0) {
            return true;
        }
        if (Check(move, -1, -1, kind, false) != 0) {
            return true;
        }
        return false;
    }
    
    //high value if opponent occupy more edge position
    private int strategy(Piece me, Piece opponent) {
        int tstrat = 0;
        for (int i = 0; i < 8; i++) {
            if (board[i][0] == opponent) {
                tstrat++;
            } else if (board[i][0] == me) {
                tstrat--;
            }
        }
        for (int i = 0; i < 8; i++) {
            if (board[i][7] == opponent) {
                tstrat++;
            } else if (board[i][7] == me) {
                tstrat--;
            }
        }
        for (int i = 0; i < 8; i++) {
            if (board[0][i] == opponent) {
                tstrat++;
            } else if (board[0][i] == me) {
                tstrat--;
            }
        }
        for (int i = 0; i < 8; i++) {
            if (board[7][i] == opponent) {
                tstrat++;
            } else if (board[7][i] == me) {
                tstrat--;
            }
        }
        return tstrat;
    }

    private class resultFindMax {

        int max, nb, nw;
    };

    private resultFindMax FindMax(int level, Piece me, Piece opponent) {
        int min, score, tnb, tnw;
        Piece[][] TempBoard = new Piece[8][8];
        int[] TempCounter = new int[2];
        resultFindMax res = new resultFindMax();
        level--;
        res.nb = counter[0];
        res.nw = counter[1];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(board[i], 0, TempBoard[i], 0, 8);
        }
        System.arraycopy(counter, 0, TempCounter, 0, 2);
        min = 10000;  // high value

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((board[i][j] == Piece.none) && (checkBoard(new Move(i, j), me) != 0)) {
                    if (level != 0) {
                        resultFindMax tres = FindMax(level, opponent, me);
                        tnb = tres.nb;
                        tnw = tres.nw;
                        score = tres.max;
                    } else {
                        tnb = counter[0];
                        tnw = counter[1];
                        score = counter[opponent.ordinal() - 1] - counter[me.ordinal() - 1] + strategy(me, opponent);
                    }
                    if (min > score) {
                        min = score;
                        res.nb = tnb;
                        res.nw = tnw;
                    }
                    for (int k = 0; k < 8; k++) {
                        System.arraycopy(TempBoard[k], 0, board[k], 0, 8);
                    }
                    System.arraycopy(TempCounter, 0, counter, 0, 2);
                }
            }
        }
        res.max = -min;
        return res;
    }

    public boolean findMove(Piece player, int llevel, Move move) {
        Piece[][] TempBoard = new Piece[8][8];
        int[] TempCounter = new int[2];
        int nb, nw, min, n_min;
        boolean found;
        resultFindMax res = new resultFindMax();
        Random random = new Random();
        
        //since there are max 6 steps left, AI cant predict beyond level6
        if (counter[0] + counter[1] >= 52 + llevel) {
            llevel = counter[0] + counter[1] - 52;
        }

        for (int i = 0; i < 8; i++) {
            System.arraycopy(board[i], 0, TempBoard[i], 0, 8);
        }
        System.arraycopy(counter, 0, TempCounter, 0, 2);
        found = false;
        min = 10000;  // high value
        n_min = 1;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((board[i][j] == Piece.none) && (checkBoard(new Move(i, j), player) != 0)) {
                    if (player == Piece.black) {
                        res = FindMax(llevel - 1, Piece.white, player);
                    } else {
                        res = FindMax(llevel - 1, Piece.black, player);
                    }
                    if ((!found) || (min > res.max)) {
                        min = res.max;
                        nw = res.nw;
                        nb = res.nb;
                        move.i = i;
                        move.j = j;
                        found = true;
                    } else if (min == res.max) { // RANDOM MOVE GENERATOR
                        n_min++;
                        if (random.nextInt(n_min) == 0) {
                            nw = res.nw;
                            nb = res.nb;
                            move.i = i;
                            move.j = j;
                        }
                    }
                    //             if found
                    //             then PreView(nw,nb);
                    for (int k = 0; k < 8; k++) {
                        System.arraycopy(TempBoard[k], 0, board[k], 0, 8);
                    }
                    
                    System.arraycopy(TempCounter, 0, counter, 0, 2);
                }
            }
        }
        return found;
    }

    public boolean userCanMove(Piece player) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((board[i][j] == Piece.none) && isValid(new Move(i, j), player)) {
                    return true;
                }
            }
        }
        return false;
    }

}

package Reversi;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hujus
 */
public class saveGame {

    Board saved = new Board();
    int gameLevel;
    Piece currentPlayer;

    public saveGame(Board newBoard,int gameLevel,Piece current) {

        this.saved.copyBoard(newBoard);
        this.gameLevel = gameLevel;
        this.currentPlayer=current;
    }
    public void update(Board newBoard, int gameLevel,Piece current) {

        this.saved.copyBoard(newBoard);
        this.gameLevel = gameLevel;
        this.currentPlayer=current;
    }
    
}

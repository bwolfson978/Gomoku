/**
 * Board
 * Created by andrewrot on 9/20/2017.
 *
 */

import java.util.Random;
import java.io.*;
import java.util.*;


public class Board {

    //2d array to keep track of the game board
    char[][] board = new char[15][15];
    char nextPlayer;
    char prevPlayer;
    //boolean yourTurn; //initialize randomly later on. true if your turn
    

    public Board(){
        initializeBoard(); 
        //this.yourTurn = true;
    }

    public Board(char[][] b, char nextPlayer, char prevPlayer){
        for(int i = 0; i < board[0].length; i++){
            for(int j = 0; j < board.length; j++){
                board[i][j] = b[i][j];
            }
        }
        this.nextPlayer = nextPlayer;
        this.prevPlayer = prevPlayer;
    }

    //iterate first col, every row in it, then move to next col across.
    private void initializeBoard(){
        for(int i = 0; i < board[0].length; i++){
            for(int j = 0; j < board.length; j++){
                board[i][j] = '_';
            }
        }
    }

    public int countMoves(){
        int moves = 0;
        for(int i = 0; i < board[0].length; i++){
            for(int j = 0; j < board.length; j++){
                if(board[i][j] != '_')
                    moves++;
            }
        }
        return moves;
    }




    //Check to see if the previous move won the game for either player
    //check the four directions
    public boolean checkWin(int r, int c){

        String symbol = "X";
        //check win for what letter?
        if(true) //fix
            symbol = "X";
        else 
            symbol = "O";
        //System.out.println("Check win for move: "+"["+r+"] ["+c+"]");

        if (vertical(r, c, symbol)|| horizontal(r, c, symbol) || forwardSlash(r, c, symbol)  || backwardSlash(r, c, symbol)) {
            //System.out.println("WININNNER");
            return true;
        }  

        return false;
    }

    //**** need to implement diagnols. find simple way to check
    // check for the / direction
    private boolean forwardSlash(int row, int col, String s){
        int streak = 0;
        //System.out.println("row: "+row + " col: "+col);

        int tempRow = row; // move this down left until it is 0, then start from there.
        int tempCol = col; //same here

        while(tempRow < 14 && tempCol > 0){
            tempRow++;
            tempCol--;
        }

        //System.out.println("FORWARD SLASH: Start looking from ["+tempRow+"] ["+tempCol+"]");
        //now we have our starting point on the edge of the board, check the diagnol starting here
        while(tempRow >= 0 && tempCol <= 14){ 
            //System.out.println("Examine ["+tempRow+"] ["+tempCol+"]  " + "comparing: "+s.charAt(0)+ " and "+ board[tempRow][tempCol]);
            if (s.charAt(0) == board[tempRow][tempCol]) {
                //System.out.println("streak++");
                streak++;
            }
            else 
                streak = 0;
            if(streak == 5)
                return true;
            tempRow--;
            tempCol++;
        }
        return false; 
    }
    // check for the \ direction
    private boolean backwardSlash(int row, int col, String s){
        //System.out.println("Check backwardSlash win for move: "+"["+row+"] ["+col+"]");
        int streak = 0;

        int tempRow = row; // move this down left until it is 0, then start from there.
        int tempCol = col; //same here

        while(tempCol < 14 && tempRow < 14){
            tempRow++;
            tempCol++;
        }
        //System.out.println("BACK SLASH: Start looking from ["+tempRow+"] ["+tempCol+"]");

        //now we have our starting point on the edge of the board, check the diagnol starting here
        while(tempCol >= 0 && tempRow >=0){ 
            //System.out.println("Examine ["+tempRow+"] ["+tempCol+"]  " + "comparing: "+s.charAt(0)+ " and "+ board[tempRow][tempCol]);

            if (s.charAt(0) == board[tempRow][tempCol]) 
                streak++;
            else 
                streak = 0;
            if(streak == 5)
                return true;
            tempRow--;
            tempCol--;
        }
        return false; 
    }
    private boolean vertical(int r, int c, String s){
        int streak = 0;
        for (int i = 0; i < board[0].length; i++) {
            //System.out.println("Examine ["+i+"] ["+c+"]  " + "comparing: "+s.charAt(0)+ " and "+ board[i][c]);

            if (s.charAt(0) == board[i][c]) 
                streak++;
            else 
                streak = 0;
            if(streak == 5)
                return true;
        }
        return false; 
    }
     private boolean horizontal(int r, int c, String s){
        int streak = 0;
        for (int i = 0; i < board.length; i++) {
            //System.out.println("Examine ["+r+"] ["+i+"]  " + "comparing: "+s.charAt(0)+ " and "+ board[r][i]);

            if (s.charAt(0) == board[r][i]) 
                streak++;
            else 
                streak = 0;
            if(streak == 5)
                return true;
        }
        return false; 
    }

    //iterate first col, every row in it, then move to next col across.
    public Set<Move> findEmpty(){
        Set<Move> empties = new HashSet<Move>();
        for(int i = 0; i < board[0].length; i++){
            for(int j = 0; j < board.length; j++){
                if(board[i][j] == '_'){
                    Move newSpot = new Move(i, j);
                    empties.add(newSpot);
                }
            }
        }
        return empties;
    }

    public void executeMove(char player, Move m){
        board[m.row][m.col] = player;
        prevPlayer = player;
        nextPlayer = (player == 'X') ? 'O' : 'X';
    }

    public int evalStatus(char p, int dToWin){
        return evalStatusRows(p, dToWin) + evalStatusCols(p, dToWin) + evalStatusForwardDiagnols(p, dToWin) + evalStatusBackDiagnols(p, dToWin);
    }



    //Return the total count of given streaks on the board for this direction ->(\)
    private int evalStatusBackDiagnols(char p, int dToWin){
        int count = 0;
        int length = 5 - dToWin; //5 - distance to win

        //Construct patterns to look for on the board (add to this later)
        String match1 = strMatch(p, length); //XXXX_
        String match2 = '_' + match1;        //_XXXX
        match1 += '_';



        String matchX = strMatch('X', length); //XXXX_
        String matchY = '_' + matchX;        //_XXXX
        matchX += '_';
        String matchA = strMatch('O', length); //XXXX_
        String matchB = '_' + matchA;        //_XXXX
        matchA += '_';

        int index = 0; //count up to 7 then down
        boolean reachedMidWay = false;

        //iterate through the rows checking for matches created above
        for (int i = 29; i > 0; i--) {
            String row = "";

            //need to construct a string from the diagnol direction
            //starting points
            if(!reachedMidWay){
                int r = i - 15;
                int c = 0;

                for(int j = 0; j <= index; j++){
                    row += board[r][j];
                    r++;
                }
            }
            if(reachedMidWay){
                int r = 0;
                for(int j = index; j <= 14; j++){
                    row+= board[r][j];
                    r++;

                }
            }

            if (row.contains(matchX)) {
                int x = row.indexOf(matchX);
                while (x >= 0) {
                    count++;
                    x = row.indexOf(matchX, matchX.length() + x);
                }
            }
            if (row.contains(matchY)) {
                int x = row.indexOf(matchY);
                while (x >= 0) {
                    count++;
                    x = row.indexOf(matchY, matchY.length() + x);
                }
            }
            if (row.contains(matchA)) {
                int x = row.indexOf(matchA);
                while (x >= 0) {
                    count++;
                    x = row.indexOf(matchA, matchA.length() + x);
                }
            }
            if (row.contains(matchB)) {
                int x = row.indexOf(matchB);
                while (x >= 0) {
                    count++;
                    x = row.indexOf(matchB, matchB.length() + x);
                }
            }


            if (row.contains(match1)) {
                int x = row.indexOf(match1);
                while (x >= 0) {
                    count++;
                    x = row.indexOf(match1, match1.length() + x);
                }
            }
            if (row.contains(match2)) {
                int x = row.indexOf(match2);
                while (x >= 0) {
                    count++;
                    x = row.indexOf(match2, match2.length() + x);
                }
            }
            //additional matching patterns e.g. "XX_XX, X_XXX, X_XXX, etc."
            boolean isX = (p == 'X') ? true : false;
            if(length == 4){
                // also check "XX_XX, X_XXX, XXX_X'
                String match3;
                String match4;
                String match5;
                    match3 = "XX_XX";
                    match4 = "X_XXX";
                    match5 = "XXX_X";
                
                  String  match6 = "OO_OO";
                  String  match7 = "O_OOO";
                  String  match8 = "OOO_O";
                
                if (row.contains(match3)) {
                    int x = row.indexOf(match3);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match3, match3.length() + x);
                    }
                }
                if (row.contains(match4)) {
                    int x = row.indexOf(match4);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match4, match4.length() + x);
                    }
                }
                if (row.contains(match5)) {
                    int x = row.indexOf(match5);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match5, match5.length() + x);
                    }
                }
                if (row.contains(match6)) {
                    int x = row.indexOf(match6);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match6, match6.length() + x);
                    }
                }
                if (row.contains(match7)) {
                    int x = row.indexOf(match7);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match7, match7.length() + x);
                    }
                }
                if (row.contains(match8)) {
                    int x = row.indexOf(match8);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match8, match8.length() + x);
                    }
                }
            }
            if(length == 3){
                // also check "XX__X, X__XX"
                String match3;
                String match4;
                    match3 = "XX__X";
                    match4 = "X__XX";
            
                  String  match5 = "OO__O";
                  String  match6 = "O__OO";
                
                if(row.contains(match3)){
                    int x = row.indexOf(match3);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match3, match3.length() + x);
                    }
                }
                if(row.contains(match4)){
                    int x = row.indexOf(match4);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match4, match4.length() + x);
                    }
                }
                if(row.contains(match5)){
                    int x = row.indexOf(match5);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match5, match5.length() + x);
                    }
                }
                if(row.contains(match6)){
                    int x = row.indexOf(match6);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match6, match6.length() + x);
                    }
                }
            }
            if(length == 2){
                // also check "X___X"
                String match3;
                    match3 = "X___X";
                
                 String   match4 = "O___O";
                
                if(row.contains(match3)){
                    int x = row.indexOf(match3);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match3, match3.length() + x);
                    }
                }
                if(row.contains(match4)){
                    int x = row.indexOf(match4);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match4, match3.length() + x);
                    }
                }
            }

            if(index == 14)
                reachedMidWay = true;

            //increase the spaces we are looking at until we reached the midway
            if(!reachedMidWay)
                index++; //increases this for next
            else
                index--; //count back to 0
        }
        return count;
    }

    //Return the total count of given streaks on the board for this direction ->(/)
    private int evalStatusForwardDiagnols(char p, int dToWin){
        int count = 0;
        int length = 5 - dToWin; //5 - distance to win

        //Construct patterns to look for on the board (add to this later)
        String match1 = strMatch(p, length); //XXXX_
        String match2 = '_' + match1;        //_XXXX
        match1 += '_';

        String matchX = strMatch('X', length); //XXXX_
        String matchY = '_' + matchX;        //_XXXX
        matchX += '_';
        String matchA = strMatch('O', length); //XXXX_
        String matchB = '_' + matchA;        //_XXXX
        matchA += '_';

        int index = 0; //count up to 7 then down
        boolean reachedMidWay = false;

        //iterate through the rows checking for matches created above
        for (int i = 0; i < 29; i++) {
            String row = "";

            //need to construct a string from the diagnol direction
            //starting points

            if(!reachedMidWay){
                int r = i;
                int c = 0;

                for(int j = 0; j < index; j++){
                    row += board[r][j];
                    r--;
                }
            }
            if(reachedMidWay){
                int r = 14;
                //int c = 0;

                for(int j = index; j <= 14; j++){
                    row += board[r][j];
                    r--;
                }
            }



            if (row.contains(match1)) {
                //System.out.println("3 FOUND A 4 IN A ROW MATCH");
                int x = row.indexOf(match1);
                while (x >= 0) {
                    count++;
                    x = row.indexOf(match1, match1.length() + x);
                }
            }
            if (row.contains(match2)) {
                 //System.out.println("4 FOUND A 4 IN A ROW MATCH");
                int x = row.indexOf(match2);
                while (x >= 0) {
                    count++;
                    x = row.indexOf(match2, match2.length() + x);
                }
            }
            if (row.contains(matchX)) {
                int x = row.indexOf(matchX);
                while (x >= 0) {
                    count++;
                    x = row.indexOf(matchX, matchX.length() + x);
                }
            }
            if (row.contains(matchY)) {
                int x = row.indexOf(matchY);
                while (x >= 0) {
                    count++;
                    x = row.indexOf(matchY, matchY.length() + x);
                }
            }
            if (row.contains(matchA)) {
                int x = row.indexOf(matchA);
                while (x >= 0) {
                    count++;
                    x = row.indexOf(matchA, matchA.length() + x);
                }
            }
            if (row.contains(matchB)) {
                int x = row.indexOf(matchB);
                while (x >= 0) {
                    count++;
                    x = row.indexOf(matchB, matchB.length() + x);
                }
            }
            //additional matching patterns e.g. "XX_XX, X_XXX, X_XXX, etc."
            boolean isX = (p == 'X') ? true : false;
            if(length == 4){
                // also check "XX_XX, X_XXX, XXX_X'
                String match3;
                String match4;
                String match5;
                    match3 = "XX_XX";
                    match4 = "X_XXX";
                    match5 = "XXX_X";
                
                 String   match6 = "OO_OO";
                 String   match7 = "O_OOO";
                String    match8 = "OOO_O";
                
                if (row.contains(match3)) {
                    int x = row.indexOf(match3);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match3, match3.length() + x);
                    }
                }
                if (row.contains(match4)) {
                    int x = row.indexOf(match4);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match4, match4.length() + x);
                    }
                }
                if (row.contains(match5)) {
                    int x = row.indexOf(match5);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match5, match5.length() + x);
                    }
                }
                if (row.contains(match6)) {
                    int x = row.indexOf(match6);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match6, match6.length() + x);
                    }
                }
                if (row.contains(match7)) {
                    int x = row.indexOf(match7);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match7, match7.length() + x);
                    }
                }
                if (row.contains(match8)) {
                    int x = row.indexOf(match8);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match8, match8.length() + x);
                    }
                }
            }
            if(length == 3){
                // also check "XX__X, X__XX"
                String match3;
                String match4;
                    match3 = "XX__X";
                    match4 = "X__XX";
            
                 String   match5 = "OO__O";
                 String   match6 = "O__OO";
                
                if(row.contains(match3)){
                    int x = row.indexOf(match3);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match3, match3.length() + x);
                    }
                }
                if(row.contains(match4)){
                    int x = row.indexOf(match4);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match4, match4.length() + x);
                    }
                }
                if(row.contains(match5)){
                    int x = row.indexOf(match5);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match5, match5.length() + x);
                    }
                }
                if(row.contains(match6)){
                    int x = row.indexOf(match6);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match6, match6.length() + x);
                    }
                }
            }
            if(length == 2){
                // also check "X___X"
                String match3;
                    match3 = "X___X";
                
                  String  match4 = "O___O";
                
                if(row.contains(match3)){
                    int x = row.indexOf(match3);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match3, match3.length() + x);
                    }
                }
                if(row.contains(match4)){
                    int x = row.indexOf(match4);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match4, match3.length() + x);
                    }
                }
            }

            if(index == 14)
                reachedMidWay = true;

            //increase the spaces we are looking at until we reached the midway
            else if(!reachedMidWay)
                index++; //increases this for next 
            else 
                index--; //count back to 0
        }
        return count;
    }


    //Return the total count of given streaks on the board
    private int evalStatusRows(char p, int dToWin){
        int count = 0;
        int length = 5 - dToWin; //5 - distance to win

        //Construct patterns to look for on the board (add to this later)
        String match1 = strMatch(p, length); //XXXX_
        String match2 = '_' + match1;        //_XXXX
        match1 += '_';



        String matchX = strMatch('X', length); //XXXX_
        String matchY = '_' + matchX;        //_XXXX
        matchX += '_';
        String matchA = strMatch('O', length); //XXXX_
        String matchB = '_' + matchA;        //_XXXX
        matchA += '_';

        //iterate through the rows checking for matches created above
        for (int i = 0; i < 15; i++) {
            String row = new String(board[i]);
            if (row.contains(match1)) {
                int x = row.indexOf(match1);
                while (x >= 0) {
                    count++;
                    x = row.indexOf(match1, match1.length() + x);
                }
            }
            if (row.contains(match2)) {
                int x = row.indexOf(match2);
                while (x >= 0) {
                    count++;
                    x = row.indexOf(match2, match2.length() + x);
                }
            }
            if (row.contains(matchX)) {
                int x = row.indexOf(matchX);
                while (x >= 0) {
                    count++;
                    x = row.indexOf(matchX, matchX.length() + x);
                }
            }
            if (row.contains(matchY)) {
                int x = row.indexOf(matchY);
                while (x >= 0) {
                    count++;
                    x = row.indexOf(matchY, matchY.length() + x);
                }
            }
            if (row.contains(matchA)) {
                int x = row.indexOf(matchA);
                while (x >= 0) {
                    count++;
                    x = row.indexOf(matchA, matchA.length() + x);
                }
            }
            if (row.contains(matchB)) {
                int x = row.indexOf(matchB);
                while (x >= 0) {
                    count++;
                    x = row.indexOf(matchB, matchB.length() + x);
                }
            }
            //additional matching patterns e.g. "XX_XX, X_XXX, X_XXX, etc."
            boolean isX = (p == 'X') ? true : false;
            if(length == 4){
                // also check "XX_XX, X_XXX, XXX_X'
                String match3;
                String match4;
                String match5;
                    match3 = "XX_XX";
                    match4 = "X_XXX";
                    match5 = "XXX_X";
                
                  String  match6 = "OO_OO";
                  String  match7 = "O_OOO";
                  String  match8 = "OOO_O";
                
                if (row.contains(match3)) {
                    int x = row.indexOf(match3);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match3, match3.length() + x);
                    }
                }
                if (row.contains(match4)) {
                    int x = row.indexOf(match4);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match4, match4.length() + x);
                    }
                }
                if (row.contains(match5)) {
                    int x = row.indexOf(match5);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match5, match5.length() + x);
                    }
                }
                if (row.contains(match6)) {
                    int x = row.indexOf(match6);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match6, match6.length() + x);
                    }
                }
                if (row.contains(match7)) {
                    int x = row.indexOf(match7);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match7, match7.length() + x);
                    }
                }
                if (row.contains(match8)) {
                    int x = row.indexOf(match8);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match8, match8.length() + x);
                    }
                }
            }
            if(length == 3){
                // also check "XX__X, X__XX"
                String match3;
                String match4;
                    match3 = "XX__X";
                    match4 = "X__XX";
            
                 String   match5 = "OO__O";
                 String   match6 = "O__OO";
                
                if(row.contains(match3)){
                    int x = row.indexOf(match3);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match3, match3.length() + x);
                    }
                }
                if(row.contains(match4)){
                    int x = row.indexOf(match4);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match4, match4.length() + x);
                    }
                }
                if(row.contains(match5)){
                    int x = row.indexOf(match5);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match5, match5.length() + x);
                    }
                }
                if(row.contains(match6)){
                    int x = row.indexOf(match6);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match6, match6.length() + x);
                    }
                }
            }
            if(length == 2){
                // also check "X___X"
                String match3;
                    match3 = "X___X";
                
                  String  match4 = "O___O";
                
                if(row.contains(match3)){
                    int x = row.indexOf(match3);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match3, match3.length() + x);
                    }
                }
                if(row.contains(match4)){
                    int x = row.indexOf(match4);
                    while (x >= 0) {
                        count++;
                        x = row.indexOf(match4, match3.length() + x);
                    }
                }
            }
        }
        return count;
    }

    private int evalStatusCols(char p, int dToWin){
        int count = 0;
        int length = 5 - dToWin;
        String match1 = strMatch(p, length);
        String match2 = '_' + match1;
        match1 += '_';



        String matchX = strMatch('X', length); //XXXX_
        String matchY = '_' + matchX;        //_XXXX
        matchX += '_';
        String matchA = strMatch('O', length); //XXXX_
        String matchB = '_' + matchA;        //_XXXX
        matchA += '_';

        for (int j = 0; j < 15; j++) {
            String column = "";
            for (int i = 0; i < 15; i++) {
                column += board[i][j];
            }
            if (column.contains(match1)) {
                int x = column.indexOf(match1);
                while (x >= 0) {
                    count++;
                    x = column.indexOf(match1, match1.length() + x);
                }
            }
            if (column.contains(match2)) {
                int x = column.indexOf(match2);
                while (x >= 0) {
                    count++;
                    x = column.indexOf(match2, match2.length() + x);
                }
            }
            if (column.contains(matchX)) {
                int x = column.indexOf(matchX);
                while (x >= 0) {
                    count++;
                    x = column.indexOf(matchX, matchX.length() + x);
                }
            }
            if (column.contains(matchY)) {
                int x = column.indexOf(matchY);
                while (x >= 0) {
                    count++;
                    x = column.indexOf(matchY, matchY.length() + x);
                }
            }
            if (column.contains(matchA)) {
                int x = column.indexOf(matchA);
                while (x >= 0) {
                    count++;
                    x = column.indexOf(matchA, matchA.length() + x);
                }
            }
            if (column.contains(matchB)) {
                int x = column.indexOf(matchB);
                while (x >= 0) {
                    count++;
                    x = column.indexOf(matchB, matchB.length() + x);
                }
            }
            //additional matching patterns e.g. "XX_XX, X_XXX, X_XXX, etc."
            boolean isX = (p == 'X') ? true : false;
            if(length == 4){
                // also check "XX_XX, X_XXX, XXX_X'
                String match3;
                String match4;
                String match5;
                    match3 = "XX_XX";
                    match4 = "X_XXX";
                    match5 = "XXX_X";
                
                 String   match6 = "OO_OO";
                 String   match7 = "O_OOO";
                  String  match8 = "OOO_O";
                
                if (column.contains(match3)) {
                    int x = column.indexOf(match3);
                    while (x >= 0) {
                        count++;
                        x = column.indexOf(match3, match3.length() + x);
                    }
                }
                if (column.contains(match4)) {
                    int x = column.indexOf(match4);
                    while (x >= 0) {
                        count++;
                        x = column.indexOf(match4, match4.length() + x);
                    }
                }
                if (column.contains(match5)) {
                    int x = column.indexOf(match5);
                    while (x >= 0) {
                        count++;
                        x = column.indexOf(match5, match5.length() + x);
                    }
                }
                if (column.contains(match6)) {
                    int x = column.indexOf(match6);
                    while (x >= 0) {
                        count++;
                        x = column.indexOf(match6, match6.length() + x);
                    }
                }
                if (column.contains(match7)) {
                    int x = column.indexOf(match7);
                    while (x >= 0) {
                        count++;
                        x = column.indexOf(match7, match7.length() + x);
                    }
                }
                if (column.contains(match8)) {
                    int x = column.indexOf(match8);
                    while (x >= 0) {
                        count++;
                        x = column.indexOf(match8, match8.length() + x);
                    }
                }
            }
            if(length == 3){
                // also check "XX__X, X__XX"
                String match3;
                String match4;
                    match3 = "XX__X";
                    match4 = "X__XX";
            
                 String   match5 = "OO__O";
                  String  match6 = "O__OO";
                
                if(column.contains(match3)){
                    int x = column.indexOf(match3);
                    while (x >= 0) {
                        count++;
                        x = column.indexOf(match3, match3.length() + x);
                    }
                }
                if(column.contains(match4)){
                    int x = column.indexOf(match4);
                    while (x >= 0) {
                        count++;
                        x = column.indexOf(match4, match4.length() + x);
                    }
                }
                if(column.contains(match5)){
                    int x = column.indexOf(match5);
                    while (x >= 0) {
                        count++;
                        x = column.indexOf(match5, match5.length() + x);
                    }
                }
                if(column.contains(match6)){
                    int x = column.indexOf(match6);
                    while (x >= 0) {
                        count++;
                        x = column.indexOf(match6, match6.length() + x);
                    }
                }
            }
            if(length == 2){
                // also check "X___X"
                String match3;
                    match3 = "X___X";
                
                   String match4 = "O___O";
                
                if(column.contains(match3)){
                    int x = column.indexOf(match3);
                    while (x >= 0) {
                        count++;
                        x = column.indexOf(match3, match3.length() + x);
                    }
                }
                if(column.contains(match4)){
                    int x = column.indexOf(match4);
                    while (x >= 0) {
                        count++;
                        x = column.indexOf(match4, match3.length() + x);
                    }
                }
            }
        }

        return count;
    }

    String strMatch(char p, int length) {
        String match = "";
        for (int i = 0; i < length; i++) {
            match += Character.toString(p);
        }
        return match;
    }

    LinkedList<Move> getPlayerPlaces(char p) {
        LinkedList<Move> places = new LinkedList<>();
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (board[i][j] == p) {
                    places.add(new Move(i,j));
                }
            }
        }
        return places;
    }

    LinkedList<Move> lookAround(Move move) {
        LinkedList<Move> adjacent = new LinkedList<Move>();
        int[] coords = new int[]{move.row, move.col};
        int i = coords[0];
        int j = coords[1];
        Move x;
        if (i - 1 >= 0) {
            if (board[i - 1][j] == '_') {
                x = new Move(i - 1, j);
                adjacent.add(x);
            }
            if (j - 1 >= 0) {
                if (board[i - 1][j - 1] == '_') {
                    x = new Move(i - 1, j - 1);
                    adjacent.add(x);
                }
            }
        }
        if (j + 1 < 15) {
            if (board[i][j + 1] == '_') {
                x = new Move(i, j + 1);
                adjacent.add(x);
            }
            if (i - 1 >= 0) {
                if (board[i - 1][j + 1] == '_') {
                    x = new Move(i - 1, j + 1);
                    adjacent.add(x);
                }
            }
        }
        if (i + 1 < 15) {
            if (board[i + 1][j] == '_') {
                x = new Move(i + 1, j);
                adjacent.add(x);
            }
            if (j + 1 < 15) {
                if (board[i + 1][j + 1] == '_') {
                    x = new Move(i + 1, j + 1);
                    adjacent.add(x);
                }
            }
        }
        return adjacent;
    }

    //print board
    public void printBoard(){

        System.out.println("     A B C D E F G H I J K L M N O ");
        for(int i = 0; i < board[0].length; i++){
            int row = i+1;
            //spaceing
            if(row < 10)
                System.out.print(row +"  [ ");
            else
                System.out.print(row +" [ ");

            for(int j = 0; j < board.length; j++){
                System.out.print(board[i][j] + " ");
            }
            System.out.println("]");
        }
        System.out.println(); //throw some pace between prints
    }



}
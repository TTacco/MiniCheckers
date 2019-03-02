import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import static java.lang.System.out;

public class CheckerBoard {
    Character[][] board = new Character[4][4];
    Scanner scan = new Scanner(System.in);
    boolean whiteTurn = true;

    Coordinate source;
    Coordinate destination;

    //Pattern m = new Pattern();
    Matcher m;



    public void InitializeGame(){
        Character input;
        Boolean validInput = true;
        //Create a new board
        InitializeBoard();

        // Repeat asking input until the desired input is accepted
        out.println("Please Choose a Mode \n1 for PvP and 2 PvE");
        do{
            input = scan.nextLine().charAt(0);
            switch (input){
                case '1':
                    out.println("Player Versus Player Initiated\n");
                    PlayerVersusPlayer();
                    break;
                case '2':
                    out.println("Player Versus Computer Initiated\nWhich side will you be playing as?");
                    PlayerVersusComputer();
                    break;

                    default:
                        validInput = false;
                        out.println("Invalid Input Please Try Again");
                        break;
            }

        }
        while(!validInput);
    }

    //Start a Player Versus Player game
    public void PlayerVersusPlayer(){
        PrintBoard();
        out.println("It is " + (whiteTurn? "White's": "Black's") + " turn\n ");
        String move = "";

        //Checks for validation
        // 1. Input is correct
        // 2. Does the piece exist in the source coordinate
        // 3. Is the destination coordinate valid for that piece
        out.println("Enter coordinates from source to destination ex: A2,C4 ");
        do
        {
            move = scan.nextLine();
        }
        while(!ValidMove(move));

    }

    //Handles if the input is a valid move
    public boolean ValidMove(String move){
        //1. Check if the input is correct
        try
        {
            source = new Coordinate(move.charAt(0), move.charAt(1));
            destination = new Coordinate(move.charAt(3), move.charAt(4));
        }
        catch(Exception e){
            out.println("Invalid Coordinate, try again");
            return false;
        }

        //2. Does the piece exist in the source coordinate
        if(board[source.getX()][source.getY()] != (whiteTurn ? 'W':'B')){
            out.println("Cannot move that piece (It is a non player piece or an empty space)");
            return false;
        }

        //3. Is the destination a valid move for that piece?
        //North, South, East, West movements
        //Outer if checks if the player is trying to move horizontally
        //Inner If the distance is greater than 1 movement we know that the player is trying to hop, else simple movement
        if(source.getX() == destination.getX() || source.getY() == destination.getY()){
            //Hop Movement
            if(Math.abs(source.getX()-destination.getX())>1 || Math.abs(source.getY()-destination.getY())>1){
                out.println("Moved more than one step");
            }
            //Single Movement
            else if(Math.abs(source.getX()-destination.getX())==1 || Math.abs(source.getY()-destination.getY())==1){
                out.println("Moved only one step");
            }
        }
        else if(source.getX() == destination.getX() && source.getY() == destination.getY()){
            out.println("Cannot move at the same position");
        }
        else{
            out.println("Cannot move vertically");
            return false;
        }



        out.println("Source (" + source.getX() + "," + source.getY() + ")");
        out.println("Destination (" + destination.getX() + "," + destination.getY() + ")");

        out.println("Valid Coordinate");
        return true;
    }

    public void MovePiece(String move){

    }

    //Start a Player Versus AI game
    public void PlayerVersusComputer(){
        //Add another case input if the player wants White or Black
    }

    //Resets the board positions at the start
    public void InitializeBoard(){
        int emptyspot = 3;
        whiteTurn = true;

        for(int y = 0; y<4; y++){
            for(int x = 0; x<4; x++){
                if(x == emptyspot){
                    board[x][y] = 'o';
                }
                else if(x < emptyspot){
                    board[x][y] = 'W';
                }
                else{
                    board[x][y] = 'B';
                }
            }
            //This var should keep the diagonal empty
            emptyspot--;
        }
    }

    //Allows quick printing of the board
    public void PrintBoard() {

        out.println("  A  B  C  D");
        for(int y = 0; y<4; y++){
            out.print(y);
            for(int x = 0; x<4; x++){
                out.print(" " + board[x][y] + " ");
            }
            out.println();
        }
    }
}
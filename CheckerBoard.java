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


    public void InitializeGame() {
        Character input;
        Boolean validInput = true;
        //Create a new board
        InitializeBoard();

        // Repeat asking input until the desired input is accepted
        out.println("Please Choose a Mode \n1 for PvP and 2 PvE");
        do {
            input = scan.nextLine().charAt(0);
            switch (input) {
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
        while (!validInput);
    }

    //===============================================================================================================
    //Create a PVP or PVE match
    //===============================================================================================================

    //Start a Player Versus Player game
    public void PlayerVersusPlayer() {
        PrintBoard();
        out.println("It is " + (whiteTurn ? "White's" : "Black's") + " turn\n ");
        String move = "";

        //Checks for move validation
        out.println("Enter coordinates from source to destination ex: A2,C4 ");
        do {
            move = scan.nextLine();
        }
        while (!ValidMove(move));

    }

    //Start a Player Versus AI game
    public void PlayerVersusComputer() {
        //Add another case input if the player wants White or Black
    }

    //Handles if the input is a valid move
    public boolean ValidMove(String move) {
        boolean validMove = true;
        boolean canPassOrSwap = true;

        try {
            TestCaseOne(move);
            TestCaseTwo();
            canPassOrSwap = TestCaseThree(whiteTurn ? 'W' : 'B');
            TestCaseFour(canPassOrSwap);

        } catch (Exception e) {
            out.println(e.getMessage());
            return false;
        }

        out.println("Valid Coordinate");
        return true;

    }

    public void MovePiece(String move) {

    }

    //===============================================================================================================
    //Valid Move Test Case Scenario Section
    //===============================================================================================================

    //Check if the input is correct
    //1. Assigns to the global coordinate variables (May throw exceptions)
    //2. Does the piece exist in the source coordinate
    //3. Does not stop the move but only checks if the player can swap or pass
    //4. Is the destination a valid move for that piece?

    //1. Assigns to the global coordinate variables (May throw exceptions)
    public void TestCaseOne(String move) {
        source = new Coordinate(move.charAt(0), move.charAt(1));
        destination = new Coordinate(move.charAt(3), move.charAt(4));
    }

    //2. Does the piece exist in the source coordinate
    public void TestCaseTwo() {
        if (board[source.getX()][source.getY()] != (whiteTurn ? 'W' : 'B')) {
            //out.println("Cannot move that piece (It is a non player piece or an empty space)");
            throw new SyntaxException("Cannot move that piece (It is a non player piece or an empty space");
        }
    }

    //3. Does not stop the move but only checks if the player can swap or pass
    public boolean TestCaseThree(char p) {
        boolean canPassAndSwap = true;

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if(board[x][y] == p){
                    //Check if the player has valid moves for any of their pieces
                    //If they can move normally then THEY CANNOT pass nor swap
                    //Checks each pieces and checks any moves horizontally or vertically, respectively
                        for(int a = x; a<4; a = XValue(a) ){
                            if(board[x][a] == 'o') return (canPassAndSwap = false);
                        }
                        for(int b = y; b<4; b = YValue(b) ){
                            if(board[b][y] == 'o') return (canPassAndSwap = false);
                        }

                }
            }
        }

        return canPassAndSwap;

    }

    //Checks Forwards Moves for each Pieces
    //I love local methods
    //And probably ternaries too, mostly ternaries
    int XValue(int x){
        return (whiteTurn? x+1 : x-1);
    }

    int YValue(int y){
        return (whiteTurn? y+1 : y-1);
    }

    //4. Is the destination a valid move for that piece?
    public void TestCaseFour(boolean canSwapOrPass) {
        /*
        North, South, East, West movements
        Outer if checks if the player is trying to move horizontally
        Inner If the distance is greater than 1 movement we know that the player is trying to hop, else simple movement
        */
        if (source.getX() == destination.getX() || source.getY() == destination.getY()) {

            //Hop Movement
            if (Math.abs(source.getX() - destination.getX()) > 1 || Math.abs(source.getY() - destination.getY()) > 1) {
                out.println("Moved more than one step");
            }
            //Single Movement
            else if (Math.abs(source.getX() - destination.getX()) == 1 || Math.abs(source.getY() - destination.getY()) == 1) {
                out.println("Moved only one step");
            }
        } else if (source.getX() == destination.getX() && source.getY() == destination.getY()) {
            out.println("Can be considered as a pass");

        } else {
            throw new SyntaxException("Cannot move vertically");
        }
    }

    //===============================================================================================================
    //Board Utilities
    //===============================================================================================================

    //Resets the board positions at the start
    public void InitializeBoard() {
        int emptyspot = 3;
        whiteTurn = true;

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (y == emptyspot) {
                    board[x][y] = 'o';
                } else if (y < emptyspot) {
                    board[x][y] = 'W';
                } else {
                    board[x][y] = 'B';
                }
            }
            emptyspot--; //This var should keep the diagonal empty
        }
    }

    //Allows quick printing of the board
    public void PrintBoard() {

        out.println("  A  B  C  D");
        for (int x = 0; x < 4; x++) {
            out.print(x + 1);
            for (int y = 0; y < 4; y++) {
                out.print(" " + board[x][y] + " ");
            }
            out.println();
        }

    }


}

              /*
        out.println("Array Coordinate Output");
        out.print("Source (" + source.getX() + "," + source.getY() + ") ");
        out.println("Destination (" + destination.getX() + "," + destination.getY() + ")");\
        */
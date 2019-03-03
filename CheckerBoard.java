import jdk.nashorn.internal.runtime.regexp.joni.Syntax;
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

    int totalMoves;


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
        String move = "";

        //Checks for move validation
        out.println("Enter coordinates from source to destination ex: A2,C4 ");


        do {
            out.println("======================");
            PrintBoard();
            out.println("It is " + (whiteTurn ? "White's" : "Black's") + " turn\n ");
            out.println("======================");

            do {
                move = scan.nextLine();
            }
            while (!ValidMove(move));

            MovePiece();
            move = "";
        } while (!WinCondition());

    }

    //Start a Player Versus AI game
    public void PlayerVersusComputer() {
        //Add another case input if the player wants White or Black
    }

    //===============================================================================================================
    //Board Conditions and Movement
    //===============================================================================================================

    //Handles if the input is a valid move
    public boolean ValidMove(String move) {
        boolean canPassOrSwap = true;

        try {
            TestCaseOne(move);
            TestCaseTwo();
            canPassOrSwap = TestCaseThree(whiteTurn ? 'W' : 'B');
            TestCaseFour(canPassOrSwap);

        } catch (Exception e) {
            //out.println(e.printStackTrace());
            out.println(e.getMessage());
            return false;
        }

        out.println("Valid Move");
        return true;

    }

    public void MovePiece() {
        //Stores the source character
        char bufferChar = board[source.getX()][source.getY()];

        //Source Coordinate Char = Destination Coordinate Char
        board[source.getX()][source.getY()] = board[destination.getX()][destination.getY()];

        //Destination Coordinate Char = BufferChar
        board[destination.getX()][destination.getY()] = bufferChar;
    }

    public boolean WinCondition() {
        int limit = 3;
        int whiteScore = 0;
        int blackScore = 0;

        for (int x = 0; x < 4; x++) {
            for(int y = 0; y < 4; y++) {
                if(y < limit){
                    if(board[x][y]=='B') blackScore++;
                }
                else if(y > limit){
                    if(board[x][y]=='W') whiteScore++;
                }
            }
            limit--;
        }

        if(whiteScore >= 6){
            out.println("White side has won!");
            PrintBoard();
            return true;
        }
        else if(blackScore >= 6){
            out.println("Black side has won!");
            PrintBoard();
            return true;
        }


        whiteTurn = !whiteTurn;
        return false;
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

    //2. Does the piece exist in the source coordinate or is there already piece at the destination?
    public void TestCaseTwo() {
        if (board[source.getX()][source.getY()] != (whiteTurn ? 'W' : 'B')) {
            throw new SyntaxException("Cannot move that piece (It is a non player piece or an empty space");
        }
    }

    //3. Does not stop the move but only checks if the player can swap or pass
    public boolean TestCaseThree(char p) {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (board[x][y] == p) {
                    //Check if the player has valid moves for any of their pieces
                    //If they can move normally then THEY CANNOT pass nor swap
                    //Checks each pieces and checks any moves horizontally or vertically, respectively
                    for (int a = y; a < 4 && a > 0; a = ArrayMove(a)) {
                        if (board[x][a] == 'o') return (false);
                    }
                    for (int b = x; b < 4 && b > 0; b = ArrayMove(b)) {
                        if (board[b][y] == 'o') return (false);
                    }

                }
            }
        }

        return true;

    }

    //Checks Forwards Moves for each Pieces DEPENDING ON THE CURRENT PLAYER
    //I love local methods
    //And probably ternaries too, mostly ternaries
    int ArrayMove(int z) {
        return (whiteTurn ? z + 1 : z - 1);
    }


    //4. Is the destination a valid move for that piece?
    public void TestCaseFour(boolean canSwapOrPass) {
        /*
        North, South, East, West movements
        Outer if checks if the player is trying to move horizontally
        Inner If the distance is greater than 1 movement we know that the player is trying to hop, else simple movement
        */

        if (source.getX() == destination.getX() && source.getY() == destination.getY()) {
            if(canSwapOrPass) {
                out.println("Player has Passed");
            }
            else{
                throw new SyntaxException("You pass turns yet as you still have forward moves");
            }
        }
        else if (source.getX() == destination.getX() || source.getY() == destination.getY()) {
            //out.println("Movement detected");
            //Hop Movement Horizontal
            //out.println("SOURCE : "+ source.getX() + " " + source.getY() );
            //out.println("DESTINATION : "+ destination.getX() + " " + destination.getY() );

            if (Math.abs(source.getY() - destination.getY()) > 1) {
                //out.println("HOP CHECK H");

                if(!(board[destination.getX()][destination.getY()] == 'o')){
                    throw new SyntaxException("Cannot hop to an existing piece");
                }

                for(int b = source.getY(); b != destination.getY(); b = Increment(b) ){
                    if(board[source.getX()][b] == 'o'){
                        throw new SyntaxException("Cannot hop horizontally");
                    }

                }

            }
            //Hop Movement Vertical
            else if(Math.abs(source.getX() - destination.getX()) > 1){
                //out.println("HOP CHECK V");

                if(!(board[destination.getX()][destination.getY()] == 'o')){
                    throw new SyntaxException("Cannot hop to an existing piece");
                }

                for(int a = source.getX(); a != destination.getX(); a = Increment(a) ){
                    if(board[a][source.getY()] == 'o'){
                        throw new SyntaxException("Cannot hop vertically");
                    }
                }

            }
            //Single Movement
            else if (Math.abs(source.getX() - destination.getX()) == 1 || Math.abs(source.getY() - destination.getY()) == 1) {
                out.println("Moved only one step");
                if ((board[destination.getX()][destination.getY()] == 'W'
                        || board[destination.getX()][destination.getY()] == 'B')
                        && !canSwapOrPass) {
                    throw new SyntaxException("There is a piece that already exists in that area and the player is not allowed to swap yet");
                }
            }
        }  else {
            throw new SyntaxException("Cannot move vertically");
        }




    }

    public int Increment(int z){

        if(source.getX() < destination.getX() || source.getY() < destination.getY() ){
            z++;
        }
        else{
            z--;
        }

        return z;
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
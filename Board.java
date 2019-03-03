import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;

import java.util.ArrayList;

import static java.lang.System.out;

public class Board {

    boolean aiMatch = false;

    char player;
    char ai_player;

    int totalMoves;

    char board[][] =
            {
                    {'o', 'o', 'o', 'o'},
                    {'o', 'o', 'o', 'o'},
                    {'o', 'o', 'o', 'o'},
                    {'o', 'o', 'o', 'o'},
            };


    //==================================================================================
    //Board AI
    //==================================================================================

    //Up Right Down Left
    int directionI[] = {-1, 0, 1, 0};
    int directionJ[] = {0, 1, 0, -1};

    public ArrayList<AIMove> GenerateMoves(char player){
        ArrayList<AIMove> allPossibleMoves = null;

        boolean canSwapOrPass = TestCaseThree(player);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if(board[i][j] == player){
                    for (int radius = 1, loop = 0; loop < 4; radius++,loop++) {
                        try{
                            //Test Destination Coordinates (k,l)
                            int k = i+radius*directionI[loop];
                            int l = j+radius*directionJ[loop];
                            if((k>=0 && k<=3 ) && (l>=0 && l<=3 )){

                                if(MovePieceSpecific(player, i,j,k,l)){
                                allPossibleMoves.add(new AIMove(i,j,k,l));
                                }
                            }
                        }
                        catch (Exception e){
                            out.println(e);
                        }

                    }
                }
            }
        }

        return allPossibleMoves;
    }



    //==================================================================================
    //Board Piece Movement
    //==================================================================================

    public boolean MovePiece(int srcI, int srcJ, int dstI, int dstJ) {
        boolean validMove = true;

        validMove = ValidMoveCheck(srcI, srcJ, dstI, dstJ);

        if (validMove) {
            SwapPieces(srcI, srcJ, dstI, dstJ);
            return true;
        } else {
            return false;
        }
    }

    //Same as above but this is for the specific char implementation
    public boolean MovePieceSpecific(char player, int srcI, int srcJ, int dstI, int dstJ) {
        boolean validMove = true;

        out.println("SPECIFIC MOVE PIECE REACHED");

        validMove = ValidMoveCheckSpecific(player, srcI, srcJ, dstI, dstJ);

        if (validMove) {
            SwapPieces(srcI, srcJ, dstI, dstJ);
            return true;
        } else {
            return false;
        }
    }

    private void SwapPieces(int srcI, int srcJ, int dstI, int dstJ) {
        char bufferChar = board[srcI][srcJ];
        board[srcI][srcJ] = board[dstI][dstJ];
        board[dstI][dstJ] = bufferChar;
    }

    //Source I, Source J, Destination I, Destination J
    public boolean ValidMoveCheck(int srcI, int srcJ, int dstI, int dstJ) {
        try {
            TestCaseTwo(srcI, srcJ);
            TestCaseFour(TestCaseThree(), srcI, srcJ, dstI, dstJ);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    //For tree generation of specific player
    public boolean ValidMoveCheckSpecific(char player, int srcI, int srcJ, int dstI, int dstJ) {

        out.println("VALID MOVE CHECK REACHED");
        try {
            TestCaseTwo(player, srcI, srcJ);
            TestCaseFour(TestCaseThree(player), srcI, srcJ, dstI, dstJ);
        } catch (Exception e) {
            System.out.println(e.getMessage());

            out.println("THROWING EXCEPTION HERE");
            return false;
        }
        return true;
    }

    //==================================================================================
    //Logical or Valid Move Checkers, returns false if the move is not valid
    //==================================================================================

    //2. Does the piece exist in the source coordinate or is there already piece at the destination?
    public void TestCaseTwo(int i, int j) {
        if (board[i][j] != player) {
            throw new SyntaxException("Cannot move that piece (It is a non player piece or an empty space");
        }
    }

    public void TestCaseTwo(char player, int i, int j) {
        if (board[i][j] != player) {
            throw new SyntaxException("Cannot move that piece (It is a non player piece or an empty space");
        }
    }

    //3a. Does not stop the move but only checks if the player can swap or pass
    public boolean TestCaseThree() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (board[x][y] == player) {
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
    //3b. Overload where the current passed character is instead checked
    public boolean TestCaseThree(char piece) {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (board[x][y] == piece) {
                    for (int a = y; a < 4 && a >= 0; a = ArrayMove(a)) {
                        if (board[x][a] == 'o') return (false);
                    }
                    for (int b = x; b < 4 && b >= 0; b = ArrayMove(b)) {
                        if (board[b][y] == 'o') return (false);
                    }
                }
            }
        }
        return true;
    }


    int ArrayMove(int z) {

        if (player == 'w') {
            return z + 1;
        } else if (player == 'b') {
            return z - 1;
        }

        return 0;
    }

    public void TestCaseFour(boolean canSwapOrPass, int srcI, int srcJ, int dstI, int dstJ) {
        /*
        North, South, East, West movements
        Outer if checks if the player is trying to move horizontally
        Inner If the distance is greater than 1 movement we know that the player is trying to hop, else simple movement
        */

        if (srcI == dstI && srcJ == dstJ) {
            if (canSwapOrPass) {
                out.println("Player has Passed");
            } else {
                throw new SyntaxException("You cant pass turns yet as you still have forward moves");
            }
        } else if (srcI == dstI || srcJ == dstJ) {
            //out.println("Movement detected");
            //Hop Movement Horizontal

            if (Math.abs(srcJ - dstJ) > 1) {
                //out.println("HOP CHECK H");

                if (!(board[dstI][dstJ] == 'o')) {
                    throw new SyntaxException("Cannot hop to an existing piece");
                }

                for (int b = srcJ; b != dstJ; b = Increment(b, srcI, srcJ, dstI, dstJ)) {
                    if (board[srcI][b] == 'o') {
                        throw new SyntaxException("Cannot hop horizontally");
                    }

                }

            }
            //Hop Movement Vertical
            else if (Math.abs(srcI - dstJ) > 1) {
                //out.println("HOP CHECK V");

                if (!(board[dstI][dstJ] == 'o')) {
                    throw new SyntaxException("Cannot hop to an existing piece");
                }

                for (int a = srcI; a != dstI; a = Increment(a, srcI, srcJ, dstI, dstJ)) {
                    if (board[a][srcJ] == 'o') {
                        throw new SyntaxException("Cannot hop vertically");
                    }
                }

            }
            //Single Movement
            else if (Math.abs(srcI - dstI) == 1 || Math.abs(srcJ - dstJ) == 1) {
                out.println("Moved only one step");
                if ((board[dstI][dstJ] == 'w'
                        || board[dstI][dstJ] == 'b')
                        && !canSwapOrPass) {
                    throw new SyntaxException("There is a piece that already exists in that area and the player is not allowed to swap yet");
                }
            }
        } else {
            throw new SyntaxException("Cannot move vertically");
        }

    }

    //Handles incrementation for TestCaseFour
    public int Increment(int z, int srcI, int srcJ, int dstI, int dstJ) {

        if (srcI < dstI || srcJ < dstJ) {
            return ++z;
        } else {
            return --z;
        }
    }
    //==================================================================================
    //Board Utilities
    //==================================================================================

    public void InitalizeBoard() {
        int emptyspot = 3;
        player = 'w';

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (j == emptyspot) {
                    board[i][j] = 'o';
                } else if (j < emptyspot) {
                    board[i][j] = 'w';
                } else {
                    board[i][j] = 'b';
                }
            }
            emptyspot--; //This var should keep the diagonal empty
        }
    }

    public void DrawBoard() {
        out.println("  A  B  C  D");
        for (int i = 0; i < 4; i++) {
            out.print(i + 1);
            for (int j = 0; j < 4; j++) {
                out.print(" " + board[i][j] + " ");
            }
            out.println();
        }
    }

    public void SwapPlayer() {
        if (player == 'w') {
            player = 'b';
        } else {
            player = 'w';

        }
    }

    public boolean CheckVictory() {
        int limit = 3;
        int whiteScore = 0;
        int blackScore = 0;

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (y < limit) {
                    if (board[x][y] == 'B') blackScore++;
                } else if (y > limit) {
                    if (board[x][y] == 'W') whiteScore++;
                }
            }
            limit--;
        }

        if (whiteScore >= 6) {
            out.println("White side has won!");
            return true;
        } else if (blackScore >= 6) {
            out.println("Black side has won!");
            return true;
        }

        return false;
    }

    public void StartAIMatch(char team) {
        aiMatch = true;
        ai_player = team;
    }


}
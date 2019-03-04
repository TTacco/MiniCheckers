import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;

import java.util.ArrayList;

import static java.lang.System.out;

public class Board {

    public char board[][] =
            {
                    {'o', 'o', 'o', 'o'},
                    {'o', 'o', 'o', 'o'},
                    {'o', 'o', 'o', 'o'},
                    {'o', 'o', 'o', 'o'},
            };
    boolean aiMatch = false;
    char player;
    char ai_player;
    int totalMoves;
    int boardWPoint[][] =
            {
                    {-4, -2, -2, 0},
                    {-2, -2, 3, 7},
                    {-2, 3, 5, 9},
                    {0, 7, 9, 12},
            };

    int boardBPoint[][] =
            {
                    {12, 9, 7, 0},
                    {9, 5, 3, -2},
                    {7, 3, -2, -2},
                    {0, -2, -2, -4},
            };


    //==================================================================================
    //Board AI
    //==================================================================================

    //Up Right Down Left
    int directionI[] = {-1, 0, 1, 0};
    int directionJ[] = {0, 1, 0, -1};

    public ArrayList<AIMove> GenerateMoves(char player, char[][] board) {
        ArrayList<AIMove> allPossibleMoves = new ArrayList<AIMove>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == player) {
                    for (int radius = 1; radius < 4; radius++) {
                        for (int loop = 0; loop < 4; loop++) {
                            //Test Destination Coordinates (k,l)
                            int k = 0;
                            int l = 0;

                            k = i + radius * directionI[loop];
                            l = j + radius * directionJ[loop];
                            if ((k >= 0 && k < 4) && (l >= 0 && l < 4)) {
                                if (MovePieceSpecific(player, i, j, k, l)) {
                                    //System.out.print("Source " + i + "," + j);
                                    //System.out.println(" Destination " + k + "," + l);

                                    allPossibleMoves.add(new AIMove(i, j, k, l));
                                }
                            }
                        }
                    }
                }
            }
        }

        return allPossibleMoves;
    }

    public int MiniMax(char[][] currState, int depth, boolean maximizing, char currPlayerPiece) {
        if (depth == 0 || CheckVictoryAI(currState)) {

            return ScoreEvaluation(ai_player, currState);
        }

        ArrayList<AIMove> aiMoves = GenerateMoves(currPlayerPiece, currState);
        ArrayList<Child> children = new ArrayList<Child>(aiMoves.size());

        for(AIMove ai : aiMoves){
            children.add(new Child(currState, ai));
        }

        //for(Child c : children){
        //    out.println(c.currentMove.sourceI + "," + c.currentMove.sourceJ + "  " + c.currentMove.destinationI + "," + c.currentMove.destinationJ);
        //    c.DrawBoardState();
        //}

        if (maximizing) {
            int maxEval = -999999999;
            AIMove bestMove = new AIMove(-1,-1,-1,-1);
            char piece = ai_player;
            switch (ai_player) {
                case 'w':
                    piece = 'b';
                    break;
                case 'b':
                    piece = 'w';
                    break;
            }
            for(Child c : children){
                int eval = MiniMax(c.childState, depth-1, !maximizing, piece);
                maxEval = Math.max(maxEval, eval);

                if(depth == 8 && (eval >= maxEval)){
                     bestMove = c.currentMove;
                }
            }
            if(depth==8){
                SwapPiecesAI(bestMove);
            }

            return maxEval;
        }
        else if(!maximizing){
            int minEval = 99999999;
            for(Child c : children){
                int eval = MiniMax(c.childState, depth-1, !maximizing, ai_player);
                minEval = Math.min(minEval, eval);
            }
            return minEval;
        }


        return 1;
    }

    //===========================================================================================================
    //ALPHA BETA MIN MAX
    //===========================================================================================================

    public int MiniMaxAlphaBeta(char[][] currState, int depth, int alpha, int beta, boolean maximizing, char currPlayerPiece) {
        if (depth == 0 || CheckVictoryAI(currState)) {

            return ScoreEvaluation(ai_player, currState);
        }

        ArrayList<AIMove> aiMoves = GenerateMoves(currPlayerPiece, currState);
        ArrayList<Child> children = new ArrayList<Child>(aiMoves.size());

        for(AIMove ai : aiMoves){
            children.add(new Child(currState, ai));
        }

        if (maximizing) {
            int maxEval = -999999999;
            AIMove bestMove = new AIMove(-1,-1,-1,-1);
            char piece = ai_player;
            switch (ai_player) {
                case 'w':
                    piece = 'b';
                    break;
                case 'b':
                    piece = 'w';
                    break;
            }
            for(Child c : children){
                //out.println(c.currentMove.sourceI + "," + c.currentMove.sourceJ + "  " + c.currentMove.destinationI + "," + c.currentMove.destinationJ);
                //c.DrawBoardState();
                int eval = MiniMaxAlphaBeta(c.childState, depth-1, alpha, beta, !maximizing, piece);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);

                if(depth == 9 && (eval >= maxEval)){
                    bestMove = c.currentMove;
                }

                if(beta<= alpha){
                    break;
                }
            }
            if(depth==9){
                //out.println(bestMove.sourceI + "," + bestMove.sourceJ + "  " + bestMove.destinationI + "," + bestMove.destinationJ);
                SwapPiecesAI(bestMove);
            }

            return maxEval;
        }
        else if(!maximizing){
            int minEval = 999999999;
            for(Child c : children){
                //out.println(c.currentMove.sourceI + "," + c.currentMove.sourceJ + "  " + c.currentMove.destinationI + "," + c.currentMove.destinationJ);
                //c.DrawBoardState();
                int eval = MiniMaxAlphaBeta(c.childState, depth-1, alpha, beta, !maximizing, ai_player);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);

                if(beta<=alpha){
                    break;
                }
            }
            return minEval;
        }


        return 1;
    }

    //Heuristic Scoring
    public int ScoreEvaluation(char piece, char[][] board) {
        int evalScore = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == piece) {
                    if (piece == 'w') {
                        evalScore += boardWPoint[i][j];
                    } else if (piece == 'b') {
                        evalScore += boardBPoint[i][j];
                    }
                }
            }
        }

        return evalScore;
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
        return ValidMoveCheckSpecific(player, srcI, srcJ, dstI, dstJ);
    }

    public void SwapPieces(int srcI, int srcJ, int dstI, int dstJ) {
        char bufferChar = board[srcI][srcJ];
        board[srcI][srcJ] = board[dstI][dstJ];
        board[dstI][dstJ] = bufferChar;
    }

    public void SwapPiecesAI(AIMove ai) {
        int srcI = ai.sourceI;
        int srcJ = ai.sourceJ;
        int dstI = ai.destinationI;
        int dstJ = ai.destinationJ;

        char bufferChar = board[srcI][srcJ];
        board[srcI][srcJ] = board[dstI][dstJ];
        board[dstI][dstJ] = bufferChar;
    }


    //AI function
    public void SwapPiecesSpecific(char[][] board, int srcI, int srcJ, int dstI, int dstJ) {
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

        try {
            TestCaseTwoSpecific(player, srcI, srcJ);
            TestCaseFourSpecific(TestCaseThree(player), srcI, srcJ, dstI, dstJ);
        } catch (Exception e) {
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

    public void TestCaseTwoSpecific(char player, int i, int j) {
        if (board[i][j] != player) {
            throw new SyntaxException("");
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
            else if (Math.abs(srcI - dstI) > 1) {
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

    //Same but its for the AI
    public void TestCaseFourSpecific(boolean canSwapOrPass, int srcI, int srcJ, int dstI, int dstJ) {
        /*
        North, South, East, West movements
        Outer if checks if the player is trying to move horizontally
        Inner If the distance is greater than 1 movement we know that the player is trying to hop, else simple movement
        */

        if (srcI == dstI && srcJ == dstJ) {
            if (canSwapOrPass) {
            } else {
                throw new SyntaxException(null);
            }
        } else if (srcI == dstI || srcJ == dstJ) {
            //out.println("Movement detected");
            //Hop Movement Horizontal


            if (Math.abs(srcJ - dstJ) > 1) {
                //out.println("HOP CHECK H");
                if (!(board[dstI][dstJ] == 'o')) {
                    throw new SyntaxException(null);
                }

                for (int b = srcJ; b != dstJ; b = Increment(b, srcI, srcJ, dstI, dstJ)) {
                    if (board[srcI][b] == 'o') {
                        throw new SyntaxException(null);
                    }

                }
                //srcI = 2 , srcJ = 2 , dstI = 0 , dstJ = 2
            }
            //Hop Movement Vertical
            else if (Math.abs(srcI - dstI) > 1) {
                //out.println("HOP CHECK V");

                if (!(board[dstI][dstJ] == 'o')) {
                    throw new SyntaxException(null);
                }

                for (int a = srcI; a != dstI; a = Increment(a, srcI, srcJ, dstI, dstJ)) {
                    if (board[a][srcJ] == 'o') {
                        throw new SyntaxException(null);
                    }
                }

            }
            //Single Movement
            else if (Math.abs(srcI - dstI) == 1 || Math.abs(srcJ - dstJ) == 1) {
                if ((board[dstI][dstJ] == 'w'
                        || board[dstI][dstJ] == 'b')
                        && !canSwapOrPass) {
                    throw new SyntaxException(null);
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
        out.println();
        out.println("=================================================");
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
                    if (board[x][y] == 'b') blackScore++;
                } else if (y > limit) {
                    if (board[x][y] == 'w') whiteScore++;
                }
            }
            limit--;
        }
        if (whiteScore >= 6) {
            return true;
        } else if (blackScore >= 6) {
            return true;
        }

        return false;
    }

    public boolean CheckVictoryAI(char board[][]) {
        int limit = 3;
        int whiteScore = 0;
        int blackScore = 0;

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (y < limit) {
                    if (board[x][y] == 'b') blackScore++;
                } else if (y > limit) {
                    if (board[x][y] == 'w') whiteScore++;
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
        ai_player = Character.toLowerCase(team);
    }


}
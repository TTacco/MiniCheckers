import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;

import static java.lang.System.out;

public class Board {
    char board[][] =
            {
                    {'W', 'W', 'W', 'o'},
                    {'W', 'W', 'o', 'B'},
                    {'W', 'o', 'B', 'B'},
                    {'o', 'B', 'B', 'B'},
            };


    public int MovePiece(char player, int srcI, int srcJ, int dstI, int dstJ) {
        boolean validMove = true;


        validMove = ValidMoveCheck(player, srcI, srcJ, dstI, dstJ);


        if (validMove) {
            //Swap Characters Here

            return 1;
        } else {
            return -1;
        }
    }

    //Source I, Source J, Destination I, Destination J
    public boolean ValidMoveCheck(char player, int srcI, int srcJ, int dstI, int dstJ) {
        try {
            TestCaseTwo(player, srcI, srcJ);
            TestCaseFour(TestCaseThree(player), srcI, srcJ, dstI, dstJ);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }


        return true;
    }

    //2. Does the piece exist in the source coordinate or is there already piece at the destination?
    public void TestCaseTwo(char player, int i, int j) {
        if (board[i][j] != player) {
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
                    for (int a = y; a < 4 && a > 0; a = ArrayMove(a, p)) {
                        if (board[x][a] == 'o') return (false);
                    }
                    for (int b = x; b < 4 && b > 0; b = ArrayMove(b, p)) {
                        if (board[b][y] == 'o') return (false);
                    }

                }
            }
        }
        return true;
    }

    int ArrayMove(int z, char p) {

        if (p == 'W') {
            return z + 1;
        } else if (p == 'B') {
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
                throw new SyntaxException("You pass turns yet as you still have forward moves");
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

                for (int a = srcI; a != srcI; a = Increment(a, srcI, srcJ, dstI, dstJ)) {
                    if (board[a][srcJ] == 'o') {
                        throw new SyntaxException("Cannot hop vertically");
                    }
                }

            }
            //Single Movement
            else if (Math.abs(srcI - dstI) == 1 || Math.abs(srcJ - dstJ) == 1) {
                out.println("Moved only one step");
                if ((board[dstI][dstJ] == 'W'
                        || board[dstI][dstJ] == 'B')
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
}
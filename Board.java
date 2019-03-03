import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;

public class Board {
    char board[][] =
            {
                    {'W','W','W','o'},
                    {'W','W','o','B'},
                    {'W','o','B','B'},
                    {'o','B','B','B'},
            };



    public int MovePiece(char player, int srcI, int srcJ, int dstI, int dstJ){
        boolean validMove = true;


        validMove = ValidMoveCheck(player, srcI, srcJ, dstI, dstJ);


        if(validMove){
            //Swap Characters Here

            return 1;
        }
        else{
            return -1;
        }
    }


    public boolean ValidMoveCheck(char player, int srcI, int srcJ, int dstI, int dstJ){
        try
        {
            TestCaseTwo(player, srcI, srcJ);
            TestCaseThree(player);
        }
        catch(Exception e){
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

        if(p == 'W'){
           return z+1;
        }
        else if (p == 'B'){
           return z-1;
        }

        return 0;
    }



}

public class Child {

    AIMove currentMove;
    char[][] childState;

    public Child(char[][] b){
        childState = new char[4][4];
        for(int i = 0; i<4; i++){
            for(int j = 0; j<4; j++){
                childState[i][j] = b[i][j];
            }
        }
    }

    public Child(char[][] b, AIMove moveSetForThisBoard){
        childState = new char[4][4];
        for(int i = 0; i<4; i++){
            for(int j = 0; j<4; j++){
                childState[i][j] = b[i][j];
            }
        }
        currentMove = moveSetForThisBoard;
        SwapStatePiecesEnhanced(moveSetForThisBoard);
    }

    public void DrawBoardState() {
        System.out.println("  A  B  C  D");
        for (int i = 0; i < 4; i++) {
            System.out.print(i + 1);
            for (int j = 0; j < 4; j++) {
                System.out.print(" " + childState[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void SwapStatePieces(int srcI, int srcJ, int dstI, int dstJ) {
        char bufferChar = childState[srcI][srcJ];
        childState[srcI][srcJ] = childState[dstI][dstJ];
        childState[dstI][dstJ] = bufferChar;
    }

    public void SwapStatePiecesEnhanced(AIMove ai) {
        int srcI = ai.sourceI;
        int srcJ = ai.sourceJ;
        int dstI = ai.destinationI;
        int dstJ = ai.destinationJ;
        char bufferChar = childState[srcI][srcJ];
        childState[srcI][srcJ] = childState[dstI][dstJ];
        childState[dstI][dstJ] = bufferChar;
    }

}

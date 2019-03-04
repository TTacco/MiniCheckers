public class Child {

    AIMove currentMove;

    char[][] array;

    public Child(char[][] b){
        array = new char[4][4];
        for(int i = 0; i<4; i++){
            for(int j = 0; j<4; j++){
                array[i][j] = b[i][j];
            }
        }
    }

    public void DrawBoardState() {
        System.out.println("  A  B  C  D");
        for (int i = 0; i < 4; i++) {
            System.out.print(i + 1);
            for (int j = 0; j < 4; j++) {
                System.out.print(" " + array[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void SwapStatePieces(int srcI, int srcJ, int dstI, int dstJ) {
        char bufferChar = array[srcI][srcJ];
        array[srcI][srcJ] = array[dstI][dstJ];
        array[dstI][dstJ] = bufferChar;
    }

    public void SwapStatePiecesEnhanced(AIMove ai) {
        int srcI = ai.sourceI;
        int srcJ = ai.sourceJ;
        int dstI = ai.destinationI;
        int dstJ = ai.destinationJ;
        char bufferChar = array[srcI][srcJ];
        array[srcI][srcJ] = array[dstI][dstJ];
        array[dstI][dstJ] = bufferChar;
    }

}

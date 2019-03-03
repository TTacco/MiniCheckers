import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;

public class Coordinate {
    protected int i, j = -1;

    public Coordinate(char i, char j) {

        //Parses the x and y coordinates into pure integers
        this.j = ConvertMove(i);
        this.i = Integer.parseInt(""+j) - 1;

        if((this.i > 3 || this.j > 3) || (this.i < 0 || this.j < 0)){
            throw new SyntaxException("Coordinate Array Out of Bounds, Please try again.");
        }
    }

    //Converts it into pure numbers
    public int ConvertMove(char moveAlpha){
        switch (moveAlpha) {
            case 'A':
            case 'a':
                return 0;
            case 'B':
            case 'b':
                return 1;
            case 'C':
            case 'c':
                return 2;
            case 'D':
            case 'd':
                return 3;
            default:
                throw new SyntaxException("Coordinate alphabet not recognized, Please try again");
        }
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

}

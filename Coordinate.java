import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;

public class Coordinate {
    protected int x, y = -1;

    public Coordinate(char x, char y) {

        //Parses the x and y coordinates into pure integers
        this.y = ConvertMove(x);
        this.x = Integer.parseInt(""+y) - 1;

        if((this.x > 3 || this.y > 3) || (this.x < 0 || this.y < 0)){
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

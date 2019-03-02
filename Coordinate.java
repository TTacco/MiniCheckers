import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;

public class Coordinate {
    protected int x, y = -1;

    public Coordinate(char x, char y) {

        //Converts the X coordinate
        this.x = ConvertMove(x);

        //Parses the Y Coordinate
        this.y = Integer.parseInt(""+y);


        if(this.x > 3 || this.y > 3){
            throw new SyntaxException("");
        }
    }

    //Converts it into pure numbers
    public int ConvertMove(char moveAlpha){
        switch (moveAlpha) {
            case 'A':
            case 'a':
            case '0':
                return 0;
            case 'B':
            case 'b':
            case '1':
                return 1;
            case 'C':
            case 'c':
            case '2':
                return 2;
            case 'D':
            case 'd':
            case '3':
                return 3;
            default:
                throw new SyntaxException("");
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

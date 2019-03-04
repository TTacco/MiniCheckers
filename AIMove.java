public class AIMove {
    int sourceI, sourceJ, destinationI, destinationJ = -1;

    int minmaxEval;

    public AIMove(int sourceI, int sourceJ, int destinationI, int destinationJ) {
        this.sourceI = sourceI;
        this.sourceJ = sourceJ;
        this.destinationI = destinationI;
        this.destinationJ = destinationJ;
    }

    public int getSourceI() {
        return sourceI;
    }

    public void setSourceI(int sourceI) {
        this.sourceI = sourceI;
    }

    public int getSourceJ() {
        return sourceJ;
    }

    public void setSourceJ(int sourceJ) {
        this.sourceJ = sourceJ;
    }

    public int getDestinationI() {
        return destinationI;
    }

    public void setDestinationI(int destinationI) {
        this.destinationI = destinationI;
    }

    public int getDestinationJ() {
        return destinationJ;
    }

    public void setDestinationJ(int destinationJ) {
        this.destinationJ = destinationJ;
    }
}

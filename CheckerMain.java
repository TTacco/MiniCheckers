import java.util.Scanner;

public class CheckerMain {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        boolean validInput = false;
        char input;

        while (true) {
            Board b = new Board();
            b.InitalizeBoard();
            validInput = false;

            System.out.println("Please input your game mode, 1 for Player Versus Player and 2 for Player Versus AI");
            do{
                input = scan.nextLine().charAt(0);

                switch(input){
                    case '1':

                    case '2':
                }


            }while(!validInput);


            b.DrawBoard();

            //Game Loop
            do {
                System.out.println("It is " + (b.player == 'w'? "White's " : "Black's ") + "turn" );



                boolean validMove = b.MovePiece();

                if(validMove){
                    b.DrawBoard();
                    //b.SwapPieces();
                }
                else{
                    System.out.println("Invalid Input");
                }

            } while (true);
        }
    }
}

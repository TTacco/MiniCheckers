import java.util.ArrayList;
import java.util.Scanner;

public class CheckerMain {
    static Coordinate src;
    static Coordinate dst;

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        boolean validInput = false;
        boolean validTeam = false;
        boolean syntaxValid = false;
        char input;

        while (true) {
            Board b = new Board();
            b.InitalizeBoard();
            validInput = false;
            validTeam = false;

            System.out.println("Please input your game mode, 1 for Player Versus Player and 2 for Player Versus AI");
            do {
                input = scan.nextLine().charAt(0);

                switch (input) {
                    case '1':
                        validInput = true;
                        break;
                    case '2':
                        System.out.println("Please pick an AI side, W or B");
                        do {
                            input = scan.nextLine().charAt(0);
                            switch (input) {
                                case 'W':
                                case 'w':
                                case 'B':
                                case 'b':
                                    b.StartAIMatch(input);
                                    validTeam = true;
                                    validInput = true;
                                    break;
                                default:
                                    validTeam = false;
                                    validInput = false;
                                    System.out.println("Invalid input, try again");
                            }
                        }
                        while (!validTeam);
                        break;
                    default:
                        validInput = false;
                }
            } while (!validInput);


            b.DrawBoard();
            //Game Loop
            do {
                System.out.println("It is " + (b.player == 'w' ? "White's " : "Black's ") + "turn");

                //If the input coordinates are part of the syntax or not
                if(b.ai_player != b.player) {
                    do {
                        try {
                            syntaxValid = true;
                            ValidateSyntax(scan.nextLine());
                        } catch (Exception e) {
                            syntaxValid = false;
                        }
                    } while (!syntaxValid);
                }
                else if(b.ai_player == b.player){
                    ArrayList<AIMove> allMoves = b.GenerateMoves(b.ai_player);

                    //System.out.println("IS EMPTY? " + allMoves.isEmpty());

                    for(AIMove a : allMoves){
                        System.out.print("Source " + a.sourceI+ "," +a.sourceJ);
                        System.out.println(" Destination " + a.destinationI+ "," +a.destinationJ);
                    }

                    scan.nextLine();
                    //PERFORM AI MOVE HERE
                }

                //Checks if the move is valid, returns a bool for now
                boolean validMove = b.MovePiece(src.getI(),src.getJ(),dst.getI(),dst.getJ());
                if (validMove) {
                    b.DrawBoard();
                    b.SwapPlayer();
                    if(b.CheckVictory())break;
                } else {
                    System.out.println("Invalid Input");
                }

            } while (true);
        }
    }

    public static void ValidateSyntax(String move) {
        src = new Coordinate(move.charAt(0), move.charAt(1));
        dst = new Coordinate(move.charAt(3), move.charAt(4));
    }
}

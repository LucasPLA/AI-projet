import java.util.Scanner;

public class Main {

    public static void jouer(Board board) {
        Scanner sc = new Scanner(System.in);
        //0: gauche, 1: devant, 2: droite
        System.out.println("Entrez la direction (0: devant, 1:droite, 3:gauche) :");

        String str = sc.nextLine();
        System.out.println("Vous avez choisi : " + str);

        int nbCases = ((int)(Math.random()*4))+1; // Random roll
        System.out.println("Vous vous déplacez de " + nbCases + "cases");

        int newOrientation = board.getOrientation() + Integer.parseInt(str);
        board.bougeVendeur(nbCases, newOrientation);
        System.out.println(Display.printBoard(board));
    }

    public static void main(String[] args) {

       Joueur black = new Joueur("Human", "x ");
       Joueur white = new Joueur("AI", "o ");

       Board board = new Board(7);
       System.out.println(Display.printBoard(board));

       while(black.getNbTapisRestants()!=0 || white.getNbTapisRestants()!=0){
           jouer(board);
       }
    }
}
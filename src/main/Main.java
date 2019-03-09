import java.util.Scanner;

public class Main {

    public static void jouer(Board b) {
        Scanner sc = new Scanner(System.in);
        //0: gauche, 1: devant, 2: droite
        System.out.println("Entrez la direction (0: devant, 1:droite, 3:gauche) :");
        String str = sc.nextLine();
        System.out.println("Vous avez choisi : " + str);
        int nbCases = ((int)(Math.random()*4))+1; // Random roll
        System.out.println("Vous vous déplacez de " + nbCases + "cases");
        int newOrientation = b.getOrientation() + Integer.parseInt(str);
        b.bougeVendeur(nbCases, newOrientation);
        b.maj();
        System.out.println(b.getDisplay());
    }

    public static void main(String[] args) {

       Joueur black = new Joueur("Human", "x ");
       Joueur white = new Joueur("AI", "o ");

       Board b = new Board(7);
       b.maj();
       System.out.println(b.getDisplay());

       while(black.getNbTapisRestants()!=0 || white.getNbTapisRestants()!=0){
           jouer(b);
       }


        /* Main plateau = new Main();
        plateau.afficher();
        plateau.jouer();
       for(int i = 0; i<100; i++){
           System.out.println((int)(Math.random()*4));
       }*/

    }
}
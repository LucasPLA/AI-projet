import java.util.Scanner;

public class Main {

    public static void jouerCoup(Board board, Joueur joueurActif) {
        Scanner sc = new Scanner(System.in);
        //0: gauche, 1: devant, 2: droite
        int nbCases = ((int)(Math.random()*4))+1; // Random roll
        System.out.println("Le vendeur se déplacera de "+nbCases+". Entrez la direction (0: devant, 1:droite, 3:gauche) :");
        String str = sc.nextLine();

        int newOrientation = (board.getOrientation() + Integer.parseInt(str)) % 4;
        board.bougeVendeur(nbCases, newOrientation, joueurActif);
        Display.afficheBoard(board);
    }

    public static void poseTapis(Board board, Joueur joueurActif) {
        Scanner sc = new Scanner(System.in);
        System.out.println("xtapis1 ?");
        int x1 = Integer.parseInt(sc.nextLine());
        System.out.println("ytapis1 ?");
        int y1 = Integer.parseInt(sc.nextLine());
        System.out.println("xtapis2 ?");
        int x2 = Integer.parseInt(sc.nextLine());
        System.out.println("xtapis2 ?");
        int y2 = Integer.parseInt(sc.nextLine());

        board.poseTapis(joueurActif, x1, y1, x2, y2);
        Display.afficheBoard(board);
    }

    public static void main(String[] args) {

       Joueur black = new Joueur("Human", "x ");
       Joueur white = new Joueur("AI", "o ");
       Joueur joueurActif = white;

       Board board = new Board(7);
       Display.afficheBoard(board);

       while(black.getNbTapisRestants()!=0 || white.getNbTapisRestants()!=0){
           jouerCoup(board, joueurActif);
           poseTapis(board, joueurActif);
           joueurActif = (joueurActif == white) ? black : white;
       }

    }
}

//TODO :
/*
- affichage
- tests unitaire
- stream java
- IA

1er milestone :
    - avoir une version en random qui marche
 */
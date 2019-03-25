import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Partie {

    Joueur white;
    Joueur black;
    Board board;
    Joueur joueurActif;

    public Partie(Joueur white, Joueur black, Board board) {
        this.white = white;
        this.black = black;
        this.board = board;
        this.joueurActif = white;
    }

    public Joueur jouerPartieJoueur() {
        boolean bool;
        Display.afficheBoard(board);
        while((black.getNbTapisRestants()!=0 || white.getNbTapisRestants()!=0) && white.getArgent() > 0 && black.getArgent() > 0){
            Scanner sc = new Scanner(System.in);
            //0: gauche, 1: devant, 2: droite
            System.out.println("Entrez la direction (0: devant, 1:droite, 3:gauche) :");
            String str = sc.nextLine();

            jouerCoup(board, joueurActif, Integer.parseInt(str));
            Display.afficheBoard(board);
            Display.afficheJoueurs(black, white);

            bool = false;

            while(!bool){
                System.out.println("-1:haut/ 0:droite/ 1:bas/ 2:gauche ?");
                int pos1 = Integer.parseInt(sc.nextLine());

                System.out.println("-1:gauche / 0:droit / 1:droite");
                int pos2 = Integer.parseInt(sc.nextLine());

                bool = poseTapis(board, joueurActif, pos1, pos2);
                if(!bool){
                    System.out.println("Position invalide veuillez rééssayer");
                }
            }

            Display.afficheBoard(board);
        }

        if(white.getArgent() <= 0) {
            return black;
        }
        if(black.getArgent() <= 0) {
            return white;
        }
        board.getPointTapis(white, black);

        return (white.getArgent() > black.getArgent()) ? white : black;
    }

    public Joueur jouerPartieAleatoire() {
        boolean bool;
        while((black.getNbTapisRestants()!=0 || white.getNbTapisRestants()!=0) && white.getArgent() > 0 && black.getArgent() > 0){
            int direction = (int) (Math.random()*3);
            if (direction == 2) direction++;
            jouerCoup(board, joueurActif, direction);

            jouerCoup(board, joueurActif, direction);
            bool = false;
            while(!bool){
                int directionTapis = ((int) (Math.random()*4))-1;
                int directionTapis2 = ((int) (Math.random()*3))-1;
                bool = poseTapis(board, joueurActif, directionTapis, directionTapis2);
            }
            joueurActif = (joueurActif == white) ? black : white;
        }

        if(white.getArgent() <= 0) {
            return black;
        }
        if(black.getArgent() <= 0) {
            return white;
        }
        board.getPointTapis(white, black);

        return (white.getArgent() > black.getArgent()) ? white : black;
    }

    public void jouerCoup(Board board, Joueur joueurActif, int direction) {
        int nbCases = ((int)(Math.random()*6))+1; // Random roll
        nbCases = (nbCases >4) ? nbCases - 3 : nbCases;

        int newOrientation = (board.getOrientation() + direction) % 4;
        board.bougeVendeur(nbCases, newOrientation, joueurActif);
    }

    public boolean poseTapis(Board board, Joueur joueurActif, int pos1, int pos2) {

        int x1 = board.getAsam()[0] - ((pos1-1) % 2);
        int y1 = board.getAsam()[1] + (pos1 % 2);

        int x2, y2;
        if(pos1 % 2 != 0) {
            if((pos2 % 2) != 0) {
                x2 = x1 - (pos1*pos2);
                y2 = y1;
            } else {
                x2 = x1;
                y2 = y1 + pos1;
            }
        } else {
            if((pos2 % 2) != 0) {
                x2 = x1;
                y2 = y1 - ((pos1-1)*pos2);
            } else {
                x2 = x1 - (pos1-1);
                y2 = y1;
            }
        }

        //System.out.println("("+x1+";"+y1+") ; ("+x2+";"+y2+")");

        return board.poseTapis(joueurActif, x1, y1, x2, y2);
    }

    /* public List<Integer> getDirectionsPossibles() {
        List<Integer> tmp = new ArrayList<>();
        if(board.getAsam()[0] > 1 && board.getCase(board.getAsam()[0], board.getAsam()[1]) != null && (!board.getCase(board.getAsam()[0], board.getAsam()[1]).equals(board.getCase(board.getAsam()[0], board.getAsam()[1])) )) {
            tmp.add(2);
        }
        if(board.getAsam()[0] < 5) {
            tmp.add(0);
        }
        if(board.getAsam()[1] > 1) {
            tmp.add(-1);
        }
        if(board.getAsam()[1] < 5) {
            tmp.add(1);
        }
    } */
}

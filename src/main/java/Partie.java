import java.util.Random;
import java.util.Scanner;

public class Partie {

    private static final Random random = new Random();

    private final Joueur white;
    private final Joueur black;
    private final Board board;
    private Joueur joueurActif;

    public Partie(Joueur white, Joueur black, Board board) {
        this.white = white;
        this.black = black;
        this.board = board;
        this.joueurActif = white;
    }

    public Partie(Partie p){
        this.white = new Joueur(p.white);
        this.black = new Joueur(p.black);
        this.board = new Board(p.board);
        if(p.joueurActif.equals(p.white)){
            this.joueurActif = this.white;
        } else {
            this.joueurActif = this.black;
        }
    }

    public Joueur jouerPartieJoueur() { // Player vs player
        Display.afficheBoard(board);
        while((black.getNbTapisRestants()!=0 || white.getNbTapisRestants()!=0) && white.getArgent() > 0 && black.getArgent() > 0){
            coupDuJoueur();

            Display.afficheBoard(board);
            Display.afficheJoueurs(white, black);

            changementJoueur();
        }
        return calculWinner();
    }

    public Joueur jouerPartieJoueurVsIA() { // Player vs IA
        Display.afficheBoard(board);
        Display.afficheJoueurs(white, black);

        while((black.getNbTapisRestants()!=0 || white.getNbTapisRestants()!=0) && white.getArgent() > 0 && black.getArgent() > 0){

            // Joueur

            coupDuJoueur();

            Display.afficheBoard(board);
            Display.afficheJoueurs(white, black);

            changementJoueur();

            // IA

            jouerCoupMCTS();

            Display.afficheBoard(board);
            Display.afficheJoueurs(white, black);

            changementJoueur();
        }
        return calculWinner();
    }

    public Joueur jouerRandomIaVsMctsIa() {
        Display.afficheBoard(board);
        Display.afficheJoueurs(white, black);

        while((black.getNbTapisRestants()!=0 || white.getNbTapisRestants()!=0) && white.getArgent() > 0 && black.getArgent() > 0){

            // Random IA

            int direction = random.nextInt(3);
            if (direction == 2) direction++;
            jouerCoup(board, direction);
            positionTapis();

            Display.afficheBoard(board);
            Display.afficheJoueurs(white, black);

            changementJoueur();

            // MCTS IA

            jouerCoupMCTS();

            Display.afficheBoard(board);
            Display.afficheJoueurs(white, black);

            changementJoueur();
        }
        return calculWinner();
    }

    public Joueur jouerPartieAleatoire() { // IA qui joue aléatoirement toute une partie
        while((black.getNbTapisRestants()!=0 || white.getNbTapisRestants()!=0) && white.getArgent() > 0 && black.getArgent() > 0){
            int direction = random.nextInt(3);
            if (direction == 2) direction++;
            jouerCoup(board, direction);
            positionTapis();
            joueurActif = (joueurActif == white) ? black : white;
        }
        return calculWinner();
    }

    public boolean jouerUnCoupComplet(int direction, int nbCases, int posTapis, int oriTapis) { // Pour le MCTS (jouer un coup pour l'expansion des noeuds)
        if (direction == 2) direction++;
        boolean valid = jouerCoupDetermine(board, joueurActif, direction, nbCases, posTapis, oriTapis);
        joueurActif = (joueurActif == white) ? black : white;
        return valid;
    }

    private int jouerCoup(Board board, int direction) { // Bouge le vendeur selon la direction indiquée d'un nombre de case aléatoire
        int nbCases = random.nextInt(6)+1; // Random roll
        nbCases = (nbCases >4) ? nbCases - 3 : nbCases;

        int newOrientation = (board.getOrientation() + direction) % 4;
        board.bougeVendeur(nbCases, newOrientation, this.joueurActif);
        return nbCases-1;
    }

    private void jouerCoupMCTS(){
        IA ia = new IA(this, true);
        int wb = white.getArgent();
        int bb = black.getArgent();
//        System.out.println("white :" + wb +"; black :" + bb);
        int direction = ia.moveChoice();
        int dir = direction;
        int wa = white.getArgent();
        int ba = black.getArgent();
//        System.out.println("white :" + wa +"; black :" + ba);
        white.setArgent(wb-wa);
        black.setArgent(bb-ba);
        if (direction == 2) direction++;
        int nbCases = jouerCoup(board, direction);
        int max = -1;
        int wins;
        int val = 0;
        for(int i = 0; i<12; i++){
            wins = ia.children[dir*48+nbCases*12+i].nWins;
            if(wins > max){
                max = wins;
                val = i;
            }
        }
        poseTapis(this.board, ((val/4)-1), ((val%3)-1));
        //positionTapis();
    }

    private boolean jouerCoupDetermine(Board board, Joueur joueurActif, int direction, int nbCases, int posTapis, int oriTapis) { // Bouge le vendeur selon la direction indiquée d'un nombre de case déterminé
        int newOrientation = (board.getOrientation() + direction) % 4;
        board.bougeVendeur(nbCases, newOrientation, joueurActif);
        return poseTapis(this.board, posTapis, oriTapis);
    }

    private void coupDuJoueur(){
        Scanner sc = new Scanner(System.in, "UTF-8");
        System.out.println("Entrez la direction (0: devant, 1:droite, 3:gauche) :");
        String str = sc.nextLine();

        jouerCoup(board, Integer.parseInt(str));
        Display.afficheBoard(board);
        Display.afficheJoueurs(white, black);

        boolean bool = false;

        while(!bool){
            System.out.println("-1:haut/ 0:droite/ 1:bas/ 2:gauche ?");
            int pos1 = Integer.parseInt(sc.nextLine());

            System.out.println("-1:gauche / 0:droit / 1:droite");
            int pos2 = Integer.parseInt(sc.nextLine());

            bool = poseTapis(board, pos1, pos2);
            if(!bool){
                System.out.println("Position invalide veuillez rééssayer");
            }
        }
    }

    private void positionTapis(){
        boolean bool = false;
        while(!bool){
            int directionTapis = random.nextInt(4)-1;
            int directionTapis2 = random.nextInt(3)-1;
            bool = poseTapis(board, directionTapis, directionTapis2);
        }
    }

    private Joueur calculWinner(){
        if(white.getArgent() <= 0) {
            return black;
        }
        if(black.getArgent() <= 0) {
            return white;
        }
        board.getPointTapis(white, black);

        return (white.getArgent() > black.getArgent()) ? white : black;
    }

    private void changementJoueur(){
        joueurActif = (joueurActif == white) ? black : white;
        System.out.println("C'est au tour de "+joueurActif.getPseudo());
    }

    private boolean poseTapis(Board board, int pos1, int pos2) {

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

        return board.poseTapis(this.joueurActif, x1, y1, x2, y2);
    }
}

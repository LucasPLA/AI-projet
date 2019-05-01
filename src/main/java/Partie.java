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
        while((black.getNbTapisRestants()>0 || white.getNbTapisRestants()>0) && white.getArgent() > 0 && black.getArgent() > 0){
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

        while((black.getNbTapisRestants()>0 || white.getNbTapisRestants()>0) && white.getArgent() > 0 && black.getArgent() > 0){

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

        while((black.getNbTapisRestants()>0 || white.getNbTapisRestants()>0) && white.getArgent() > 0 && black.getArgent() > 0){

            // Random IA

            int direction = (int) (Math.random()*3);
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
            int direction = (int) (Math.random()*3);
            if (direction == 2) direction++;
            jouerCoup(board, direction);
            positionTapis();
            joueurActif = (joueurActif == white) ? black : white;
        }
        return calculWinner();
    }

    public boolean jouerUnCoupComplet(int direction, int nbCases, int pos1, int pos2) { // Pour le MCTS (jouer un coup pour l'expansion des noeuds)
        if (direction == 2) direction++;
        boolean v = jouerCoupDetermine(board, joueurActif, direction, nbCases, pos1, pos2);
        joueurActif = (joueurActif == white) ? black : white;
        return v;
    }

    public int jouerCoup(Board board, int direction) { // Bouge le vendeur selon la direction indiquée d'un nombre de case aléatoire
        int nbCases = ((int)(Math.random()*6))+1; // Random roll
        nbCases = (nbCases >4) ? nbCases - 3 : nbCases;

        int newOrientation = (board.getOrientation() + direction) % 4;
        board.bougeVendeur(nbCases, newOrientation, this.joueurActif);
        return nbCases-1;
    }

    public void jouerCoupMCTS(){
        IA ia = new IA(this, true);
        int wb = white.getArgent();
        int bb = black.getArgent();
        int choice = ia.moveChoice();
        int wa = white.getArgent();
        int ba = black.getArgent();
        white.setArgent(wb-wa);
        black.setArgent(bb-ba);
        int direction = (choice);
        if (direction == 2) direction++;
        int nbC = jouerCoup(board, direction);
        Display.afficheBoard(board);
        Display.afficheJoueurs(white, black);
        int pos = ia.poseChoice((choice), nbC);
        boolean v = poseTapis(board, (pos/3)-1,(pos%3)-1);
        if(!v){
            positionTapis();
        }
    }

    public boolean jouerCoupDetermine(Board board, Joueur joueurActif, int direction, int nbCases, int pos1, int pos2) { // Bouge le vendeur selon la direction indiquée d'un nombre de case déterminé
        int newOrientation = (board.getOrientation() + direction) % 4;
        board.bougeVendeur(nbCases, newOrientation, joueurActif);
        boolean test = poseTapis(board, pos1, pos2);
        return test;
    }

    public void coupDuJoueur(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Entrez la direction (0: devant, 1:droite, 3:gauche) :");
        String str = sc.nextLine();

        jouerCoup(board, Integer.parseInt(str));
        Display.afficheBoard(board);
        Display.afficheJoueurs(white, black);

        Boolean bool = false;

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

    public void positionTapis(){
        boolean bool = false;
        while(!bool){
            int directionTapis = ((int) (Math.random()*4))-1;
            int directionTapis2 = ((int) (Math.random()*3))-1;
            bool = poseTapis(board, directionTapis, directionTapis2);
        }
    }

    public Joueur calculWinner(){
        if(white.getArgent() <= 0) {
            return black;
        }
        if(black.getArgent() <= 0) {
            return white;
        }
        board.getPointTapis(white, black);

        return (white.getArgent() > black.getArgent()) ? white : black;
    }

    public void changementJoueur(){
        joueurActif = (joueurActif == white) ? black : white;
        System.out.println("C'est au tour de "+joueurActif.getPseudo());
    }

    public boolean poseTapis(Board board, int pos1, int pos2) {

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

        return board.poseTapis(this.joueurActif, x1, y1, x2, y2);
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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Board {

    // Etat général du plateau de jeu :
    private Tapis[][] board;
    // Dimension du plateau
    private int dimension;
    // Position de asam :
    private int xpos;
    private int ypos;

    /**
     *     0
     *     |
     * 3 <- -> 1
     *     |
     *     2
     */
    private int orientation;

    public Board(int dimension) {
        this.dimension = dimension;
        this.board = new Tapis[dimension][dimension];
        for(int i=0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                this.board[i][j] = null;
            }
        }

        this.xpos = (dimension - dimension/2) - 1;
        this.ypos = (dimension - dimension/2) - 1;
        this.orientation = 0;
    }

    public int getOrientation(){
        return orientation;
    }

    public int getDimension() {
        return dimension;
    }

    public Tapis getCase(int xpos, int ypos) {
        return this.board[xpos][ypos];
    }

    public boolean isAsam(int x, int y){
        return (x == xpos && y == ypos);
    }

    //TODO : revoir
    public int choixAsam(){
        if(board[xpos][ypos] == null){
            // Asam n'est pas sur un tapis
            return orientation;
        } else if(board[xpos][ypos].getPossesseur().getColor() == "x "){
            // Asam est sur un tapis noir
            return 4 + orientation;
        } else {
            // Asam est sur un tapis blanc
            return 8 + orientation;
        }
    }

    public void poseTapis(Joueur joueur, int xpos1, int ypos1, int xpos2, int ypos2) {
        // verifie les deux moitiés sont bien adjacente et valides
        if(this.estValide(xpos1, ypos1) && this.estValide(xpos2, ypos2) && Board.isAdjacent(xpos1, ypos1, xpos2, ypos2)) {
            throw new RuntimeException();
        }

        // verifier que le tapis est bien a coté d'asam
        if(!(Board.isAdjacent(this.xpos, this.ypos, xpos1, ypos1) || Board.isAdjacent(this.xpos, this.ypos, xpos2, ypos2))) {
            throw new RuntimeException();
        }

        // vérifie que tu ne recouvre pas un tapis complet
        if((this.board[xpos1][ypos1].getXcomplement() == xpos2) && (this.board[xpos1][ypos1].getYcomplement() == ypos2) && !this.board[xpos1][ypos1].isComplementRecouvert()) {
            throw new RuntimeException();
        }

        // indique au voisin que l'on recouvre que son autre moitié va être recouverte
        if((this.board[xpos1][ypos1] != null) && (!this.board[xpos1][ypos1].isComplementRecouvert())) {
            int x = this.board[xpos1][ypos1].getXcomplement();
            int y = this.board[xpos1][ypos1].getYcomplement();
            this.board[x][y].setComplementRecouvert();
        }
        // pose le voisin
        this.board[xpos1][ypos1] = new Tapis(joueur, xpos2, ypos2);

        // la même pour le deuxième tapis
        if((this.board[xpos2][ypos2] != null) && (!this.board[xpos2][ypos2].isComplementRecouvert())) {
            int x = this.board[xpos2][ypos2].getXcomplement();
            int y = this.board[xpos2][ypos2].getYcomplement();
            this.board[x][y].setComplementRecouvert();
        }
        this.board[xpos2][ypos2] = new Tapis(joueur, xpos1, ypos1);

        joueur.decrementTapis();
    }

    public void bougeVendeur(int longueur, int newOrientation, Joueur joueurActif) {
        int newXpos = this.xpos;
        int newYpos = this.ypos;

        if((newOrientation % 2) == 0) {
            newYpos -= (1-newOrientation) * longueur;
        } else {
            newXpos += (2-newOrientation) * longueur;
        }

        if(estValide(newXpos, newYpos)){
            xpos = newXpos;
            ypos = newYpos;
        } else {
            int[] pos = gererDebordement(newXpos, newYpos);
            xpos = pos[0];
            ypos = pos[1];
        }

        // paye le joueur possesseur du tapis sur lequel on finit
        if (this.board[this.xpos][this.ypos] != null) {
            if (!this.board[this.xpos][this.ypos].getPossesseur().equals(joueurActif))
                joueurActif.payerJoueur(this.nbTapisAdjacents(), this.board[this.xpos][this.ypos].getPossesseur());
        }
    }

    public int nbTapisAdjacents() {
        List<Tapis> tapisVisites = new ArrayList<Tapis>();
        Deque<Tapis> tapisAVisiter = new ArrayDeque<Tapis>();
        int compteur = 0;

        tapisAVisiter.addFirst(this.board[this.xpos][this.ypos]);
        while(!tapisAVisiter.isEmpty()) {
            Tapis tmp = tapisAVisiter.removeLast();
            compteur++;
            tapisVisites.add(tmp);

            if(this.estValide(tmp.getX() + 1, tmp.getY()) && (this.board[tmp.getX() + 1][tmp.getY()]) != null) {
                Tapis tapis = this.board[tmp.getX() + 1][tmp.getY()];
                if(tapisVisites.contains(tapis)) {
                    if(tapis.getPossesseur().equals(this.board[this.xpos][this.ypos].getPossesseur())) {
                        tapisAVisiter.addFirst(tapis);
                    }
                }
            }

            if(this.estValide(tmp.getX() - 1, tmp.getY()) && (this.board[tmp.getX() - 1][tmp.getY()]) != null) {
                Tapis tapis = this.board[tmp.getX() - 1][tmp.getY()];
                if(tapisVisites.contains(tapis)) {
                    if(tapis.getPossesseur().equals(this.board[this.xpos][this.ypos].getPossesseur())) {
                        tapisAVisiter.addFirst(tapis);
                    }
                }
            }

            if(this.estValide(tmp.getX(), tmp.getY() - 1) && (this.board[tmp.getX()][tmp.getY() - 1]) != null) {
                Tapis tapis = this.board[tmp.getX()][tmp.getY() - 1];
                if(tapisVisites.contains(tapis)) {
                    if(tapis.getPossesseur().equals(this.board[this.xpos][this.ypos].getPossesseur())) {
                        tapisAVisiter.addFirst(tapis);
                    }
                }
            }

            if(this.estValide(tmp.getX(), tmp.getY() + 1) && (this.board[tmp.getX()][tmp.getY() + 1]) != null) {
                Tapis tapis = this.board[tmp.getX()][tmp.getY() + 1];
                if(tapisVisites.contains(tapis)) {
                    if(tapis.getPossesseur().equals(this.board[this.xpos][this.ypos].getPossesseur())) {
                        tapisAVisiter.addFirst(tapis);
                    }
                }
            }
        }

        return compteur;
    }

    // En partant du principe que dimension est impaire...
    public int[] gererDebordement(int x, int y){
        // Depassement en abscisse
        if(x<0 || x>(this.dimension - 1)) {

            this.orientation = 2 - this.orientation;
            int parité = (y % 2 == 0) ? -1 : 1;

            if (x < 0 && y > 0) {
                y = y + parité;
                x = -x - 1;
            }
            else if ((x > this.dimension - 1) && (y < this.dimension - 1)) {
                y = y + parité;
                x = (this.dimension - 1) - (x - this.dimension);
            }
            else { //situation où l'on est dans un coin
                this.orientation = 4 - this.orientation;
                if(y < 0) {
                    y = -x - 1;
                    x = 0;
                } else {
                    y = (this.dimension - 1) - (x - this.dimension);
                    x = (this.dimension - 1);
                }
            }
        } else {
            int[] temp = this.gererDebordement(y, x);

            x = temp[1];
            y = temp[0];
            this.orientation = 3 - this.orientation;

        }
        int[] res = {x, y};
        return res;
    }

    // retourne vrai si la case est bien dans les dimensions du tableau
    public boolean estValide(int x, int y) {
        return (x>=0 && x<=(dimension-1) && y>=0 && y<=(dimension-1));
    }

    //
    public static boolean isAdjacent(int x1, int y1, int x2, int y2) {
        return (((Math.abs(x1-x2) == 1) && (Math.abs(y1-y2) == 0)) || ((Math.abs(x1-x2) == 0) && (Math.abs(y1-y2) == 1)));
    }
}

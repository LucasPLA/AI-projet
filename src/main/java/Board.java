public class Board {

    // Etat général du plateau de jeu :
    private Joueur[][] board;
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
        this.board = new Joueur[dimension][dimension];
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

    public Joueur getCase(int xpos, int ypos) {
        return this.board[xpos][ypos];
    }

    // retourne vrai si la position en paramètre est celle d'asam
    public boolean estSousAsam(int x, int y){
        return (x == xpos && y == ypos);
    }

    //TODO a refaire
    public int choixAsam(){
        if(board[xpos][ypos] == null){
            // Asam n'est pas sur un tapis
            return orientation;
        } else if(board[xpos][ypos].getColor() == "x "){
            // Asam est sur un tapis noir
            return 4 + orientation;
        } else {
            // Asam est sur un tapis blanc
            return 8 + orientation;
        }
    }

    public void poseTapis(Joueur joueur, int xpos, int ypos) {
        this.board[xpos][ypos] = joueur;
        joueur.decrementTapis();
    }

    public void bougeVendeur(int longueur, int newOrientation) {
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
    }

    // En partant du principe que dimension est impaire...
    public int[] gererDebordement(int x, int y){
        // Depassement en abscisse
        if(x<0 || x>(dimension - 1)){
            // Negatif
            if(x<0){
                if(y== (dimension - 1)){
                    y = dimension + x;
                    x = 0;
                } else {
                    x = -x - 1;
                    y = y - (((y % 2) * 2) - 1);
                }
            // Positif
            } else {
                if(y == 0){
                    y = x - dimension;
                    x = dimension - 1;
                } else {
                    x = dimension - 1 - (x - dimension);
                    y = y + (((y % 2) * 2) - 1);
                }
            }
        // Depassement en ordonnée
        } else {
            // Negatif
            if(y<0){
                if(x == (dimension - 1)){
                    x = dimension + y;
                    y = 0;
                }
                else {
                    x = x - (((x % 2) * 2) - 1);
                    y = -y - 1;
                }
            // Positif
            } else {
                if(x == 0) {
                    x = y - dimension;
                    y = dimension - 1;
                } else {
                    x = x + (((x % 2) * 2) - 1);
                    y = dimension - 1 - (y - dimension);
                }
            }
        }
        int[] res = {x, y};
        return res;
    }

    public boolean estValide(int x, int y) {
        return (x>=0 && x<=(dimension-1) && y>=0 && y<=(dimension-1));
    }
}

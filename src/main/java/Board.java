public class Board {

    private Joueur[][] board;
    private int dimension;
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

        // TODO tests si le vendeur dépasse du terrain
    }
}

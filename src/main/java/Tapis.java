public class Tapis {

    private Joueur possesseur;
    private int x, y;
    private int xcomplement, ycomplement;
    private boolean complementRecouvert;

    public Tapis(Joueur possesseur, int xcomplement, int ycomplement) {
        this.possesseur = possesseur;
        this.xcomplement = xcomplement;
        this.ycomplement = ycomplement;
        this.complementRecouvert = false;
    }

    public Joueur getPossesseur() {
        return possesseur;
    }

    public int getXcomplement() {
        return xcomplement;
    }

    public int getYcomplement() {
        return ycomplement;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isComplementRecouvert() {
        return complementRecouvert;
    }

    public void setComplementRecouvert() {
        this.complementRecouvert = !this.complementRecouvert;
    }

}

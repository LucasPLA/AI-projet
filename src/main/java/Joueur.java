public class Joueur {

    private int nbTapisRestants;
    private String pseudo;
    private int argent;
    private String color;

    public Joueur(String pseudo, String couleur) {
        this.pseudo = pseudo;
        this.nbTapisRestants = 15;
        this.argent = 30;
        this.color = couleur;
    }

    public int getNbTapisRestants() {
        return nbTapisRestants;
    }

    public String getPseudo() {
        return pseudo;
    }

    public int getArgent() {
        return argent;
    }

    public String getColor() {return color;}

    public void decrementTapis() {
        this.nbTapisRestants--;
    }

    public void setArgent(int argent) {
        this.argent = argent;
    }

    public void payerJoueur(int argent, Joueur joueur) {
        this.argent -= argent;
        joueur.setArgent(argent);
    }
}

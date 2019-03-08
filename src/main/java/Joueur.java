public class Joueur {

    private int nbTapisRestants;
    private String pseudo;
    private int argent;

    public Joueur(String pseudo) {
        this.pseudo = pseudo;
        this.nbTapisRestants = 15;
        this.argent = 30;
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

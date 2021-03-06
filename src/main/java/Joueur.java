import java.util.Objects;

public class Joueur {

    private int nbTapisRestants;
    private final String pseudo;
    private int argent;
    private final String color;

    public Joueur(String pseudo, String couleur) {
        this.pseudo = pseudo;
        this.nbTapisRestants = 15;
        this.argent = 30;
        this.color = couleur;
    }

    public Joueur(Joueur j){
        this.pseudo = j.pseudo;
        this.nbTapisRestants = j.nbTapisRestants;
        this.argent = j.argent;
        this.color = j.color;
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
        this.argent += argent;
    }

    public void payerJoueur(int argent, Joueur joueur) {
        this.argent -= argent;
        joueur.setArgent(argent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Joueur)) return false;
        Joueur joueur = (Joueur) o;
        return getNbTapisRestants() == joueur.getNbTapisRestants() &&
                getArgent() == joueur.getArgent() &&
                Objects.equals(getPseudo(), joueur.getPseudo()) &&
                Objects.equals(getColor(), joueur.getColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNbTapisRestants(), getPseudo(), getArgent(), getColor());
    }
}

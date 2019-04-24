import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class IA { // MCTS

    //static Random r = new Random(); // Pour éviter les cas d'égalité
    static int nActions = 3; // Nombre d'action maximum possible (seul la direction est prise en compte pour l'instant)
    static double epsilon = 1e-6; // Pour éviter les divisions par 0

    IA[] children; // Noeuds enfants
    int nVisits; // Nb de visite du noeud
    int totValue; // Nb de partie gagnée dans le noeud
    Partie game;

    public IA(Partie game){
        children = null;
        nVisits = 0;
        totValue = 0;
        this.game = new Partie(game);
    }

    public int moveChoice(){
        double time;
        time = System.currentTimeMillis();
        int nbPartie = 0;
        while((System.currentTimeMillis() - time) < 10000){
            this.selectAction();
        }
        double max = -1;
        int choice = 0;
        for(int i = 0; i<nActions; i++){
            if(this.children[i].totValue > max){
                choice = i;
                max = this.children[i].totValue;
            }
            System.out.println("Dir "+i+" gagne "+this.children[i].totValue+" games");
        }
        System.out.println("L'IA a joué "+this.nVisits+" coups");
        return choice;
    }

    public void selectAction() {
        List<IA> visited = new LinkedList<IA>(); // Liste des noeuds visité dans le parcours courant
        IA cur = this; // Noeud courant
        visited.add(this);
        while (!cur.isLeaf()) { // On descend jusqu'à atteindre une feuille
            cur = cur.select(); // Sélection du noeud enfant
            visited.add(cur);
        }
        IA newNode;
        if(cur.nVisits != 0){
            cur.expand(); // Expansion si la feuille a déjà été visitée
            newNode = cur.select();
            visited.add(newNode);
        } else {
            newNode = cur;
        }
        int value = rollOut(newNode); // Partie aléatoire joué depuis le noeud choisi
        for (IA node : visited) { // Maj des noeuds visité
            node.updateStats(value);
        }
    }

    public void expand() {
        Partie p;
        children = new IA[nActions];
        for (int i=0; i<nActions; i++) {
            p = new Partie(this.game);
            p.jouerUnCoupComplet(i);
            children[i] = new IA(p);
        }
    }

    private IA select() {
        IA selected = null;
        double uctValue;
        double bestValue = Double.MIN_VALUE;
        for (IA c : children) {
            if(c.nVisits!=0){
                uctValue = (c.totValue / c.nVisits) +
                        (Math.sqrt(2) * (Math.sqrt(Math.log(nVisits+1) / c.nVisits)));
            } else {
                uctValue = Double.MAX_VALUE;
            }

            if (uctValue > bestValue) {
                selected = c;
                bestValue = uctValue;
            }
        }
        return selected;
    }

    public boolean isLeaf() {
        return children == null;
    }

    public int rollOut(IA a) {
        Partie p = new Partie(a.game);
        if(p.jouerPartieAleatoire().getPseudo() == "black"){
            return 1;
        }
        return 0;
    }

    public void updateStats(double value) {
        nVisits++;
        totValue += value;
    }

    public int arity() {
        return children == null ? 0 : children.length;
    }

    /*public Joueur partieAleatoire() {
        Partie partie = new Partie(new Joueur("white", "o"), new Joueur("black", "x"), new Board(7));    }*/

}

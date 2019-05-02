import java.util.LinkedList;
import java.util.List;

public class IA { // MCTS

    //static Random r = new Random(); // Pour éviter les cas d'égalité
    private static final int nActions = 144; // Nombre d'action maximum possible (seul la direction est prise en compte pour l'instant)
    //static double epsilon = 1e-6; // Pour éviter les divisions par 0

    IA[] children; // Noeuds enfants
    private int nVisits; // Nb de visite du noeud
    int nWins; // Nb de partie gagnée dans le noeud
    private final Partie game; // Etat de la partie dans le neoud
    private final boolean valid; // Noeud invalide si position invalide

    public IA(Partie game, boolean v){
        children = null;
        nVisits = 0;
        nWins = 0;
        valid = v;
        this.game = new Partie(game);
        // System.out.println(this.game + ";" + game); //TODO
    }

    public int moveChoice(){ // Choix du movement de l'IA à partir du MCTS
        System.out.println("L'IA choisi la meilleure orientation à partir du MCTS :");
        double time;
        time = System.currentTimeMillis();
        int nbPartie = 0;
        while((System.currentTimeMillis() - time) < 1000){ // L'algo du MCTS tourne pendant 10 seconde
            this.selectAction();
        }
        int max = Integer.MIN_VALUE;
        int choice = 0;
        int val;
        int sum = 0;
        for(int i = 0; i<144; i++){ // Compte le nombre de victoire pour chaque action et choisi la plus victorieuse
            val = children[i].nWins;
            sum+=val;
            if(i%48==47){
                System.out.println("La direction "+(i/48)+" gagne "+ sum +" points");
                if(sum > max){
                    choice = (i/48);
                    max = sum;
                }
                sum = 0;
            }

        }
        System.out.println("L'IA a joué "+this.nVisits+" parties");
        System.out.println("Elle choisit la direction "+(choice)+" qui semble la plus prometteuse");
        return choice;
    }

    public int poseChoice(int i, int j) {
        System.out.println("L'IA récupère la meilleure position pour son tapis depuis le MCTS");
        int offset = ((i*48)+(j*12));
        int max = -1;
        int choice = 0;
        int val;
        for(int k = offset; k<offset+12; k++){
            val = children[k].nWins;
            if(val > max){
                choice = k;
                max = val;
            }
            System.out.println("La position ("+(((k%12)/3)-1)+";"+((k%3)-1)+") remporte "+val+" points");
        }
        return choice%12;
    }

    private void selectAction() {
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

    private void expand() { // Expansion du noeud
        Partie p;
        boolean v;
        children = new IA[nActions];
        for (int i=0; i<nActions; i++) {
            p = new Partie(this.game);
            v = p.jouerUnCoupComplet((i/48), (((i%48)/12)+1), (((i%12)/3)-1), ((i%3))-1); // (direction, nbCases, posTapis, orientationTapis
            children[i] = new IA(p, v);
        }
    }

    private IA select() { // Selection du noeud enfant
        IA selected = null;
        double uctValue;
        double bestValue = Double.MIN_VALUE;
        for (IA c : children) {
            if(c.valid){
                if(c.nVisits!=0){
                    uctValue = ((double) c.nWins / c.nVisits) +
                            (Math.sqrt(2) * (Math.sqrt(Math.log(nVisits+1) / c.nVisits)));
                } else {
                    uctValue = Double.MAX_VALUE;
                }

                if (uctValue > bestValue) {
                    selected = c;
                    bestValue = uctValue;
                }
            }
        }
        return selected;
    }

    private boolean isLeaf() {
        return children == null;
    }

    private int rollOut(IA a) { // Dérouler une partie aléatoire
        Partie p = new Partie(a.game);
        if(p.jouerPartieAleatoire().getPseudo().equals("black")){
            return 1;
        }
        return -1;
    }

    private void updateStats(double value) {
        nVisits++;
        nWins += value;
    }
}

import java.util.LinkedList;
import java.util.List;

public class IA { // MCTS

    //static Random r = new Random(); // Pour éviter les cas d'égalité
    static int nActions = 12; // Nombre d'action maximum possible (seul la direction est prise en compte pour l'instant)
    //static double epsilon = 1e-6; // Pour éviter les divisions par 0

    IA[] children; // Noeuds enfants
    int nVisits; // Nb de visite du noeud
    int nWins; // Nb de partie gagnée dans le noeud
    Partie game;
    boolean ori; // MCTS pour l'orientation ou la pose
    boolean valid; // La pose est elle valide

    public IA(Partie game, boolean ori, boolean valid){
        children = null;
        nVisits = 0;
        nWins = 0;
        this.ori = ori;
        this.valid = valid;
        this.game = new Partie(game);
    }

    public int moveChoiceOri(){ // Choix de l'orientation de l'IA à partir du MCTS
        System.out.println("L'IA choisi la meilleure orientation à partir du MCTS :");
        runMCTS();
        int max = -1;
        int sum = 0;
        int choice = 0;
        for(int i = 0; i<3; i++){ // Compte le nombre de victoire pour chaque action et choisi la plus victorieuse
            sum = 0;
            for(int j = 0; j<4; j++){
                sum += this.children[(4*i)+j].nWins;
            }
            if(sum > max){
                choice = i;
                max = sum;
            }
            System.out.println("La direction "+i+" gagne "+ sum +" parties virtuelles");
        }
        System.out.println("L'IA a joué "+this.nVisits+" parties");
        System.out.println("Elle choisit la direction "+choice+" qui semble la plus prometteuse");
        return choice;
    }

    public int moveChoicePose(){ // Choix de la position du tapis
        System.out.println("L'IA choisi la meilleure position pour son tapis à partir du MCTS :");
        runMCTS();
        int max = -1;
        int choice = 0;
        for(int i = 0; i<4; i++){ // Compte le nombre de victoire pour chaque action et choisi la plus victorieuse
            for(int j = 0; j<3; j++){
                if(children[(3*i)+j].valid){
                    System.out.println("La postion ("+(i-1)+","+(j-1)+") gagne "+ this.children[(3*i)+j].nWins +" parties virtuelles");
                    if(this.children[(3*i)+j].nWins > max){
                        choice = (3*i)+j;
                        max = this.children[(3*i)+j].nWins;
                    }
                } else {
                    System.out.println("La postion ("+(i-1)+","+(j-1)+") est invalide");
                }
            }
        }
        System.out.println("L'IA a joué "+this.nVisits+" parties");
        System.out.println("Elle choisit la position ("+((choice/3)-1)+","+((choice%3)-1)+") qui semble la plus prometteuse");
        return choice;
    }

    public void runMCTS(){
        double time;
        time = System.currentTimeMillis();
        int nbPartie = 0;
        while((System.currentTimeMillis() - time) < 1000){ // L'algo du MCTS tourne pendant 10 seconde
            this.selectAction();
        }
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
            if(ori){
                cur.expandOri(); // Expansion si la feuille a déjà été visitée
            } else {
                cur.expandPose();
            }
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

    public void expandOri() { // Expansion du noeud
        Partie p;
        children = new IA[nActions];
        for (int i=0; i<nActions; i++) {
            p = new Partie(this.game);
            p.jouerUnCoupComplet((i/4), ((i%4)+1)); // Joue un coup avec la direction et le nombre de cases indiqués
            children[i] = new IA(p, true, true);
        }
    }

    public void expandPose(){
        Partie p;
        boolean v;
        children = new IA[nActions];
        for (int i=0; i<nActions; i++) {
            p = new Partie(this.game);
            v = p.poseTapis(p.board, ((i/3)-1), ((i%3)-1)); // Joue un demi coup en posant le tapis à la position indiquée
            children[i] = new IA(p, true, v);
        }
    }

    private IA select() { // Selection du noeud enfant
        IA selected = null;
        double uctValue;
        double bestValue = Double.MIN_VALUE;
        for (IA c : children) {
            if(c.valid){
                if(c.nVisits!=0){
                    uctValue = (c.nWins / c.nVisits) +
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

    public boolean isLeaf() {
        return children == null;
    }

    public int rollOut(IA a) { // Dérouler une partie aléatoire
        Partie p = new Partie(a.game);
        if(p.jouerPartieAleatoire().getPseudo() == "black"){
            return 1;
        }
        return 0;
    }

    public void updateStats(double value) {
        nVisits++;
        nWins += value;
    }

    public int arity() {
        return children == null ? 0 : children.length;
    }

    /*public Joueur partieAleatoire() {
        Partie partie = new Partie(new Joueur("white", "o"), new Joueur("black", "x"), new Board(7));    }*/

}

public class Main {

    public static void main(String[] args) {

        // Calcul du nombre de partie joué en 10 secondes

       /*double time;
       time = System.currentTimeMillis();
       int nbPartie = 0;
       while((System.currentTimeMillis() - time) < 10000){
           Partie partie = new Partie(new Joueur("white", "o "), new Joueur("black", "x "), new Board(7));
           Joueur gagnant = partie.jouerPartieAleatoire();
           //System.out.println("gagnant"+gagnant.getPseudo());
           nbPartie++;
       }
       System.out.println("Nombre de partie : "+nbPartie);*/

        Partie partie = new Partie(new Joueur("white", "o "), new Joueur("black", "x "), new Board(7));

       //Joueur winner = partie.jouerPartieJoueurVsIA(); // Joueur VS IA
       //Joueur winner = partie.jouerPartieJoueur(); // Joueur VS Joueur
       Joueur winner = partie.jouerRandomIaVsMctsIa(); // Random VS MCTS

        System.out.println("Le gagnant est "+winner.getPseudo());

        /*for(int i = 0; i<144; i++){
            System.out.println((i/48) +","+ (((i%48)/12)+1) +","+ ((i%12)/3) +","+ (i%3));
        }

        System.out.println(((int) (Math.random()*4))-1);
        System.out.println(((int) (Math.random()*3))-1);*/

    }
}

//TODO :
/*
- stream java
- IA

1er milestone :
    - avoir une version en random qui marche
 */
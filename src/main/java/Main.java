import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

       double time;
       time = System.currentTimeMillis();
       int nbPartie = 0;
       while((System.currentTimeMillis() - time) < 10000){
           Partie partie = new Partie(new Joueur("white", "o "), new Joueur("black", "x "), new Board(7));
           Joueur gagnant = partie.jouerPartieAleatoire();
           //System.out.println("gagnant"+gagnant.getPseudo());
           nbPartie++;
       }
       System.out.println("Nombre de partie : "+nbPartie);
       /*System.out.println((int) (Math.random()*3));
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
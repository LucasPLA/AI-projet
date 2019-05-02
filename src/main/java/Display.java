public class Display {

    private static final String[] asamDisplay = {"\u1431", "\u1433", "\u142F", "\u1438"};

    /* Affichage de asam :
Dans l'ordre :
0-3 : asam n'est pas sur un tapis orientation haut, gauche, bas, droite
4-7 : asam est sur un tapis noir idem pour l'orientation
8-11 : asam est sur un tapis blanc idem pour l'orientation
 */

    // Mets à jour l'état du plateau pour la console
    public static void afficheBoard(Board board){ // Nouvelle affichage avec les bordures limites
        StringBuilder display = new StringBuilder("    0 1 2 3 4 5 6\n");
        display.append("    ");
        for(int i = 0; i<(board.getDimension()/2)+1; i++){
            display.append("\u2554\u2550\u2557 ");
        }
        display.append("\n");

        for(int i = 0; i<board.getDimension(); i++){
            display.append(i).append(" ");
            if(i%2==0){
                display.append("\u2554 ");
            } else {
                display.append("\u255A ");
            }
            for(int j = 0; j<board.getDimension(); j++){
                if(board.isAsam(j,i)){
                    display.append(asamDisplay[board.getOrientation()]).append(" ");
                } else {
                    if(board.getCase(j,i) == null){
                        display.append(". ");
                    } else {
                        display.append(board.getCase(j,i).getPossesseur().getColor());
                    }
                }
            }
            if(i%2==0){
                display.append("\u255D ");
            } else {
                display.append("\u2557 ");
            }
            display.append("\n");
        }
        display.append("  ");
        for(int i = 0; i<(board.getDimension()/2)+1; i++){
            display.append("\u255A\u2550\u255D ");
        }
        System.out.println(display);
    }

    public static void afficheJoueurs(Joueur j1, Joueur j2) {
        System.out.println("Le joueur "+j1.getPseudo()+" a "+j1.getArgent()+" dirhams et " + j1.getNbTapisRestants()+ " tapis restants");
        System.out.println("Le joueur "+j2.getPseudo()+" a "+j2.getArgent()+" dirhams et " + j2.getNbTapisRestants()+ " tapis restants");
    }
}

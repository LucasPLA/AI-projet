public class Display {

    private static String[] asamDisplay = {"\u1431", "\u1433", "\u142F", "\u1438"};
    // , "\u15D7 ", "\u15D8", "\u15D6", "\u15DB", "\u15CB", "\u15CC", "\u15CA", "\u15CF"

    public Display() {
    }

    /* Affichage de asam :
Dans l'ordre :
0-3 : asam n'est pas sur un tapis orientation haut, gauche, bas, droite
4-7 : asam est sur un tapis noir idem pour l'orientation
8-11 : asam est sur un tapis blanc idem pour l'orientation
 */

    // Mets à jour l'état du plateau pour la console
    public static void afficheBoard(Board board){
        String display = "  0 1 2 3 4 5 6\n";

        for(int i = 0; i<board.getDimension(); i++){
            display += (i) +" ";
            for(int j = 0; j<board.getDimension(); j++){
                if(board.isAsam(j,i)){
                    display += asamDisplay[board.getOrientation()]+" ";
                } else {
                    if(board.getCase(j,i) == null){
                        display += ". ";
                    } else {
                        display += board.getCase(j,i).getPossesseur().getColor();
                    }
                }
            }
            display += "\n";
        }
        System.out.println(display);
    }

    /*
        //TODO : revoir
    public int choixAsam(){
        if(board[xpos][ypos] == null){
            // Asam n'est pas sur un tapis
            return orientation;
        } else if(board[xpos][ypos].getPossesseur().getColor() == "x "){
            // Asam est sur un tapis noir
            return 4 + orientation;
        } else {
            // Asam est sur un tapis blanc
            return 8 + orientation;
        }
    }
     */
}

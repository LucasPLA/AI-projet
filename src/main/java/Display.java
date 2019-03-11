public class Display {

    private static String[] asamDisplay = {"\u1431", "\u1433", "\u142F", "\u1438", "\u15D7 ", "\u15D8", "\u15D6", "\u15DB", "\u15CB", "\u15CC", "\u15CA", "\u15CF"};

    public Display() {
    }

    /* Affichage de asam :
Dans l'ordre :
0-3 : asam n'est pas sur un tapis orientation haut, gauche, bas, droite
4-7 : asam est sur un tapis noir idem pour l'orientation
8-11 : asam est sur un tapis blanc idem pour l'orientation
 */

    // Mets à jour l'état du plateau pour la console
    public static String afficheBoard(Board board){
        String display = "";
        for(int i = 0; i<board.getDimension(); i++){
            for(int j = 0; j<board.getDimension(); j++){
                if(board.isAsam(i,j)){
                    display += asamDisplay[board.choixAsam()];
                } else {
                    if(board.getCase(i,j) == null){
                        display += "  ";
                    } else {
                        display += board.getCase(i,j).getPossesseur().getColor();
                    }
                }
            }
            display += "\n";
        }
        return display;
    }
}

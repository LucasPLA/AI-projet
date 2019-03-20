import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Board board1; // Plateau principal
    Joueur black; // Joueur noir
    Joueur white; // Joueur blanc
    
    @BeforeEach
    void setUp() {
        board1 = new Board(7);
        black = new Joueur("black", "x ");
        white = new Joueur("white", "o ");
    }

    @Test
    void testDebordementHaut() {
        int couple[] = board1.gererDebordement(2,-2);
        assertEquals(couple[1], 1);
        assertEquals(3, couple[0]);
        assertEquals(2, board1.getOrientation());
    }

    @Test
    void testDebordementDroite() {
        int couple[] = board1.gererDebordement(7,4);
        assertEquals(couple[0], 6);
        assertEquals(couple[1], 3);
        assertEquals(3, board1.getOrientation());
    }

    // Tests pour les debordements

    @Test
    void testDebordementGauche() {
        int couple[] = board1.gererDebordement(-2,2);
        assertEquals(couple[0], 1);
        assertEquals(couple[1], 3);
        assertEquals(1, board1.getOrientation());
    }

    @Test
    void testDebordementBas() {
        int couple[] = board1.gererDebordement(1,8);
        assertEquals(couple[0], 2);
        assertEquals(couple[1], 5);
        assertEquals(0, board1.getOrientation());
    }

    @Test
    void testDebordementTopRightCorner() {
        int couple[] = board1.gererDebordement(8,0);
        assertEquals(couple[1], 1);
        assertEquals(couple[0], 6);
        assertEquals(2, board1.getOrientation());

        couple = board1.gererDebordement(6, -2);
        assertEquals(couple[0], 5);
        assertEquals(couple[1], 0);
        assertEquals(3, board1.getOrientation());
    }

    @Test
    void testDebordementBottomLeftCorner() {
        int couple[] = board1.gererDebordement(0,7);
        assertEquals(couple[0], 0);
        assertEquals(couple[1], 6);
        assertEquals(1, board1.getOrientation());

        couple = board1.gererDebordement(-3, 6);
        assertEquals(couple[0], 0);
        assertEquals(couple[1], 4);
        assertEquals(0, board1.getOrientation());

    }

    // Tests sur la pose des tapis

    @Test
    void testPoseTapis() {
        Tapis[][] tapis = board1.poseTapis(black, 2, 3, 2, 2);
        assertEquals(tapis[2][3].getPossesseur(), black);
        assertEquals(tapis[2][2].getPossesseur(), black);
        assertNull(tapis[3][2]);
        assertNull(tapis[4][3]);
        assertNull(tapis[3][4]);
    }

    @Test
    void testPoseTapisErreur() {
        // Position invalide
        assertThrows(RuntimeException.class, () -> board1.poseTapis(black, -1, 3,2, 2));
        // Pas a cote d'asam
        assertThrows(RuntimeException.class, () -> board1.poseTapis(black, 2, 2,2, 1));
        // Tapis non adjacent
        assertThrows(RuntimeException.class, () -> board1.poseTapis(black, 2, 3,4, 3));
        // Recouvre un tapis complet
        board1.poseTapis(black, 2, 3, 2, 2);
        assertThrows(RuntimeException.class, () -> board1.poseTapis(white, 2, 3,2, 2));
    }

    @Test
    void testPoseMoitieTapisSurUnAutre() {
        Tapis[][] tapis = board1.poseTapis(black, 2, 3, 2, 2);
        tapis = board1.poseTapis(white, 2, 3, 1, 3);
        assertEquals(tapis[2][3].getPossesseur(), white);
        assertEquals(tapis[1][3].getPossesseur(), white);
        assertEquals(tapis[2][2].getPossesseur(), black);
    }

    @Test
    void testNbTapisAdjacent() {

        board1.poseTapis(white, 3, 2, 4, 2);
        board1.bougeVendeur(1, 0, white);
        board1.poseTapis(white, 3, 1, 2, 1);
        board1.poseTapis(white, 4,3,3,3);
        board1.bougeVendeur(1, 0, black);
        board1.bougeVendeur(1, 2, black);
        Display.afficheJoueurs(white, black);
        Display.afficheBoard(board1);


        assertEquals(6, board1.nbTapisAdjacents());
    }

}
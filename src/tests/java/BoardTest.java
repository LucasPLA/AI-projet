import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Board board1; // Plateau principal
    Joueur jb; // Joueur noir
    Joueur jw; // Joueur blanc
    
    @BeforeEach
    void setUp() {
        board1 = new Board(7);
        jb = new Joueur("black", "x ");
        jw = new Joueur("white", "o ");
    }

    @Test
    void testDebordementHaut() {
        int couple[] = board1.gererDebordement(2,-2);
        assertEquals(couple[0], 3);
        assertEquals(couple[1], 1);
    }

    @Test
    void testDebordementDroite() {
        int couple[] = board1.gererDebordement(7,4);
        assertEquals(couple[0], 6);
        assertEquals(couple[1], 3);
    }

    // Tests pour les debordements

    @Test
    void testDebordementGauche() {
        int couple[] = board1.gererDebordement(2,-2);
        assertEquals(couple[0], 3);
        assertEquals(couple[1], 1);
    }

    @Test
    void testDebordementBas() {
        int couple[] = board1.gererDebordement(1,8);
        assertEquals(couple[0], 2);
        assertEquals(couple[1], 5);
    }

    @Test
    void testDebordementTopRightCorner() {
        int couple[] = board1.gererDebordement(8,0);
        assertEquals(couple[0], 6);
        assertEquals(couple[1], 1);
        couple = board1.gererDebordement(6, -2);
        assertEquals(couple[0], 5);
        assertEquals(couple[1], 0);
    }

    @Test
    void testDebordementBottomLeftCorner() {
        int couple[] = board1.gererDebordement(0,7);
        assertEquals(couple[0], 0);
        assertEquals(couple[1], 6);
        couple = board1.gererDebordement(-3, 6);
        assertEquals(couple[0], 0);
        assertEquals(couple[1], 4);
    }

    // Tests sur la pose des tapis

    @Test
    void testPoseTapis() {
        Tapis[][] tapis = board1.poseTapis(jb, 2, 3, 2, 2);
        assertEquals(tapis[2][3].getPossesseur(), jb);
        assertEquals(tapis[2][2].getPossesseur(), jb);
        assertNull(tapis[3][2]);
        assertNull(tapis[4][3]);
        assertNull(tapis[3][4]);
    }

    @Test
    void testPoseTapisErreur() {
        // Position invalide
        assertThrows(RuntimeException.class, () -> board1.poseTapis(jb, -1, 3,2, 2));
        // Pas a cote d'asam
        assertThrows(RuntimeException.class, () -> board1.poseTapis(jb, 2, 2,2, 1));
        // Tapis non adjacent
        assertThrows(RuntimeException.class, () -> board1.poseTapis(jb, 2, 3,4, 3));
        // Recouvre un tapis complet
        board1.poseTapis(jb, 2, 3, 2, 2);
        assertThrows(RuntimeException.class, () -> board1.poseTapis(jw, 2, 3,2, 2));
    }

    @Test
    void testPoseMoitieTapisSurUnAutre() {
        Tapis[][] tapis = board1.poseTapis(jb, 2, 3, 2, 2);
        tapis = board1.poseTapis(jw, 2, 3, 1, 3);
        assertEquals(tapis[2][3].getPossesseur(), jw);
        assertEquals(tapis[1][3].getPossesseur(), jw);
        assertEquals(tapis[2][2].getPossesseur(), jb);
    }

    // Tests pour le compte du nombre de tapis adjacent

    @Test
    void testNbTapisAdjacent() {
          /* Initialisation du tapis :
        . . . . . . .
        . . . o . . .
        . x x o o x .
        . . x x o x .
        . . . x . . .
        . . . . x x .
        . . . . . . .
         */

        board1.setBoard(new Tapis(jb, 2, 2), 1, 2);
        board1.setBoard(new Tapis(jb, 1, 2), 2, 2);
        board1.setBoard(new Tapis(jb, 3, 3), 2, 3);
        board1.setBoard(new Tapis(jb, 2, 3), 3, 3);
        board1.setBoard(new Tapis(jb, 3, 3), 3, 4);
        board1.setBoard(new Tapis(jb, 3, 4), 3, 3);
        board1.setBoard(new Tapis(jb, 5, 3), 5, 2);
        board1.setBoard(new Tapis(jb, 5, 2), 5, 3);
        board1.setBoard(new Tapis(jb, 5, 5), 4, 5);
        board1.setBoard(new Tapis(jb, 4, 5), 5, 5);

        board1.setBoard(new Tapis(jw, 3, 2), 3, 1);
        board1.setBoard(new Tapis(jw, 3, 1), 3, 2);
        board1.setBoard(new Tapis(jw, 4, 3), 4, 2);
        board1.setBoard(new Tapis(jw, 4, 2), 4, 3);

        board1.setAsam(2,2);
        assertEquals(board1.nbTapisAdjacents(), 5);
        board1.setAsam(4,3);
        assertEquals(board1.nbTapisAdjacents(), 4);
    }


}
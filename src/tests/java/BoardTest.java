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

        /* Initialisation du tapis :
        . . . . . . .
        . . . . . . .
        . . . . . . .
        . . . . . . .
        . . . . . . .
        . . . . . . .
        . . . . . . .
         */
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

    // Tests la pose des tapis

    @Test
    void testPoseTapis() {
        Tapis[][] tapis = board1.poseTapis(jb, 2, 3, 2, 2);
        assertEquals(tapis[2][3].getPossesseur(), jb);
        assertEquals(tapis[2][2].getPossesseur(), jb);
        assertNull(tapis[3][2]);
        assertNull(tapis[4][3]);
        assertNull(tapis[3][4]);
    }

}
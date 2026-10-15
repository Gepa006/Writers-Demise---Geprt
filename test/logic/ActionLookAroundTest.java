package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testovací třída pro komplexní otestování třídy {@link ActionLookAround}.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025, 2025-12-13
 */
public class ActionLookAroundTest {
    private Game game;

    private Area area1;
    private Area area2;

    @BeforeEach
    public void setUp() {
        game = new Game();

        area1 = new Area("hala", "Toto je vstupní hala Nové budovy VŠE v Praze.");
        area2 = new Area("bufet", "Toto je bufet, v případě nouze zde najdeš pohotovostní zásobu krmiva.");

        game.getGameWorld().setCurrentArea(area1);
    }

    @Test
    public void testIncorrectParameters() {
        assertEquals("Tomu nerozumím, umím se rozhlédnout jen kolem sebe, ne na něco konkrétního.", game.processUserInput("rozhledni_se x"));
        assertEquals("Tomu nerozumím, umím se rozhlédnout jen kolem sebe, ne na něco konkrétního.", game.processUserInput("rozhledni_se x y"));
    }

    @Test
    public void testCorrectParameters() {
        assertEquals(area1.getFullDescription(), game.processUserInput("rozhledni_se"));

        game.getGameWorld().setCurrentArea(area2);
        assertEquals(area2.getFullDescription(), game.processUserInput("rozhledni_se"));
    }
}

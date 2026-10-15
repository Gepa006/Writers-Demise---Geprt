package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testovací třída pro ActionInvestigate.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class ActionInvestigateTest {
    private Game game;
    private Item item1;
    private Item item2;

    @BeforeEach
    public void setUp() {
        game = new Game();
        Area area1 = new Area("hala", "Hala.");
        Area area2 = new Area("bufet", "Bufet.");

        item1 = new Item("kreslo", "Starožitná sedačka.", false, Item.Type.OTHER);
        item2 = new Item("rum", "Láhev alkoholu.", Item.Type.POTION);

        area1.addExit(area2);
        area1.addItem(item1);
        area2.addItem(item2);

        game.getGameWorld().setCurrentArea(area1);
    }

    @Test
    public void testIncorrectParameters() {
        // Upraveno podle skutečné hlášky
        assertEquals("Co mám prozkoumat?", game.processUserInput("prozkoumej"));
        assertEquals("Nic takového tu nevidím.", game.processUserInput("prozkoumej neco"));

        // Test více parametrů
        assertEquals("Nic takového tu nevidím.", game.processUserInput("prozkoumej a b"));

        assertEquals("Nic takového tu nevidím.", game.processUserInput("prozkoumej " + item2.getName()));
    }

    @Test
    public void testCorrectParameters() {
        String expected1 = "Předmět '" + item1.getName() + "': " + item1.getDescription();
        assertEquals(expected1, game.processUserInput("prozkoumej " + item1.getName()));

        game.processUserInput("jdi bufet");

        String expected2 = "Předmět '" + item2.getName() + "': " + item2.getDescription();
        assertEquals(expected2, game.processUserInput("prozkoumej " + item2.getName()));
    }
}
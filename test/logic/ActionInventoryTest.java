package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testovací třída pro ověření funkčnosti příkazu 'inventar' (ActionInventory).
 * Testuje výpis obsahu inventáře na konzoli.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class ActionInventoryTest {
    private Game game;

    /**
     * Příprava hry před každým testem.
     */
    @BeforeEach
    public void setUp() {
        game = new Game();
    }

    /**
     * Testuje výpis prázdného inventáře.
     * Ověřuje, že výstup obsahuje informaci o prázdných rukou i kapsách.
     */
    @Test
    public void testEmptyInventory() {
        String result = game.processUserInput("inventar");
        assertTrue(result.contains("V rukou držíš: nic"));
        assertTrue(result.contains("V kapsách máš: nic"));
    }

    /**
     * Testuje výpis inventáře s předměty.
     * Ověřuje, že se ve výpisu správně zobrazí názvy přidaných předmětů.
     */
    @Test
    public void testFullInventory() {
        game.getGameWorld().getInventory().addItem(new Item("Mec", "Meč", Item.Type.WEAPON));
        game.getGameWorld().getInventory().addItem(new Item("Lektvar", "Lektvar", Item.Type.POTION));

        String result = game.processUserInput("inventar");
        assertTrue(result.contains("V rukou držíš: Mec"));
        assertTrue(result.contains("V kapsách máš: Lektvar"));
    }
}
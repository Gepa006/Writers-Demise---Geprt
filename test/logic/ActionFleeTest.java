package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testovací třída pro ověření funkčnosti příkazu 'utec' (ActionFlee).
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class ActionFleeTest {
    private Game game;

    /**
     * Příprava herního světa před každým testem.
     */
    @BeforeEach
    public void setUp() {
        game = new Game();
        Area start = new Area("start", "startovní lokace");
        Area cil = new Area("cil", "cílová lokace");
        start.addExit(cil);
        game.getGameWorld().setCurrentArea(start);
    }

    /**
     * Testuje pokus o útěk v situaci, kdy v lokaci není žádný nepřítel.
     * Hráč by měl zůstat na místě a dostat odpovídající hlášku.
     */
    @Test
    public void testFleeWithoutEnemy() {
        String result = game.processUserInput("utec");
        assertTrue(result.contains("Není před kým utíkat"));
        assertEquals("start", game.getGameWorld().getCurrentArea().getName());
    }

    /**
     * Testuje nemožnost útěku před finálním bossem (Spisovatelem).
     * I když je boss v místnosti, příkaz k útěku musí selhat se specifickou hláškou.
     */
    @Test
    public void testFleeFromBoss() {
        Area bossRoom = new Area("studovna", "Temná pracovna.");
        bossRoom.addCharacter("spisovatel", "Boss");
        game.getGameWorld().setCurrentArea(bossRoom);

        String result = game.processUserInput("utec");
        assertTrue(result.contains("Odsud není úniku"));
    }

    /**
     * Testuje garantovaný útěk s použitím Lektvaru mršnosti.
     * Pokud je aktivní efekt mršnosti, útěk se musí vždy podařit.
     */
    @Test
    public void testGuaranteedFleeWithPotion() {
        game = new Game();
        game.getGameWorld().setCurrentArea(game.getGameWorld().getCurrentArea().getExit("Zahrada_soch").getExit("Hlavní_brána").getExit("Vstupní_hala").getExit("Točité_schodiště"));

        game.getGameWorld().activateAgility();

        String result = game.processUserInput("utec");
        assertTrue(result.contains("Podařilo se ti proklouznout"));
        assertNotEquals("Točité_schodiště", game.getGameWorld().getCurrentArea().getName());
    }
}
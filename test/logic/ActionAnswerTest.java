package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testovací třída pro ověření logiky hádanek (příkaz odpovez).
 * Testuje interakci s NPC Vrátný.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class ActionAnswerTest {
    private Game game;

    /**
     * Inicializace hry a přesun hráče do Vstupní haly (kde je hádanka) před každým testem.
     */
    @BeforeEach
    public void setUp() {
        game = new Game();
        Area start = game.getGameWorld().getCurrentArea();
        Area zahrada = start.getExit("Zahrada_soch");
        Area brana = zahrada.getExit("Hlavní_brána");
        Area hala = brana.getExit("Vstupní_hala");

        game.getGameWorld().setCurrentArea(hala);
    }

    /**
     * Ověřuje chování příkazu v lokaci, kde není komu odpovídat.
     */
    @Test
    public void testNoRiddleHere() {
        game.getGameWorld().setCurrentArea(game.getGameWorld().getCurrentArea().getExit("Hlavní_brána"));

        String result = game.processUserInput("odpovez 33");
        assertTrue(result.contains("není nikdo"), "Měl by nahlásit, že tu není komu odpovídat.");
    }

    /**
     * Ověřuje reakci na špatnou odpověď.
     * Hráč by měl dostat chybovou hlášku a přijít o část zdraví.
     * Překážka (Vrátný) by měla zůstat na místě.
     */
    @Test
    public void testWrongAnswer() {
        int initialHP = game.getGameWorld().getHealth();

        String result = game.processUserInput("odpovez 50");

        assertTrue(result.contains("CHYBA") || result.contains("není správně"));
        assertTrue(game.getGameWorld().getHealth() < initialHP, "Za špatnou odpověď by měl přijít trest (zranění).");
        assertTrue(game.getGameWorld().getCurrentArea().containsCharacter("vratny"));
    }

    /**
     * Ověřuje reakci na správnou odpověď.
     * Vrátný by měl zmizet a uvolnit cestu.
     */
    @Test
    public void testCorrectAnswer() {
        String result = game.processUserInput("odpovez 33");

        assertTrue(result.contains("Správně") || result.contains("rozplynul"));
        assertFalse(game.getGameWorld().getCurrentArea().containsCharacter("vratny"));
    }
}
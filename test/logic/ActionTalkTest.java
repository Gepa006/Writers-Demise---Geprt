package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testovací třída pro ověření funkčnosti příkazu 'promluv' (ActionTalk).
 * Testuje dialogy se specifickými NPC a získávání předmětů.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class ActionTalkTest {
    private Game game;

    @BeforeEach
    public void setUp() {
        game = new Game();
        // Vytvoříme čistou testovací místnost
        Area mistnost = new Area("Testovací_místnost", "Popis");
        game.getGameWorld().setCurrentArea(mistnost);
    }

    /**
     * Testuje reakci na chybějící parametr.
     */
    @Test
    public void testNoParameters() {
        String result = game.processUserInput("promluv");
        assertEquals("S kým mám mluvit?", result);
    }

    /**
     * Testuje pokus o rozhovor s postavou, která v lokaci není.
     */
    @Test
    public void testCharacterNotHere() {
        String result = game.processUserInput("promluv nikdo");
        assertEquals("Postava 'nikdo' tady není.", result);
    }

    /**
     * Testuje rozhovor s Duchem Vency (úspěšné získání dýky).
     */
    @Test
    public void testTalkToGhostSuccess() {
        game.getGameWorld().getCurrentArea().addCharacter("duch_vency", "Duch");

        String result = game.processUserInput("promluv duch_vency");

        assertTrue(result.contains("Alice, vem si tuhle dýku"));
        assertTrue(game.getGameWorld().getInventory().containsItem("dyka"));
        // Duch by měl zmizet
        assertFalse(game.getGameWorld().getCurrentArea().containsCharacter("duch_vency"));
    }

    /**
     * Testuje rozhovor s Duchem Vency, když má hráč plné ruce (už má zbraň).
     */
    @Test
    public void testTalkToGhostFullInventory() {
        game.getGameWorld().getCurrentArea().addCharacter("duch_vency", "Duch");
        // Hráč už drží meč
        game.getGameWorld().getInventory().addItem(new Item("mec", "Meč", Item.Type.WEAPON));

        String result = game.processUserInput("promluv duch_vency");

        assertTrue(result.contains("Máš plné ruce!"));
        assertFalse(game.getGameWorld().getInventory().containsItem("dyka"));
        // Duch by tam měl zůstat
        assertTrue(game.getGameWorld().getCurrentArea().containsCharacter("duch_vency"));
    }

    /**
     * Testuje rozhovor s Kuchařkou (úspěšné získání lektvaru).
     */
    @Test
    public void testTalkToCookSuccess() {
        game.getGameWorld().getCurrentArea().addCharacter("kucharka", "Kuchařka");

        String result = game.processUserInput("promluv kucharka");

        assertTrue(result.contains("Tady máš Lektvar abstrakce"));
        assertTrue(game.getGameWorld().getInventory().containsItem("lektvar_abstrakce"));
    }

    /**
     * Testuje rozhovor s Kuchařkou při plných kapsách.
     */
    @Test
    public void testTalkToCookFullInventory() {
        game.getGameWorld().getCurrentArea().addCharacter("kucharka", "Kuchařka");
        // Naplníme kapsy
        game.getGameWorld().getInventory().addItem(new Item("l1", "L1", Item.Type.POTION));
        game.getGameWorld().getInventory().addItem(new Item("l2", "L2", Item.Type.POTION));

        String result = game.processUserInput("promluv kucharka");

        assertTrue(result.contains("Máš plné kapsy"));
        assertFalse(game.getGameWorld().getInventory().containsItem("lektvar_abstrakce"));
    }

    /**
     * Testuje specifické hlášky ostatních postav.
     */
    @Test
    public void testTalkToOtherNPCs() {
        game.getGameWorld().getCurrentArea().addCharacter("komornik", "K");
        assertEquals("Komorník: 'Zmiz, než tě něčím přetáhnu!'", game.processUserInput("promluv komornik"));

        game.getGameWorld().getCurrentArea().addCharacter("vratny", "V");
        String vratnyResult = game.processUserInput("promluv vratny");
        assertTrue(vratnyResult.contains("hádanku"));
    }

    /**
     * Testuje použití předložky "s" (promluv s kucharka).
     */
    @Test
    public void testTalkWithPreposition() {
        game.getGameWorld().getCurrentArea().addCharacter("kucharka", "Kuchařka");

        String result = game.processUserInput("promluv s kucharka");

        assertTrue(result.contains("Tady máš Lektvar abstrakce"));
    }
}
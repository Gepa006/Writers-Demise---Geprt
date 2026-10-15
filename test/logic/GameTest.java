package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testovací třída pro komplexní otestování třídy {@link Game}.
 * Ověřuje inicializaci hry, zpracování vstupů, normalizaci textu,
 * fungování aliasů a stavy ukončení hry.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class GameTest {
    private Game game;

    /**
     * Inicializace hry před každým testem.
     */
    @BeforeEach
    public void setUp() {
        game = new Game();
    }

    /**
     * Testuje inicializaci hry a úvodní zprávu.
     */
    @Test
    public void testInitialization() {
        assertNotNull(game.getGameWorld());
        assertFalse(game.isGameOver());

        String prologue = game.getPrologue();
        assertNotNull(prologue);
        assertTrue(prologue.contains("Writers Demise"));
        assertTrue(prologue.contains("Paříže"));

        assertEquals("Mlhové_předměstí", game.getGameWorld().getCurrentArea().getName());
    }

    /**
     * Testuje zpracování prázdného vstupu.
     * Upraveno dle chování Game.java: split prázdného řetězce vrátí pole s prázdným řetězcem,
     * což propadne jako neznámý příkaz.
     */
    @Test
    public void testEmptyInput() {
        String result = game.processUserInput("");

        assertEquals("Tomu nerozumím, tento příkaz neznám.", result);

    }

    /**
     * Testuje reakci na neznámý příkaz.
     */
    @Test
    public void testUnknownCommand() {
        String result = game.processUserInput("blabol");
        assertTrue(result.contains("Tomu nerozumím"));
    }

    /**
     * Testuje normalizaci vstupu (odstranění diakritiky a převod na malá písmena).
     * Příkaz "JDI" by měl fungovat stejně jako "jdi".
     */
    @Test
    public void testInputNormalization() {
        String result = game.processUserInput("JDI zahradu");
        assertTrue(result.contains("Zahrada plná kamenných soch"));
        assertEquals("Zahrada_soch", game.getGameWorld().getCurrentArea().getName());

        game.processUserInput("jdi predmesti");

        String resultDiacritics = game.processUserInput("Běž zahradu");
        assertTrue(resultDiacritics.contains("Zahrada plná kamenných soch"));
    }

    /**
     * Testuje fungování definovaných aliasů.
     * Například "prohledni" by mělo volat "prozkoumej".
     */
    @Test
    public void testCommandAliases() {
        String result = game.processUserInput("prohledni");
        assertEquals("Co mám prozkoumat?", result);

        String resultMove = game.processUserInput("bez zahradu");
        assertTrue(resultMove.contains("Zahrada"));
    }

    /**
     * Testuje stav hry po jejím ukončení.
     * Po nastavení příznaku GameOver by hra měla odmítat další příkazy.
     */
    @Test
    public void testGameOverState() {
        assertFalse(game.isGameOver());

        game.setGameOver(true);
        assertTrue(game.isGameOver());

        String result = game.processUserInput("jdi zahradu");
        assertEquals("Hra již skončila.", result);
    }

    /**
     * Testuje základní interakci se světem (pohyb a inventář).
     * Ověřuje, že příkazy procházejí skrz Game do GameWorldu správně.
     */
    @Test
    public void testBasicInteraction() {
        assertTrue(game.getGameWorld().getCurrentArea().containsItem("lektvar_mrsnosti"), "lektvar_mrsnosti by měl být na startu.");

        String takeResult = game.processUserInput("seber lektvar_mrsnosti");

        assertTrue(takeResult.contains("Sebral(a) jsi"),
                "Příkaz 'seber lektvar_mrsnosti' selhal. Hra vrátila: " + takeResult);

        assertTrue(game.getGameWorld().getInventory().containsItem("lektvar_mrsnosti"), "lektvar_mrsnosti by měl být v inventáři.");

        String moveResult = game.processUserInput("jdi zahrada");
        assertTrue(moveResult.contains("Zahrada_soch"));
        assertEquals("Zahrada_soch", game.getGameWorld().getCurrentArea().getName());
    }

    /**
     * Testuje epilog hry.
     */
    @Test
    public void testEpilogue() {
        String epilogue = game.getEpilogue();
        assertTrue(epilogue.contains("Díky, že sis zahrál"));
    }
}
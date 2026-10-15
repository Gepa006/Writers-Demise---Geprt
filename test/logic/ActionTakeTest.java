package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testovací třída pro ověření funkčnosti příkazu 'seber' (ActionTake).
 * Ověřuje reakce na chybějící parametry, sbírání existujících i neexistujících předmětů,
 * nepřenositelné předměty a limity inventáře.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class ActionTakeTest {
    private Game game;
    private Item mec;
    private Item stul;

    /**
     * Příprava herního světa a předmětů před každým testem.
     */
    @BeforeEach
    public void setUp() {
        game = new Game();
        Area mistnost = new Area("mistnost", "popis");

        mec = new Item("mec", "ostrý meč", true, Item.Type.WEAPON);
        stul = new Item("stul", "těžký stůl", false, Item.Type.OTHER);

        mistnost.addItem(mec);
        mistnost.addItem(stul);

        game.getGameWorld().setCurrentArea(mistnost);
    }

    /**
     * Testuje reakci na chybějící parametry.
     */
    @Test
    public void testNoParameters() {
        String result = game.processUserInput("seber");
        assertEquals("Tomu nerozumím, musíš mi říct, co mám sebrat.", result);
    }

    /**
     * Testuje reakci na zadání více parametrů.
     */
    @Test
    public void testTooManyParameters() {
        String result = game.processUserInput("seber a b");
        assertEquals("Tomu nerozumím, neumím sebrat více věcí současně.", result);
    }

    /**
     * Testuje pokus o sebrání předmětu, který v místnosti není.
     */
    @Test
    public void testItemNotHere() {
        String result = game.processUserInput("seber neexistujici");
        assertEquals("Předmět 'neexistujici' tady není.", result);
    }

    /**
     * Testuje pokus o sebrání nepřenositelného předmětu (např. stůl).
     */
    @Test
    public void testUnmoveableItem() {
        String result = game.processUserInput("seber stul");
        assertEquals("Předmět 'stul' fakt neuneseš.", result);
        assertFalse(game.getGameWorld().getInventory().containsItem("stul"));
    }

    /**
     * Testuje úspěšné sebrání předmětu.
     */
    @Test
    public void testTakeSuccess() {
        String result = game.processUserInput("seber mec");

        assertEquals("Sebral(a) jsi předmět 'mec'.", result);
        assertTrue(game.getGameWorld().getInventory().containsItem("mec"));
        assertFalse(game.getGameWorld().getCurrentArea().containsItem("mec"));
    }

    /**
     * Testuje chování při plném inventáři (pro zbraně).
     * Hráč už jednu zbraň má, pokusí se sebrat druhou.
     */
    @Test
    public void testFullInventoryWeapon() {
        game.processUserInput("seber mec");

        game.getGameWorld().getCurrentArea().addItem(new Item("dyka", "dýka", Item.Type.WEAPON));

        String result = game.processUserInput("seber dyka");

        assertEquals("Nemůžeš to sebrat. Máš plné ruce! Nejdřív musíš položit zbraň, co držíš.", result);
        assertFalse(game.getGameWorld().getInventory().containsItem("dyka"));
    }

    /**
     * Testuje chování při plném inventáři (pro lektvary).
     * Hráč má 2 lektvary, pokusí se sebrat třetí.
     */
    @Test
    public void testFullInventoryPotions() {
        game.getGameWorld().getInventory().addItem(new Item("lektvar1", "l1", Item.Type.POTION));
        game.getGameWorld().getInventory().addItem(new Item("lektvar2", "l2", Item.Type.POTION));

        game.getGameWorld().getCurrentArea().addItem(new Item("lektvar3", "l3", Item.Type.POTION));

        String result = game.processUserInput("seber lektvar3");

        assertEquals("Nemůžeš to sebrat. Máš plné kapsy! Vejdou se ti tam jen dva lektvary.", result);
        assertFalse(game.getGameWorld().getInventory().containsItem("lektvar3"));
    }
}
package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testovací třída pro ověření funkčnosti třídy {@link Item}.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class ItemTest {
    private Item mec;
    private Item lektvar;

    /**
     * Příprava předmětů před každým testem.
     */
    @BeforeEach
    public void setUp() {
        mec = new Item("mec", "Ostrý železný meč.", true, Item.Type.WEAPON);
        lektvar = new Item("lektvar", "Léčivý lektvar.", Item.Type.POTION);
    }

    /**
     * Testuje, zda konstruktor správně nastaví všechny atributy.
     */
    @Test
    public void testInitialization() {
        assertEquals("mec", mec.getName());
        assertEquals("Ostrý železný meč.", mec.getDescription());
        assertTrue(mec.isMoveable());
        assertEquals(Item.Type.WEAPON, mec.getType());

        assertEquals("lektvar", lektvar.getName());
        assertTrue(lektvar.isMoveable());
        assertEquals(Item.Type.POTION, lektvar.getType());
    }

    /**
     * Testuje změnu přenositelnosti předmětu.
     */
    @Test
    public void testSetMoveable() {
        assertTrue(mec.isMoveable());
        mec.setMoveable(false);
        assertFalse(mec.isMoveable());
    }

    /**
     * Testuje rovnost dvou předmětů.
     * Předměty jsou shodné, pokud mají stejný název.
     */
    @Test
    public void testEquality() {
        Item mec2 = new Item("mec", "Jiný popis meče", false, Item.Type.OTHER);
        Item stul = new Item("stul", "Stůl", false, Item.Type.OTHER);

        assertEquals(mec, mec2);

        assertNotEquals(mec, stul);
        assertNotEquals(mec, null);
        assertNotEquals(mec, "nějaký string");
    }

    /**
     * Testuje porovnávání předmětů (pro řazení).
     */
    @Test
    public void testCompareTo() {
        Item a = new Item("a_item", "desc", Item.Type.OTHER);
        Item b = new Item("b_item", "desc", Item.Type.OTHER);

        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
        assertEquals(0, a.compareTo(new Item("a_item", "desc", Item.Type.OTHER)));
    }
}
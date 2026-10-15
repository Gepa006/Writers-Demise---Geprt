package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testovací třída pro ověření netriviální logiky inventáře.
 * Testuje kapacitu slotů pro zbraně a lektvary a manipulaci s předměty.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class InventoryTest {
    private Inventory inventory;
    private Item weapon1;
    private Item potion1;
    private Item potion2;
    private Item potion3;

    /**
     * Příprava testovacích dat (inventář a předměty) před každým testem.
     */
    @BeforeEach
    public void setUp() {
        inventory = new Inventory();
        weapon1 = new Item("mec", "Meč", Item.Type.WEAPON);
        potion1 = new Item("lektvar1", "Lektvar 1", Item.Type.POTION);
        potion2 = new Item("lektvar2", "Lektvar 2", Item.Type.POTION);
        potion3 = new Item("lektvar3", "Lektvar 3", Item.Type.POTION);
    }

    /**
     * Ověřuje limity kapacity inventáře.
     * Testuje, že nelze vložit druhou zbraň, pokud je jedna v ruce,
     * a že nelze vložit více než dva lektvary.
     */
    @Test
    public void testInventoryCapacity() {
        assertTrue(inventory.addItem(weapon1));
        assertFalse(inventory.addItem(new Item("dyka", "Dýka", Item.Type.WEAPON)),
                "Nemělo by jít přidat druhou zbraň, když je ruka plná.");

        assertTrue(inventory.addItem(potion1));
        assertTrue(inventory.addItem(potion2));
        assertFalse(inventory.addItem(potion3),
                "Nemělo by jít přidat třetí lektvar, kapsy jsou plné.");
    }

    /**
     * Ověřuje správné odebrání předmětů z inventáře.
     * Testuje odebrání zbraně i lektvaru.
     */
    @Test
    public void testRemoveItem() {
        inventory.addItem(weapon1);
        inventory.addItem(potion1);

        assertEquals(weapon1, inventory.removeItem("mec"));
        assertNull(inventory.getWeapon());

        assertEquals(potion1, inventory.removeItem("lektvar1"));
        assertFalse(inventory.containsItem("lektvar1"));
    }

    /**
     * Ověřuje metodu pro nahlížení na předmět.
     * Testuje, zda metoda vrátí referenci na předmět, ale neodstraní ho z inventáře.
     */
    @Test
    public void testGetItemInspection() {
        inventory.addItem(potion1);

        Item inspected = inventory.getItem("lektvar1");
        assertNotNull(inspected);
        assertEquals("lektvar1", inspected.getName());
        assertTrue(inventory.containsItem("lektvar1"));
    }
}
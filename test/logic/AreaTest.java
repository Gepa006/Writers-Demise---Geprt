package logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testovací třída pro komplexní otestování třídy {@link Area}.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025, 2025-12-13
 */
public class AreaTest {
    @Test
    public void testExits() {
        Area area1 = new Area("hala", "Toto je vstupní hala budovy VŠE na Jižním městě.");
        Area area2 = new Area("bufet", "Toto je bufet, kam si můžeš zajít na svačinu.");

        assertTrue(area1.getExits().isEmpty());
        assertTrue(area2.getExits().isEmpty());

        assertNull(area1.getExit(area1.getName()));
        assertNull(area1.getExit(area2.getName()));
        assertNull(area2.getExit(area1.getName()));

        assertFalse(area1.hasExit(area1.getName()));
        assertFalse(area1.hasExit(area2.getName()));
        assertFalse(area2.hasExit(area1.getName()));

        area1.addExit(area1);
        area1.addExit(area2);
        area2.addExit(area1);

        assertEquals(2, area1.getExits().size());
        assertEquals(1, area2.getExits().size());

        assertEquals(area1, area1.getExit(area1.getName()));
        assertEquals(area1, area2.getExit(area1.getName()));
        assertEquals(area2, area1.getExit(area2.getName()));
        assertNull(area1.getExit("učebna"));
        assertNull(area2.getExit("učebna"));

        assertTrue(area1.hasExit(area1.getName()));
        assertTrue(area1.hasExit(area2.getName()));
        assertTrue(area2.hasExit(area1.getName()));
        assertFalse(area1.hasExit("učebna"));
        assertFalse(area2.hasExit("učebna"));
    }

    @Test
    public void testItems() {
        Area area = new Area("hala", "Toto je vstupní hala budovy VŠE na Jižním městě.");

        // OPRAVA: Doplněn typ předmětu (OTHER pro stůl, POTION pro rum)
        Item item1 = new Item("stul", "Těžký dubový stůl.", false, Item.Type.OTHER);
        Item item2 = new Item("rum", "Láhev vyzrálého rumu.", Item.Type.POTION);

        assertTrue(area.getItems().isEmpty());

        assertNull(area.getItem(item1.getName()));
        assertNull(area.getItem(item2.getName()));

        assertFalse(area.containsItem(item1.getName()));
        assertFalse(area.containsItem(item2.getName()));

        area.addItem(item1);
        area.addItem(item2);

        assertEquals(2, area.getItems().size());

        assertEquals(item1, area.getItem(item1.getName()));
        assertEquals(item2, area.getItem(item2.getName()));
        assertNull(area.getItem("pc"));

        assertTrue(area.containsItem(item1.getName()));
        assertTrue(area.containsItem(item2.getName()));
        assertFalse(area.containsItem("pc"));

        assertEquals(item1, area.removeItem(item1.getName()));
        assertEquals(item2, area.removeItem(item2.getName()));
        assertNull(area.removeItem("pc"));

        assertNull(area.getItem(item1.getName()));
        assertNull(area.getItem(item2.getName()));
        assertNull(area.getItem("pc"));

        assertFalse(area.containsItem(item1.getName()));
        assertFalse(area.containsItem(item2.getName()));
        assertFalse(area.containsItem("pc"));
    }

    @Test
    public void testFullDescription() {
        Area area = new Area("hala", "Toto je vstupní hala Nové budovy VŠE v Praze.");

        Area exit1 = new Area("bufet", "Toto je bufet, v případě nouze zde najdeš pohotovostní zásobu krmiva.");
        Area exit2 = new Area("ucebna", "Toto je učebna, přepadá Tě tu neodbytný pocit zmaru a neštěstí.");

        Item item1 = new Item("stul", "Těžký dubový stůl.", false, Item.Type.OTHER);
        Item item2 = new Item("rum", "Láhev vyzrálého rumu.", Item.Type.POTION);

        area.addExit(exit1);
        area.addExit(exit2);

        area.addItem(item1);
        area.addItem(item2);

        String fullDescription = area.getFullDescription();

        assertNotNull(fullDescription);
        assertTrue(fullDescription.contains(area.getName()));
        assertTrue(fullDescription.contains(area.getDescription()));
        assertTrue(fullDescription.contains(exit1.getName()));
        assertTrue(fullDescription.contains(exit2.getName()));
        assertTrue(fullDescription.contains(item1.getName()));
        assertTrue(fullDescription.contains(item2.getName()));
    }
}
package logic;

import java.util.*;

/**
 * Třída představuje lokaci <i>(místo, místnost, prostor)</i> ve scénáři hry.
 * Každá lokace má název, který ji jednoznačně identifikuje. Může mít sousední
 * lokace, do kterých z ní lze odejít. Odkazy na všechny sousední lokace
 * jsou uložené v kolekci. Lokace také může obsahovat předměty a postavy.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class Area implements Comparable<Area> {
    private String name;
    private String description;
    private Set<Area> exits;
    private Map<String, Item> items;
    private Map<String, String> characters;

    /**
     * Konstruktor třídy, vytvoří lokaci se zadaným názvem a popisem.
     *
     * @param name        název lokace <i>(jednoznačný identifikátor, musí se jednat o text bez mezer)</i>
     * @param description podrobnější popis lokace
     */
    public Area(String name, String description) {
        this.name = name;
        this.description = description;

        this.exits = new TreeSet<>();
        this.items = new TreeMap<>();
        this.characters = new TreeMap<>();
    }

    /**
     * Metoda vrací název lokace, který byl zadán při vytváření instance jako
     * parametr konstruktoru. Jedná se o jednoznačný identifikátor lokace.
     *
     * @return název lokace
     */
    public String getName() {
        return name;
    }

    /**
     * Metoda vrací popis lokace.
     *
     * @return popis lokace
     */
    public String getDescription() {
        return description;
    }

    /**
     * Metoda vrací kompletní informace o lokaci. Výsledek volání obsahuje:
     * <ul>
     * <li>název lokace</li>
     * <li>popis lokace</li>
     * <li>seznam sousedních lokací (východů)</li>
     * <li>seznam předmětů v lokaci</li>
     * <li>seznam postav v lokaci</li>
     * </ul>
     *
     * @return kompletní informace o lokaci
     */
    public String getFullDescription() {
        String exitNames = "Východy:";
        for (Area exit : exits) {
            exitNames += " " + exit.getName();
        }

        String itemNames = "Předměty:";
        if (items.isEmpty()) {
            itemNames += " žádné";
        } else {
            for (String itemName : items.keySet()) {
                itemNames += " " + itemName;
            }
        }

        String characterNames = "Postavy:";
        if (characters.isEmpty()) {
            characterNames += " žádné";
        } else {
            for (String charName : characters.keySet()) {
                characterNames += " " + charName;
            }
        }

        return "Jsi v lokaci '" + name + "'.\n"
                + description + "\n\n"
                + exitNames + "\n"
                + itemNames + "\n"
                + characterNames;
    }

    /**
     * Metoda přidá další východ z této lokace do lokace předané v parametru.
     *
     * @param targetArea lokace, do které bude vytvořen východ z aktuální lokace
     */
    public void addExit(Area targetArea) {
        exits.add(targetArea);
    }

    /**
     * Metoda vrátí kolekci všech sousedních lokací.
     *
     * @return kolekce sousedních lokací
     */
    public Collection<Area> getExits() {
        return new HashSet<>(exits);
    }

    /**
     * Metoda zkontroluje, zda lokace sousedí s lokací s daným názvem.
     *
     * @param exitName název lokace
     * @return {@code true}, pokud lokace sousedí s lokací s daným názvem; jinak {@code false}
     */
    public boolean hasExit(String exitName) {
        for (Area exit : exits) {
            if (exit.getName().equals(exitName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Metoda vyhledá sousední lokaci s daným názvem a vrátí na ní odkaz.
     *
     * @param exitName název lokace
     * @return lokace s daným názvem; {@code null}, pokud taková sousední lokace není
     */
    public Area getExit(String exitName) {
        for (Area exit : exits) {
            if (exit.getName().equals(exitName)) {
                return exit;
            }
        }
        return null;
    }

    /**
     * Metoda přidá předmět do lokace.
     *
     * @param item předmět, který bude do lokace přidán
     */
    public void addItem(Item item) {
        items.put(item.getName(), item);
    }

    /**
     * Metoda vrátí kolekci všech předmětů v lokaci.
     *
     * @return kolekce předmětů v lokaci
     */
    public Collection<Item> getItems() {
        return new HashSet<>(items.values());
    }

    /**
     * Metoda zkontroluje, zda lokace obsahuje předmět s daným názvem.
     *
     * @param itemName název předmětu
     * @return {@code true}, pokud lokace obsahuje předmět s daným názvem; jinak {@code false}
     */
    public boolean containsItem(String itemName) {
        return items.containsKey(itemName);
    }

    /**
     * Metoda vyhledá v lokaci předmět s daným názvem a vrátí na něj odkaz.
     *
     * @param itemName název předmětu
     * @return předmět s daným názvem; {@code null}, pokud v lokaci není
     */
    public Item getItem(String itemName) {
        return items.get(itemName);
    }

    /**
     * Metoda vyhledá v lokaci předmět s daným názvem, odstraní ho z lokace a vrátí na něj odkaz.
     *
     * @param itemName název předmětu
     * @return odstraněný předmět; {@code null}, pokud předmět v lokaci není
     */
    public Item removeItem(String itemName) {
        return items.remove(itemName);
    }

    /**
     * Metoda přidá postavu do lokace.
     *
     * @param name        jméno postavy (jednoznačný identifikátor)
     * @param description krátký popis postavy nebo její úvodní hláška
     */
    public void addCharacter(String name, String description) {
        characters.put(name, description);
    }

    /**
     * Metoda vrátí kolekci jmen všech postav v lokaci.
     *
     * @return kolekce jmen postav
     */
    public Collection<String> getCharacters() {
        return new HashSet<>(characters.keySet());
    }

    /**
     * Metoda zkontroluje, zda se v lokaci nachází postava s daným jménem.
     *
     * @param name jméno postavy
     * @return {@code true}, pokud se postava v lokaci nachází; jinak {@code false}
     */
    public boolean containsCharacter(String name) {
        return characters.containsKey(name);
    }

    /**
     * Metoda odstraní postavu z lokace a vrátí její popis.
     *
     * @param name jméno postavy
     * @return popis odstraněné postavy; {@code null}, pokud postava v lokaci nebyla
     */
    public String removeCharacter(String name) {
        return characters.remove(name);
    }

    /**
     * Metoda porovnává dvě lokace. Lokace jsou shodné, pokud mají stejný název.
     *
     * @param o objekt, který bude porovnán s aktuálním
     * @return {@code true}, pokud mají obě lokace stejný název; jinak {@code false}
     */
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o instanceof Area) {
            Area area = (Area) o;
            return name.equals(area.getName());
        }
        return false;
    }

    /**
     * Metoda vrací hash kód instance na základě názvu lokace.
     *
     * @return číselný identifikátor instance
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * Metoda porovná lokaci s druhou lokací abecedně dle jejich názvů.
     *
     * @param area lokace, jejíž název bude porovnán s názvem této lokace
     * @return výsledek porovnání řetězců
     */
    @Override
    public int compareTo(Area area) {
        return name.compareTo(area.getName());
    }
}
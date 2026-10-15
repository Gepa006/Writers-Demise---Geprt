package logic;

/**
 * Třída představující předmět ve hře.
 * Předmět může být typu zbraň, lektvar nebo ostatní.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class Item implements Comparable<Item> {
    /**
     * Výčet typů předmětů.
     */
    public enum Type {
        WEAPON,
        POTION,
        OTHER
    }

    private String name;
    private String description;
    private boolean moveable;
    private Type type;

    /**
     * Konstruktor třídy.
     *
     * @param name        název předmětu
     * @param description popis předmětu
     * @param moveable    zda je předmět přenositelný
     * @param type        typ předmětu
     */
    public Item(String name, String description, boolean moveable, Type type) {
        this.name = name;
        this.description = description;
        this.moveable = moveable;
        this.type = type;
    }

    /**
     * Zjednodušený konstruktor pro přenositelné předměty.
     *
     * @param name        název předmětu
     * @param description popis předmětu
     * @param type        typ předmětu
     */
    public Item(String name, String description, Type type) {
        this(name, description, true, type);
    }

    /**
     * Vrátí typ předmětu.
     *
     * @return typ předmětu
     */
    public Type getType() {
        return type;
    }

    /**
     * Vrátí název předmětu.
     *
     * @return název předmětu
     */
    public String getName() {
        return name;
    }

    /**
     * Vrátí popis předmětu.
     *
     * @return popis předmětu
     */
    public String getDescription() {
        return description;
    }

    /**
     * Zjistí, zda je předmět přenositelný.
     *
     * @return true, pokud je přenositelný
     */
    public boolean isMoveable() {
        return moveable;
    }

    /**
     * Nastaví přenositelnost předmětu.
     *
     * @param moveable nová hodnota přenositelnosti
     */
    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }

    /**
     * Porovná tento předmět s jiným objektem.
     * Dva předměty jsou shodné, pokud mají stejný název.
     *
     * @param o objekt k porovnání
     * @return true, pokud jsou objekty shodné
     */
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o instanceof Item) {
            Item item = (Item) o;
            return name.equals(item.getName());
        }
        return false;
    }

    /**
     * Vrátí hash kód předmětu na základě jeho názvu.
     *
     * @return hash kód
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * Porovná tento předmět s jiným předmětem pro účely řazení.
     *
     * @param item předmět k porovnání
     * @return výsledek porovnání řetězců názvů
     */
    @Override
    public int compareTo(Item item) {
        return name.compareTo(item.getName());
    }
}
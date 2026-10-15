package logic;

import java.util.*;

/**
 * Třída představující inventář hráče.
 * Inventář má omezenou kapacitu: jeden slot pro zbraň v ruce
 * a dva sloty v kapsách pro lektvary.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class Inventory {

    private Item weaponInHand;
    private List<Item> potionsInPocket;

    private static final int MAX_POTIONS = 2;

    /**
     * Konstruktor třídy. Vytvoří prázdný inventář.
     */
    public Inventory() {
        this.potionsInPocket = new ArrayList<>();
        this.weaponInHand = null;
    }

    /**
     * Pokusí se přidat předmět do inventáře.
     * Zbraně jdou do ruky (pokud je prázdná), lektvary do kapes (pokud je místo).
     *
     * @param item předmět k přidání
     * @return true, pokud se předmět podařilo přidat; jinak false
     */
    public boolean addItem(Item item) {
        if (item.getType() == Item.Type.WEAPON) {
            if (weaponInHand == null) {
                weaponInHand = item;
                return true;
            } else {
                return false;
            }
        } else if (item.getType() == Item.Type.POTION) {
            if (potionsInPocket.size() < MAX_POTIONS) {
                potionsInPocket.add(item);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Odebere předmět z inventáře podle názvu.
     *
     * @param itemName název předmětu
     * @return odebraný předmět, nebo null, pokud předmět v inventáři není
     */
    public Item removeItem(String itemName) {
        if (weaponInHand != null && weaponInHand.getName().equals(itemName)) {
            Item removed = weaponInHand;
            weaponInHand = null;
            return removed;
        }

        for (Item potion : potionsInPocket) {
            if (potion.getName().equals(itemName)) {
                potionsInPocket.remove(potion);
                return potion;
            }
        }

        return null;
    }

    /**
     * Vrátí odkaz na předmět v inventáři (pro průzkum), ale neodstraní ho.
     *
     * @param itemName název předmětu
     * @return instance předmětu nebo null
     */
    public Item getItem(String itemName) {
        if (weaponInHand != null && weaponInHand.getName().equals(itemName)) {
            return weaponInHand;
        }

        for (Item potion : potionsInPocket) {
            if (potion.getName().equals(itemName)) {
                return potion;
            }
        }

        return null;
    }

    /**
     * Zjistí, zda se předmět nachází v inventáři.
     *
     * @param itemName název předmětu
     * @return true, pokud je předmět v inventáři; jinak false
     */
    public boolean containsItem(String itemName) {
        if (weaponInHand != null && weaponInHand.getName().equals(itemName)) return true;
        for (Item p : potionsInPocket) {
            if (p.getName().equals(itemName)) return true;
        }
        return false;
    }

    /**
     * Vrátí aktuálně drženou zbraň.
     *
     * @return objekt zbraně nebo null
     */
    public Item getWeapon() {
        return weaponInHand;
    }

    /**
     * Vrátí textový popis obsahu inventáře pro výpis hráči.
     *
     * @return naformátovaný řetězec s obsahem inventáře
     */
    public String getInventoryContent() {
        String weaponStatus = (weaponInHand != null) ? weaponInHand.getName() : "nic";

        String potionStatus = "";
        if (potionsInPocket.isEmpty()) {
            potionStatus = "nic";
        } else {
            for (Item p : potionsInPocket) {
                potionStatus += p.getName() + ", ";
            }
            if (potionStatus.length() > 2) {
                potionStatus = potionStatus.substring(0, potionStatus.length() - 2);
            }
        }

        return "V rukou držíš: " + weaponStatus + "\nV kapsách máš: " + potionStatus;
    }
}
package logic;

/**
 * Třída implementující příkaz pro zobrazení obsahu inventáře.
 * Ukazuje, co má Alice v rukou a co v kapsách.
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class ActionInventory implements IAction {
    private Game game;

    /**
     * Konstruktor třídy.
     *
     * @param game hra, ve které se bude příkaz používat
     */
    public ActionInventory(Game game) {
        this.game = game;
    }

    /**
     * Metoda vrací název herního příkazu.
     *
     * @return název herního příkazu
     */
    @Override
    public String getName() {
        return "inventar";
    }

    /**
     * Metoda zajistí vypsání obsahu inventáře.
     * Ignoruje případné parametry, protože se vždy vypisuje celý inventář.
     *
     * @param parameters parametry příkazu (aktuálně se nevyužívají)
     * @return textový popis obsahu inventáře
     */
    @Override
    public String execute(String[] parameters) {
        Inventory inventory = game.getGameWorld().getInventory();

        // Zavoláme metodu, kterou jsme dříve připravili v Inventory.java
        return inventory.getInventoryContent();
    }
}
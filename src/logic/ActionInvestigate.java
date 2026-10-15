package logic;

/**
 * Třída implementující příkaz pro detailní průzkum předmětů nebo postav.
 * Hledá v lokaci, v inventáři i mezi postavami.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class ActionInvestigate implements IAction {
    private Game game;

    /**
     * Konstruktor třídy.
     *
     * @param game hra, ve které se bude příkaz používat
     */
    public ActionInvestigate(Game game) {
        this.game = game;
    }

    /**
     * Vrátí název příkazu (prozkoumej).
     *
     * @return název příkazu
     */
    @Override
    public String getName() {
        return "prozkoumej";
    }

    /**
     * Zobrazí detailní popis specifikovaného objektu (předmětu nebo postavy).
     * Prohledává aktuální lokaci i inventář hráče.
     *
     * @param parameters parametry příkazu (název objektu)
     * @return popis objektu
     */
    @Override
    public String execute(String[] parameters) {
        if (parameters.length < 1) return "Co mám prozkoumat?";

        String rawInput = String.join(" ", parameters);
        String targetName = game.getGameWorld().translateName(rawInput);

        Area currentArea = game.getGameWorld().getCurrentArea();
        Inventory inventory = game.getGameWorld().getInventory();

        if (currentArea.containsItem(targetName)) {
            return "Předmět '" + targetName + "': " + currentArea.getItem(targetName).getDescription();
        }

        if (currentArea.containsCharacter(targetName)) {
            return "Vidíš postavu: " + targetName + ". Zkus si s ní promluvit.";
        }

        if (inventory.containsItem(targetName)) {
            Item item = inventory.getItem(targetName);
            if (item != null) {
                return "Předmět v batohu '" + targetName + "': " + item.getDescription();
            }
        }

        return "Nic takového tu nevidím.";
    }
}
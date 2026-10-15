package logic;

/**
 * Třída implementující příkaz pro pokládání předmětů.
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class ActionDrop implements IAction {
    private Game game;

    /**
     * Konstruktor třídy.
     *
     * @param game hra, ve které se bude příkaz používat
     */
    public ActionDrop(Game game) {
        this.game = game;
    }

    /**
     * Metoda vrací název příkazu tj. slovo poloz.
     *
     * @return název příkazu
     */
    @Override
    public String getName() {
        return "poloz";
    }

    /**
     * Metoda se pokusí položit předmět z inventáře do aktuální lokace.
     *
     * @param parameters parametry příkazu (očekává se název předmětu)
     * @return informace pro hráče
     */
    @Override
    public String execute(String[] parameters) {
        if (parameters.length < 1) {
            return "Tomu nerozumím, musíš mi říct, co mám položit.";
        }

        if (parameters.length > 1) {
            return "Tomu nerozumím, neumím položit více věcí současně.";
        }

        String itemName = parameters[0];
        Inventory inventory = game.getGameWorld().getInventory();
        Area currentArea = game.getGameWorld().getCurrentArea();

        Item itemToDrop = inventory.removeItem(itemName);

        if (itemToDrop == null) {
            return "Předmět '" + itemName + "' u sebe nemáš.";
        }

        currentArea.addItem(itemToDrop);

        return "Položil(a) jsi předmět '" + itemName + "' na zem.";
    }
}
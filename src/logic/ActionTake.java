package logic;

/**
 * Třída implementující příkaz pro sbírání předmětů.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025, 2025-12-13
 */
public class ActionTake implements IAction {
    private Game game;

    /**
     * Konstruktor třídy.
     *
     * @param game hra, ve které se bude příkaz používat
     */
    public ActionTake(Game game) {
        this.game = game;
    }

    /**
     * Metoda vrací název příkazu tj.&nbsp;slovo <b>seber</b>.
     *
     * @return název příkazu
     */
    @Override
    public String getName() {
        return "seber";
    }

    /**
     * Metoda se pokusí sebrat předmět z aktuální lokace a uložit ho do hráčova
     * inventáře. Nejprve zkontroluje počet parametrů. Pokud nebyl zadán žádný
     * parametr <i>(tj. neznáme požadovaný předmět)</i>, nebo bylo zadáno dva a
     * více parametrů <i>(tj. hráč chce sebrat více předmětů současně)</i>, vrátí
     * chybové hlášení. Pokud byl zadán právě jeden parametr, zkontroluje, zda
     * v aktuální lokaci je předmět s daným názvem, zda je přenositelný a zda
     * na něj hráč má v inventáři místo <i>(tj. zda ho lze sebrat)</i>. Pokud ne,
     * vrátí chybové hlášení. Pokud všechny kontroly proběhnou v pořádku, provede
     * přesun předmětu z lokace do inventáře a vrátí informaci o sebrání předmětu.
     *
     * @param parameters parametry příkazu <i>(očekává se pole s jedním prvkem)</i>
     * @return informace pro hráče, které hra vypíše na konzoli
     */
    @Override
    public String execute(String[] parameters) {
        if (parameters.length < 1) {
            return "Tomu nerozumím, musíš mi říct, co mám sebrat.";
        }

        if (parameters.length > 1) {
            return "Tomu nerozumím, neumím sebrat více věcí současně.";
        }
        String itemName = parameters[0];
        Area currentArea = game.getGameWorld().getCurrentArea();
        Inventory inventory = game.getGameWorld().getInventory();

        if (!currentArea.containsItem(itemName)) {
            return "Předmět '" + itemName + "' tady není.";
        }

        Item item = currentArea.getItem(itemName);

        if (!item.isMoveable()) {
            return "Předmět '" + itemName + "' fakt neuneseš.";
        }
        if (inventory.addItem(item)) {
            currentArea.removeItem(itemName);
            return "Sebral(a) jsi předmět '" + itemName + "'.";
        } else {
            if (item.getType() == Item.Type.WEAPON) {
                return "Nemůžeš to sebrat. Máš plné ruce! Nejdřív musíš položit zbraň, co držíš.";
            } else if (item.getType() == Item.Type.POTION) {
                return "Nemůžeš to sebrat. Máš plné kapsy! Vejdou se ti tam jen dva lektvary.";
            } else {
                return "Předmět '" + itemName + "' nejde sebrat.";
            }
        }
    }
}
package logic;

/**
 * Třída implementující příkaz pro použití předmětů (lektvarů).
 * Aktivuje efekty (léčení, staty) na omezený počet tahů.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class ActionUse implements IAction {
    private Game game;

    /**
     * Konstruktor třídy.
     *
     * @param game hra, ve které se bude příkaz používat
     */
    public ActionUse(Game game) {
        this.game = game;
    }

    /**
     * Vrátí název příkazu (pouzij).
     *
     * @return název příkazu
     */
    @Override
    public String getName() {
        return "pouzij";
    }

    /**
     * Použije specifikovaný předmět z inventáře.
     * Aplikuje efekt (léčení, buffy) a odstraní předmět.
     *
     * @param parameters parametry příkazu (název předmětu)
     * @return výsledek použití
     */
    @Override
    public String execute(String[] parameters) {
        if (parameters.length < 1) {
            return "Co mám použít?";
        }

        String rawInput = String.join(" ", parameters);
        String itemName = game.getGameWorld().translateName(rawInput);

        GameWorld world = game.getGameWorld();
        Inventory inventory = world.getInventory();

        if (!inventory.containsItem(itemName)) {
            return "Předmět '" + rawInput + "' u sebe nemáš.";
        }

        Item item = inventory.removeItem(itemName);

        if (item.getType() == Item.Type.POTION) {
            String effectMsg = "";
            String name = item.getName();

            if (itemName.equals("lektvar_fenixovy_krve")) {
                world.setHealth(world.getMaxHealth());
                return "Vypil(a) jsi Lektvar fénixovy krve. Jsi plně vyléčen! (HP: " + world.getHealth() + ")";
            } else if (itemName.equals("lektvar_mrsnosti")) {
                world.activateAgility();
                return "Vypil(a) jsi Lektvar mršnosti. Cítíš se rychlejší! (Šance na úhyb 50% po dobu 10 tahů)";
            } else if (itemName.equals("lektvar_abstrakce")) {
                world.activateResistance();
                return "Vypil(a) jsi Lektvar abstrakce. Tvé tělo zprůhlednělo. (Dostáváš poloviční poškození po dobu 10 tahů)";
            } else if (itemName.equals("lektvar_rychlosti")) {
                world.activateSpeed();
                return "Vypil(a) jsi Lektvar rychlosti! Máš nyní 50% šanci, že v souboji zahraješ 2x po sobě.";
            }

            inventory.removeItem(itemName);
            return "Vypil(a) jsi " + rawInput + ". " + effectMsg;
        } else {
            return "Tohle se nedá použít.";
        }
    }
}
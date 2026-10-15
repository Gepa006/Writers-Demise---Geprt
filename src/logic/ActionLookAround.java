package logic;

/**
 * Třída implementující příkaz pro průzkum aktuální lokace.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025, 2025-12-13
 */
public class ActionLookAround implements IAction {
    private Game game;

    /**
     * Konstruktor třídy.
     *
     * @param game hra, ve které se bude příkaz používat
     */
    public ActionLookAround(Game game) {
        this.game = game;
    }

    /**
     * Metoda vrací název příkazu tj.&nbsp;slovo <b>rozhledni_se</b>.
     *
     * @return název příkazu
     */
    @Override
    public String getName() {
        return "rozhledni_se";
    }

    /**
     * Metoda vrátí detailní popis aktuální lokace.
     *
     * @param parameters parametry příkazu <i>(očekává se prázdné pole)</i>
     * @return informace pro hráče, které hra vypíše na konzoli
     */
    @Override
    public String execute(String[] parameters) {
        if (parameters.length > 0) {
            return "Tomu nerozumím, umím se rozhlédnout jen kolem sebe, ne na něco konkrétního.";
        }

        Area currentArea = game.getGameWorld().getCurrentArea();

        return currentArea.getFullDescription();
    }
}

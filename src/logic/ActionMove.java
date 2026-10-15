package logic;

/**
 * Třída implementující příkaz pro pohyb mezi herními lokacemi.
 * Zajišťuje zpracování předložek, aliasů a kontrolu, zda hráč může odejít z lokace (Combat Lock).
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class ActionMove implements IAction {
    private Game game;

    /**
     * Konstruktor třídy.
     *
     * @param game hra, ve které se bude příkaz používat
     */
    public ActionMove(Game game) {
        this.game = game;
    }

    /**
     * Vrátí název příkazu (jdi).
     *
     * @return název příkazu
     */
    @Override
    public String getName() {
        return "jdi";
    }

    /**
     * Pokusí se přesunout hráče do jiné lokace.
     * Kontroluje přítomnost nepřátel (Combat Lock) a řeší víceslovné názvy lokací.
     *
     * @param parameters parametry příkazu (cíl cesty)
     * @return výsledek operace (popis nové lokace nebo chybová hláška)
     */
    @Override
    public String execute(String[] parameters) {
        if (parameters.length < 1) {
            return "Tomu nerozumím, musíš mi říct, kam mám jít.";
        }

        String rawInput;
        if ((parameters[0].equalsIgnoreCase("do") || parameters[0].equalsIgnoreCase("na") || parameters[0].equalsIgnoreCase("k"))
                && parameters.length > 1) {
            rawInput = parameters[1];
        } else {
            rawInput = String.join(" ", parameters);
        }

        GameWorld world = game.getGameWorld();
        Area currentArea = world.getCurrentArea();

        for (String charName : currentArea.getCharacters()) {
            if (world.getEnemyHealth(charName) > 0) {
                return "Nemůžeš odejít! V cestě ti stojí " + charName + ".\n"
                        + "Musíš ho porazit (zautoc) nebo zkusit utéct (utec).";
            }
        }

        String targetName = world.translateName(rawInput);
        Area targetArea = null;

        for (Area exit : currentArea.getExits()) {
            if (exit.getName().equalsIgnoreCase(targetName)) {
                targetArea = exit;
                break;
            }
            if (world.translateName(exit.getName()).equalsIgnoreCase(targetName)) {
                targetArea = exit;
                break;
            }
        }

        if (targetArea == null) {
            return "Do lokace '" + rawInput + "' se odsud jít nedá (nebo neexistuje).";
        }

        world.setCurrentArea(targetArea);
        String output = targetArea.getFullDescription();

        if (targetArea.getName().equals("Vstupní_hala") && targetArea.containsCharacter("vratny")) {
            output += "\n\n" +
                    "****************************************************\n" +
                    "Vrátný ti zastoupil cestu!\n" +
                    "Vrátný: 'Stůj! Dál tě nepustím, dokud neuhodneš mou hádanku.'\n" +
                    "'Kolikátá expedice v Clair Obscur porazila Malířku?'\n" +
                    "(Odpověz příkazem: 'odpovez [číslo]')\n" +
                    "****************************************************";
        }

        return output;
    }
}
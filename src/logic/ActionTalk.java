package logic;

/**
 * Třída implementující příkaz pro rozhovor s postavami.
 * Zajišťuje dialogy a získávání předmětů od NPC.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class ActionTalk implements IAction {
    private Game game;

    /**
     * Konstruktor třídy.
     *
     * @param game hra, ve které se bude příkaz používat
     */
    public ActionTalk(Game game) {
        this.game = game;
    }

    /**
     * Vrátí název příkazu (promluv).
     *
     * @return název příkazu
     */
    @Override
    public String getName() {
        return "promluv";
    }

    /**
     * Zahájí rozhovor se specifikovanou postavou v aktuální lokaci.
     *
     * @param parameters parametry příkazu (jméno postavy)
     * @return text rozhovoru
     */
    @Override
    public String execute(String[] parameters) {
        if (parameters.length < 1) {
            return "S kým mám mluvit?";
        }

        String rawInput;
        if ((parameters[0].equalsIgnoreCase("s") || parameters[0].equalsIgnoreCase("se")) && parameters.length > 1) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < parameters.length; i++) sb.append(parameters[i]).append(" ");
            rawInput = sb.toString().trim();
        } else {
            rawInput = String.join(" ", parameters);
        }

        String npcName = game.getGameWorld().translateName(rawInput);
        Area currentArea = game.getGameWorld().getCurrentArea();
        Inventory inventory = game.getGameWorld().getInventory();

        if (!currentArea.containsCharacter(npcName)) {
            return "Postava '" + rawInput + "' tady není.";
        }

        if (npcName.equals("duch_vency")) {
            Item dyka = new Item("dyka", "Ostrá dýka tvého bratra.", Item.Type.WEAPON);
            if (inventory.addItem(dyka)) {
                currentArea.removeCharacter("duch_vency");
                return "Duch Vency: 'Alice, vem si tuhle dýku a pomsti náš rod.' (Získal jsi dýku)";
            }
            return "Duch Vency: 'Máš plné ruce!'";
        }

        if (npcName.equals("kucharka")) {
            Item lektvar = new Item("lektvar_abstrakce", "Lektvar abstrakce.", Item.Type.POTION);
            if (inventory.addItem(lektvar)) {
                return "Kuchařka: 'Pššt! Tady máš Lektvar abstrakce.' (Získal jsi lektvar)";
            }
            return "Kuchařka: 'Máš plné kapsy.'";
        }

        if (npcName.equals("vratny")) {
            return "Vrátný: 'Stůj! Dál tě nepustím, dokud neuhodneš mou hádanku.'\n"
                    + "'Kolikátá expedice v Clairs Obscur porazila Malířku?'\n"
                    + "(Odpověz příkazem: 'odpovez [číslo]')";
        }

        if (npcName.equals("komornik")) return "Komorník: 'Zmiz, než tě něčím přetáhnu!'";
        if (npcName.equals("spisovatel")) {
            return "Spisovatel: 'Ale, ale, Alice. Konečná příležitost, vyhladit celý váš rod!'\n"
                    + "Myslel jsem, že už se tu neobjevíš, potom co jsem ti zabil celou rodinu.\n"
                    + "Jsi ale stejně naivní jako oni. Tak se předvěd.";
        }

        return "Postava s tebou nechce mluvit.";
    }
}
package logic;

/**
 * Třída implementující příkaz pro zobrazení nápovědy ke hře.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025, 2025-12-13
 */
public class ActionHelp implements IAction {
    /**
     * Metoda vrací název příkazu tj.&nbsp;slovo <b>napoveda</b>.
     *
     * @return název příkazu
     */
    @Override
    public String getName() {
        return "napoveda";
    }

    /**
     * Metoda vrací obecnou nápovědu ke hře. Nyní pouze vypisuje vcelku
     * primitivní zprávu o herním příběhu.
     *
     * @param parameters parametry příkazu <i>(aktuálně se ignorují)</i>
     * @return nápověda, kterou hra vypíše na konzoli
     */
    @Override
    public String execute(String[] parameters) {
        return "Tvým úkolem je najít Magický štětec a porazit Spisovatele ve studovně.\n"
                + "Cestou sbírej lektvary a zbraně, budeš je potřebovat.\n\n"
                + "Dostupné příkazy:\n"
                + "  jdi [lokace]      - přesun do jiné místnosti (např. 'jdi do kuchyne')\n"
                + "  rozhledni_se      - vypíše popis aktuální lokace\n"
                + "  seber [předmět]   - sebere věc (max 1 zbraň + 2 lektvary)\n"
                + "  poloz [předmět]   - zahodí věc z inventáře\n"
                + "  inventar          - ukáže obsah batohu a stav tvého zdraví\n"
                + "  prozkoumej [věc]  - zobrazí detailní popis předmětu nebo postavy\n"
                + "  promluv [postava] - získáš informace nebo předměty\n"
                + "  pouzij [lektvar]  - vypiješ lektvar (léčení, mršnost, rychlost...)\n"
                + "  zautoc [nepřítel] - zaútočíš zbraní v ruce\n"
                + "  bran se           - snížíš poškození v tomto kole\n"
                + "  utec              - pokusíš se utéct z boje do vedlejší místnosti\n"
                + "  konec             - ukončí hru";
    }
}

package logic;

import java.util.HashMap;
import java.util.Map;

/**
 * Třída představující mapu lokací herního světa. V datovém atributu
 * {@link #currentArea} uchovává odkaz na aktuální lokaci, ve které
 * se hráč právě nachází. Z aktuální lokace je možné se prostřednictvím
 * jejích sousedů dostat ke všem přístupným lokacím ve hře.
 * <p>
 * Třída dále uchovává stav bojových statistik hráče (zdraví, efekty lektvarů),
 * stav úkolů (zda je boss mrtev) a mapuje aliasy pro zjednodušení ovládání.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class GameWorld {
    private Area currentArea;
    private Inventory inventory;
    private boolean spisovatelJeMrtev;

    private int health;
    private final int MAX_HEALTH = 100;

    private int agilityDuration;
    private int resistanceDuration;
    private int speedDuration;

    private final int POTION_DURATION = 10;

    private Map<String, Integer> enemyHealths;
    private Map<String, String> aliases;

    /**
     * Konstruktor třídy. Vytvoří jednotlivé lokace, propojí je pomocí východů,
     * naplní je předměty a postavami. Dále inicializuje stav hráče a mapu aliasů.
     */
    public GameWorld() {
        this.inventory = new Inventory();
        this.spisovatelJeMrtev = false;
        this.health = 100;

        this.agilityDuration = 0;
        this.resistanceDuration = 0;
        this.speedDuration = 0;

        this.enemyHealths = new HashMap<>();
        this.aliases = new HashMap<>();

        initializeWorld();
        initializeAliases();
    }

    /**
     * Pomocná metoda pro inicializaci lokací, předmětů a postav.
     */
    private void initializeWorld() {
        Area predmesti = new Area("Mlhové_předměstí", "Probouzíš se na mlžném předměstí. Je ti zima a nic si nepamatuješ.");
        Area zahrada = new Area("Zahrada_soch", "Zahrada plná kamenných soch. Cesty vedou ke kůlně a k bráně.");
        Area kulna = new Area("Stará_kůlna", "Rozpadlá kůlna na nářadí.");
        Area brana = new Area("Hlavní_brána", "Obrovská brána. Stojí tu Komorník.");
        Area hala = new Area("Vstupní_hala", "Velkolepá vstupní hala vily. Cestu dál blokuje Vrátný.");
        Area jidelna = new Area("Jídelna", "Dlouhý stůl. Je tu Kuchařka.");
        Area spiz = new Area("Skrytá_spíž", "Temná komora se zásobami.");
        Area galerie = new Area("Zrcadlová_galerie", "Místnost plná zrcadel. Hlídá ji Elitní strážce.");
        Area schodiste = new Area("Točité_schodiště", "Schody vedou nahoru.");
        Area studovna = new Area("Spisovatelova_studovna", "Temná pracovna Spisovatele. Cíl tvé cesty.");

        predmesti.addExit(zahrada);
        zahrada.addExit(predmesti);
        zahrada.addExit(kulna);
        zahrada.addExit(brana);
        kulna.addExit(zahrada);
        brana.addExit(zahrada);
        brana.addExit(hala);
        hala.addExit(brana);
        hala.addExit(jidelna);
        hala.addExit(schodiste);
        jidelna.addExit(hala);
        jidelna.addExit(spiz);
        jidelna.addExit(galerie);
        spiz.addExit(jidelna);
        galerie.addExit(jidelna);
        schodiste.addExit(hala);
        schodiste.addExit(studovna);
        studovna.addExit(schodiste);

        currentArea = predmesti;

        predmesti.addItem(new Item("lektvar_mrsnosti", "Lektvar mršnosti (Zvyšuje rychlost a dává 50% šanci na úhyb).", Item.Type.POTION));
        kulna.addItem(new Item("mec", "Těžký železný meč.", Item.Type.WEAPON));
        kulna.addItem(new Item("lektvar_fenixovy_krve", "Vzácný lektvar Fénixovy krve (Schovaný pod senem).", Item.Type.POTION));
        zahrada.addItem(new Item("lektvar_fenixovy_krve", "Vzácný lektvar Fénixovy krve (Zapadlý v trávě).", Item.Type.POTION));
        spiz.addItem(new Item("lektvar_fenixovy_krve", "Vzácný lektvar Fénixovy krve (V polici).", Item.Type.POTION));


        predmesti.addCharacter("duch_vency", "Průsvitná postava tvého bratra.");
        jidelna.addCharacter("kucharka", "Žena v zástěře míchající podivný inkoust.");

        brana.addCharacter("komornik", "Stojí tu hrozivý Komorník a blokuje cestu.");
        enemyHealths.put("komornik", 90);

        hala.addCharacter("vratny", "Podivný Vrátný, který si mumlá něco o číslech.");
        enemyHealths.put("vratny", 120);

        schodiste.addCharacter("strazny", "Strážný blokuje cestu do patra.");
        enemyHealths.put("strazny", 100);

        galerie.addCharacter("elitni_strazce", "Strážce v lesklé zbroji hlídá štětec.");
        enemyHealths.put("elitni_strazce", 150);

        studovna.addCharacter("spisovatel", "Samotný Spisovatel. Původce všeho zla.");
        enemyHealths.put("spisovatel", 200);
    }

    /**
     * Pomocná metoda pro inicializaci aliasů (synonym) pro příkazy.
     */
    private void initializeAliases() {
        addAlias("duch_vency", "ducha", "vencou");
        addAlias("kucharka", "kucharku", "kucharkou");
        addAlias("komornik", "komornika", "komornikem");
        addAlias("spisovatel", "spisovatele", "spisovatelem");
        addAlias("elitni_strazce", "strazce", "elitniho strazce");
        addAlias("strazny", "strazneho", "strazce");
        addAlias("vratny", "vratneho", "Vrátný");

        addAlias("lektvar_mrsnosti", "mrsnost");
        addAlias("lektvar_abstrakce", "abstrakce");
        addAlias("lektvar_rychlosti", "rychlost");
        addAlias("lektvar_fenixovy_krve", "krev");

        addAlias("dyka", "dyku", "dykou");
        addAlias("mec", "mece", "mecem");
        addAlias("magicky_stetec", "stetec", "stetcem");

        addAlias("Mlhové_předměstí", "predmesti", "venek");
        addAlias("Zahrada_soch", "zahradu", "zahrady", "zahrada");
        addAlias("Stará_kůlna", "kulnu", "kulny", "kulna");
        addAlias("Hlavní_brána", "branu", "brany", "brana");
        addAlias("Vstupní_hala", "halu", "haly", "hala");
        addAlias("Jídelna", "jidelnu", "jidelny");
        addAlias("Skrytá_spíž", "spiz", "spize");
        addAlias("Zrcadlová_galerie", "galerii", "galerie");
        addAlias("Točité_schodiště", "schodiste", "schody");
        addAlias("Spisovatelova_studovna", "studovnu", "studovny", "studovna");
    }

    /**
     * Přidá sadu přezdívek pro jeden cílový klíč do mapy aliasů.
     *
     * @param targetKey Skutečný název (identifikátor)
     * @param nicknames Seznam aliasů (synonym)
     */
    private void addAlias(String targetKey, String... nicknames) {
        for (String nick : nicknames) {
            aliases.put(nick.toLowerCase(), targetKey);
        }
    }

    /**
     * Přeloží uživatelský vstup na oficiální název entity.
     * Pokud alias neexistuje, vrátí původní vstup (malými písmeny).
     *
     * @param name Text zadaný hráčem
     * @return Oficiální ID entity nebo původní text
     */
    public String translateName(String name) {
        if (name == null) return "";
        String lowerName = name.toLowerCase();
        if (aliases.containsKey(lowerName)) {
            return aliases.get(lowerName);
        }
        return lowerName;
    }

    /**
     * Aktualizuje časovače aktivních efektů lektvarů.
     * Volá se v každém kole hry.
     *
     * @return Textová informace o efektech, které právě vypršely.
     */
    public String updateCooldowns() {
        StringBuilder sb = new StringBuilder();
        if (agilityDuration > 0) {
            agilityDuration--;
            if (agilityDuration == 0) sb.append(">>> Účinek Lektvaru mršnosti vypršel! <<<\n");
        }
        if (resistanceDuration > 0) {
            resistanceDuration--;
            if (resistanceDuration == 0) sb.append(">>> Účinek Lektvaru abstrakce vypršel! <<<\n");
        }
        if (speedDuration > 0) {
            speedDuration--;
            if (speedDuration == 0) sb.append(">>> Účinek Lektvaru rychlosti vypršel! <<<\n");
        }
        return sb.toString();
    }

    /**
     * Vrátí odkaz na aktuální lokaci, ve které se hráč právě nachází.
     *
     * @return aktuální lokace
     */
    public Area getCurrentArea() {
        return currentArea;
    }

    /**
     * Nastaví aktuální lokaci.
     *
     * @param currentArea lokace, která bude nastavena jako aktuální
     */
    public void setCurrentArea(Area currentArea) {
        this.currentArea = currentArea;
    }

    /**
     * Vrátí inventář hráče.
     *
     * @return inventář
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Nastaví stav úkolu "Spisovatel je mrtev".
     *
     * @param stav true pokud je boss poražen, jinak false
     */
    public void setSpisovatelJeMrtev(boolean stav) {
        this.spisovatelJeMrtev = stav;
    }

    /**
     * Vrátí aktuální počet životů hráče.
     *
     * @return počet životů
     */
    public int getHealth() {
        return health;
    }

    /**
     * Nastaví počet životů hráče. Hodnota je omezena maximálním zdravím.
     *
     * @param health nová hodnota zdraví
     */
    public void setHealth(int health) {
        this.health = (health > MAX_HEALTH) ? MAX_HEALTH : health;
    }

    /**
     * Vrátí maximální možný počet životů.
     *
     * @return maximální zdraví
     */
    public int getMaxHealth() {
        return MAX_HEALTH;
    }

    /**
     * Zjistí, zda je aktivní efekt lektvaru mršnosti.
     *
     * @return true, pokud efekt trvá
     */
    public boolean isAgilityActive() {
        return agilityDuration > 0;
    }

    /**
     * Zjistí, zda je aktivní efekt lektvaru odolnosti.
     *
     * @return true, pokud efekt trvá
     */
    public boolean isResistanceActive() {
        return resistanceDuration > 0;
    }

    /**
     * Zjistí, zda je aktivní efekt lektvaru rychlosti.
     *
     * @return true, pokud efekt trvá
     */
    public boolean isSpeedActive() {
        return speedDuration > 0;
    }

    /**
     * Aktivuje efekt mršnosti na stanovenou dobu.
     */
    public void activateAgility() {
        this.agilityDuration = POTION_DURATION;
    }

    /**
     * Aktivuje efekt odolnosti na stanovenou dobu.
     */
    public void activateResistance() {
        this.resistanceDuration = POTION_DURATION;
    }

    /**
     * Aktivuje efekt rychlosti na stanovenou dobu.
     */
    public void activateSpeed() {
        this.speedDuration = POTION_DURATION;
    }

    /**
     * Zjistí aktuální životy nepřítele podle jeho jména (nebo aliasu).
     *
     * @param enemyName jméno nepřítele
     * @return počet životů (0 pokud neexistuje nebo je mrtvý)
     */
    public int getEnemyHealth(String enemyName) {
        String key = translateName(enemyName);
        return enemyHealths.getOrDefault(key, 0);
    }

    /**
     * Způsobí zranění specifikovanému nepříteli.
     *
     * @param enemyName jméno nepřítele
     * @param damage    poškození, které má být uděleno
     */
    public void damageEnemy(String enemyName, int damage) {
        String key = translateName(enemyName);
        if (enemyHealths.containsKey(key)) {
            enemyHealths.put(key, enemyHealths.get(key) - damage);
        }
    }

    /**
     * Vrátí aktuální stav hry.
     *
     * @return stav hry (BĚŽÍ, VÝHRA, PROHRA)
     */
    public GameState getGameState() {
        if (spisovatelJeMrtev) {
            return GameState.WON;
        }
        if (health <= 0) {
            return GameState.LOST;
        }
        return GameState.RUNNING;
    }
}
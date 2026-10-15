package logic;

import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.text.Normalizer;

/**
 * Hlavní třída logiky aplikace. Třída vytváří instanci třídy {@link GameWorld},
 * která inicializuje herní svět a vytváří instance tříd představující jednotlivé
 * herní příkazy. Třída také uchovává aktuální stav hry a zpracovává vstupy.
 * <p>
 * Tato třída také řeší normalizaci uživatelského vstupu (odstranění diakritiky,
 * převod na malá písmena) a mapování synonym příkazů.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class Game {
    private boolean gameOver;
    private GameWorld gameWorld;
    private Set<IAction> actions;
    private Map<String, String> commandAliases;

    /**
     * Konstruktor třídy. Vytvoří hru, inicializuje herní svět, seznam platných příkazů
     * a mapu synonym pro alternativní názvy příkazů.
     */
    public Game() {
        gameOver = false;
        gameWorld = new GameWorld();

        actions = new HashSet<>();
        actions.add(new ActionHelp());
        actions.add(new ActionTerminate(this));
        actions.add(new ActionMove(this));
        actions.add(new ActionInvestigate(this));
        actions.add(new ActionLookAround(this));
        actions.add(new ActionInventory(this));
        actions.add(new ActionTake(this));
        actions.add(new ActionDrop(this));
        actions.add(new ActionTalk(this));
        actions.add(new ActionUse(this));
        actions.add(new ActionAttack(this));
        actions.add(new ActionDefend(this));
        actions.add(new ActionFlee(this));
        actions.add(new ActionAnswer(this));

        commandAliases = new HashMap<>();

        commandAliases.put("bez", "jdi");

        commandAliases.put("vezmi", "seber");
        commandAliases.put("vem", "seber");

        commandAliases.put("prohledni", "prozkoumej");
        commandAliases.put("okoukni", "prozkoumej");

        commandAliases.put("napadni", "zautoc");
    }

    /**
     * Zjistí, zda hra již skončila (výhrou, prohrou nebo ukončením příkazem).
     *
     * @return true, pokud je hra ukončena; jinak false
     */
    public boolean isGameOver() {
        if (gameWorld.getGameState() != GameState.RUNNING) {
            return true;
        }
        return gameOver;
    }

    /**
     * Nastaví příznak ukončení hry.
     *
     * @param gameOver příznak ukončení hry
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * Vrátí odkaz na herní svět.
     *
     * @return herní svět
     */
    public GameWorld getGameWorld() {
        return gameWorld;
    }

    /**
     * Pomocná metoda pro odstranění diakritiky a převod na malá písmena.
     * Například vstup "Běž na JIH" převede na "bez na jih".
     *
     * @param input vstupní řetězec
     * @return normalizovaný řetězec bez diakritiky a malými písmeny
     */
    private String normalizeText(String input) {
        if (input == null) return null;

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        return normalized.toLowerCase();
    }

    /**
     * Zpracuje příkaz zadaný hráčem. Rozdělí vstupní řetězec na slova,
     * najde odpovídající příkaz a vykoná jej.
     * <p>
     * Před zpracováním je vstup normalizován (odstraněna diakritika) a
     * název příkazu je zkontrolován v mapě synonym.
     * Pokud příkaz proběhne úspěšně, aktualizuje také stavy lektvarů (cooldowny).
     *
     * @param line text, který hráč zadal na konzoli
     * @return odpověď hry na zadaný příkaz
     */
    public String processUserInput(String line) {
        if (isGameOver()) {
            return "Hra již skončila.";
        }

        String normalizedLine = normalizeText(line);
        String[] parts = normalizedLine.split("[ \t]+");

        if (parts.length == 0) {
            return "Zadej nějaký příkaz.";
        }

        String commandName = parts[0];

        if (commandAliases.containsKey(commandName)) {
            commandName = commandAliases.get(commandName);
        }

        String[] commandParameters = new String[parts.length - 1];
        for (int i = 0; i < commandParameters.length; i++) {
            commandParameters[i] = parts[i + 1];
        }

        String result = "Tomu nerozumím, tento příkaz neznám.";
        boolean commandExecuted = false;

        for (IAction action : actions) {
            if (action.getName().equalsIgnoreCase(commandName)) {
                result = action.execute(commandParameters);
                commandExecuted = true;
                break;
            }
        }

        if (commandExecuted) {
            String cooldownMsg = gameWorld.updateCooldowns();
            if (!cooldownMsg.isEmpty()) {
                result += "\n" + cooldownMsg;
            }
        }

        return result;
    }

    /**
     * Vrátí úvodní text pro hráče (prolog).
     *
     * @return úvodní text
     */
    public String getPrologue() {
        return "Vítejte ve hře Writers Demise!\n"
                + "Probouzíš se na mlžném předměstí surreálné Paříže.\n"
                + "Spisovatel a jeho inkoustové stvůry zabili tvou rodinu a zničili tvůj domov.\n"
                + "Tvým úkolem je najít Spisovatele, pomstít rodinu a ukončit tyranii inkoustu.\n\n"
                + "Nevíte-li, jak pokračovat, zadejte příkaz 'napoveda'.";
    }

    /**
     * Vrátí závěrečný text pro hráče (epilog) v závislosti na stavu hry.
     *
     * @return závěrečný text
     */
    public String getEpilogue() {
        String epilogue = "Díky, že sis zahrál(a).";
        if (gameWorld.getGameState() == GameState.WON) {
            epilogue = "GRATULACE! Spisovatel je poražen, svět je zachráněn.\n" + epilogue;
        } else if (gameWorld.getGameState() == GameState.LOST) {
            epilogue = "Bohužel jsi zemřel(a). Zkus to znovu a lépe!\n" + epilogue;
        }
        return epilogue;
    }
}
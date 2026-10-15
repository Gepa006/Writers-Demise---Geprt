package logic;

import java.util.Random;

/**
 * Třída implementující příkaz 'odpovez' pro řešení hádanek.
 * Specificky slouží pro interakci s Vrátným.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class ActionAnswer implements IAction {
    private Game game;
    private Random random;

    /**
     * Konstruktor třídy.
     *
     * @param game hra, ve které se bude příkaz používat
     */
    public ActionAnswer(Game game) {
        this.game = game;
        this.random = new Random();
    }

    /**
     * Vrátí název příkazu (odpovez).
     *
     * @return název příkazu
     */
    @Override
    public String getName() {
        return "odpovez";
    }

    /**
     * Zpracuje odpověď na hádanku. Pokud je správná, odstraní překážku (Vrátného).
     * Pokud je špatná, udělí hráči poškození.
     *
     * @param parameters parametry příkazu (text odpovědi)
     * @return výsledek hádanky
     */
    @Override
    public String execute(String[] parameters) {
        if (parameters.length < 1) {
            return "Co mám odpovědět?";
        }

        String answer = String.join(" ", parameters).toLowerCase();
        GameWorld world = game.getGameWorld();
        Area currentArea = world.getCurrentArea();

        if (!currentArea.containsCharacter("vratny")) {
            return "Tady není nikdo, komu bys mohl odpovídat na hádanky.";
        }

        if (answer.contains("33") || answer.contains("třicet tři") || answer.contains("tricet tri")) {
            currentArea.removeCharacter("vratny");
            return "Vrátný: 'Správně... Expedice 33... Můžeš projít.'\n"
                    + "(Vrátný se rozplynul v mlze. Cesta dál je volná.)";
        } else {
            int damage = 29 + random.nextInt(23);

            if (world.isResistanceActive()) {
                damage /= 2;
            }

            world.setHealth(world.getHealth() - damage);

            String output = "Vrátný: 'CHYBA! To není správně!'\n"
                    + "Vrátný na tebe v záchvatu vzteku zaútočil a ubral ti " + damage + " HP.\n"
                    + "(Zbývá ti " + world.getHealth() + " HP). Zkus to znovu nebo bojuj (zautoc).";

            if (world.getHealth() <= 0) {
                game.setGameOver(true);
                output += "\n\nZEMŘEL(A) JSI.";
            }

            return output;
        }
    }
}
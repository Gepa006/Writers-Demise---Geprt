package logic;

import java.util.Random;
import java.util.Iterator;

/**
 * Třída implementující příkaz pro útěk z boje.
 * Pokud hráč uspěje, přesune se do vedlejší lokace. Pokud ne, utrpí poškození.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class ActionFlee implements IAction {
    private Game game;
    private Random random;

    /**
     * Konstruktor třídy.
     *
     * @param game hra, ve které se bude příkaz používat
     */
    public ActionFlee(Game game) {
        this.game = game;
        this.random = new Random();
    }

    /**
     * Vrátí název příkazu (utec).
     *
     * @return název příkazu
     */
    @Override
    public String getName() {
        return "utec";
    }

    /**
     * Pokusí se o útěk z boje.
     * Úspěšnost závisí na náhodě nebo použití lektvaru mršnosti.
     * Boss neumožňuje útěk.
     *
     * @param parameters parametry příkazu (nevyužívají se)
     * @return výsledek pokusu o útěk
     */
    @Override
    public String execute(String[] parameters) {
        GameWorld world = game.getGameWorld();
        Area currentArea = world.getCurrentArea();

        String enemyName = null;
        for (String charName : currentArea.getCharacters()) {
            if (world.getEnemyHealth(charName) > 0) {
                enemyName = charName;
                break;
            }
        }

        if (enemyName == null) {
            return "Není před kým utíkat. Prostě použij příkaz 'jdi'.";
        }

        if (enemyName.equals("spisovatel")) {
            return "Spisovatel: 'Odsud není úniku! Příběh musí skončit!'\n"
                    + "(Před finálním bossem nelze utéct. Musíš bojovat!)";
        }

        boolean success = false;

        // S lektvarem mršnosti je útěk jistý
        if (world.isAgilityActive()) {
            success = true;
        } else {
            // Jinak 70% šance
            if (random.nextInt(100) < 70) {
                success = true;
            }
        }

        if (success) {
            Iterator<Area> it = currentArea.getExits().iterator();
            if (it.hasNext()) {
                Area exit = it.next();
                world.setCurrentArea(exit);
                return "Podařilo se ti proklouznout kolem nepřítele a utéct!\n" + exit.getFullDescription();
            } else {
                return "Chtěl(a) jsi utéct, ale není kam! (Slepá ulička)";
            }
        } else {
            int damage = 15;
            world.setHealth(world.getHealth() - damage);

            String output = "Nepodařilo se ti utéct! " + enemyName + " tě chytil a srazil k zemi.\n"
                    + "Ztratil(a) jsi " + damage + " HP. (Zbývá: " + world.getHealth() + ")\n";

            if (world.getHealth() <= 0) {
                game.setGameOver(true);
                output += "\nZemřel(a) jsi na útěku.";
            }
            return output;
        }
    }
}
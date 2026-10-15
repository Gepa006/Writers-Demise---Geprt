package logic;

import java.util.Random;

/**
 * Třída implementující příkaz pro obranu.
 * Hráč v tomto kole neútočí, ale výrazně sníží příchozí poškození od nepřítele.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class ActionDefend implements IAction {
    private Game game;
    private Random random;

    /**
     * Konstruktor třídy.
     *
     * @param game hra, ve které se bude příkaz používat
     */
    public ActionDefend(Game game) {
        this.game = game;
        this.random = new Random();
    }

    /**
     * Vrátí název příkazu (bran).
     *
     * @return název příkazu
     */
    @Override
    public String getName() {
        return "bran";
    }

    /**
     * Provede obranný manévr.
     * Zjistí nepřítele v lokaci, vypočítá jeho útok a sníží ho.
     *
     * @param parameters parametry příkazu (očekává se 'se')
     * @return výsledek obrany
     */
    @Override
    public String execute(String[] parameters) {
        if (parameters.length > 0 && !parameters[0].equalsIgnoreCase("se")) {
            return "Myslel(a) jsi 'bran se'?";
        }

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
            return "Nemáš se před kým bránit. Nikdo na tebe neútočí.";
        }

        StringBuilder output = new StringBuilder();
        output.append("Zaujímáš obranný postoj a kryješ si hlavu...\n");

        int enemyDamage;
        switch (enemyName) {
            case "komornik":
                enemyDamage = 20 + random.nextInt(14);
                break;
            case "strazny":
                enemyDamage = 20 + random.nextInt(23);
                break;
            case "elitni_strazce":
                enemyDamage = 25 + random.nextInt(26);
                break;
            case "spisovatel":
                enemyDamage = 50 + random.nextInt(51);
                break;
            default:
                enemyDamage = 5;
        }

        int reducedDamage = enemyDamage / 2;

        if (world.isResistanceActive()) {
            reducedDamage = reducedDamage / 2;
            output.append("(Tvůj lektvar abstrakce obranu ještě posílil!) ");
        }

        if (reducedDamage < 1) reducedDamage = 1;

        world.setHealth(world.getHealth() - reducedDamage);

        output.append("Nepřítel ").append(enemyName).append(" zaútočil, ale ty ses ubránil(a)!\n");
        output.append("Utrpěl(a) jsi jen ").append(reducedDamage).append(" poškození (místo původních ").append(enemyDamage).append(").\n");
        output.append("Zbývá ti ").append(world.getHealth()).append(" HP.");

        if (world.getHealth() <= 0) {
            game.setGameOver(true);
            output.append("\n\nI přes statečnou obranu jsi padl(a). ZEMŘEL(A) JSI.");
        }

        return output.toString();
    }
}
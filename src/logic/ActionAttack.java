package logic;

import java.util.Random;

/**
 * Třída implementující příkaz pro útok na nepřítele.
 * Zajišťuje výpočet poškození, mechaniku smrti nepřátel, dropování předmětů
 * a logiku protiútoku.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class ActionAttack implements IAction {
    private Game game;
    private Random random;

    /**
     * Konstruktor třídy.
     *
     * @param game hra, ve které se bude příkaz používat
     */
    public ActionAttack(Game game) {
        this.game = game;
        this.random = new Random();
    }

    /**
     * Vrátí název příkazu (zautoc).
     *
     * @return název příkazu
     */
    @Override
    public String getName() {
        return "zautoc";
    }

    /**
     * Provede útok na specifikovaného nepřítele.
     * Metoda kontroluje přítomnost nepřítele, počítá poškození hráče i nepřítele,
     * aplikuje efekty lektvarů a kontroluje podmínky vítězství či prohry.
     *
     * @param parameters parametry příkazu (jméno nepřítele)
     * @return textový výsledek souboje
     */
    @Override
    public String execute(String[] parameters) {
        if (parameters.length < 1) {
            return "Na koho mám zaútočit?";
        }

        String rawInput;
        if (parameters[0].equalsIgnoreCase("na") && parameters.length > 1) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < parameters.length; i++) {
                sb.append(parameters[i]).append(" ");
            }
            rawInput = sb.toString().trim();
        } else {
            rawInput = String.join(" ", parameters);
        }

        String enemyName = game.getGameWorld().translateName(rawInput);
        GameWorld world = game.getGameWorld();
        Area currentArea = world.getCurrentArea();

        if (!currentArea.containsCharacter(enemyName)) {
            return "Nikdo takový ('" + rawInput + "') tu není.";
        }

        int enemyHp = world.getEnemyHealth(enemyName);
        if (enemyHp <= 0) {
            return "Tato postava už je mrtvá.";
        }

        StringBuilder output = new StringBuilder();

        Item weapon = world.getInventory().getWeapon();
        int playerDamage = 10 + random.nextInt(11);
        String weaponName = "holé ruce";

        if (weapon != null) {
            weaponName = weapon.getName();
            if (weaponName.contains("dyka")) playerDamage = 25 + random.nextInt(11);
            else if (weaponName.contains("mec")) playerDamage = 38 + random.nextInt(23);
            else if (weaponName.contains("stetec")) playerDamage = 1000;
            else playerDamage = 5;
        }

        world.damageEnemy(enemyName, playerDamage);
        int newEnemyHp = world.getEnemyHealth(enemyName);

        output.append("Útočíš na ").append(enemyName).append(" zbraní '").append(weaponName)
                .append("' (").append(playerDamage).append(" dmg).\n");

        if (newEnemyHp <= 0) {
            currentArea.removeCharacter(enemyName);
            output.append("Nepřítel ").append(enemyName).append(" padl k zemi mrtvý!");

            if (enemyName.equals("komornik")) {
                currentArea.addItem(new Item("lektvar_rychlosti", "Lektvar rychlosti.", Item.Type.POTION));
                output.append("\n>>> Z Komorníka vypadl Lektvar rychlosti! <<<");
            }
            if (enemyName.equals("elitni_strazce")) {
                currentArea.addItem(new Item("magicky_stetec", "Magický štětec.", Item.Type.WEAPON));
                output.append("\n>>> Z Elitního strážce vypadl Magický štětec! <<<");
            }
            if (enemyName.equals("spisovatel")) {
                world.setSpisovatelJeMrtev(true);
                output.append("\n\nDOKÁZAL(A) JSI TO! Porazil(a) jsi Spisovatele!");
            }
            return output.toString();
        }

        output.append("(Nepříteli zbývá ").append(newEnemyHp).append(" HP)\n");

        if (world.isSpeedActive() && random.nextBoolean()) {
            output.append("\n>>> Jsi rychlý! Hraješ znovu. <<<");
            return output.toString();
        }

        if (world.isAgilityActive() && random.nextBoolean()) {
            output.append(">>> Díky mršnosti ses vyhnul! <<<");
            return output.toString();
        }

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
            case "vratny":
                enemyDamage = 29 + random.nextInt(23);
                break;
            default:
                enemyDamage = 5;
        }

        if (world.isResistanceActive()) {
            enemyDamage /= 2;
            output.append("(Lektvar abstrakce ztlumil ránu) ");
        }

        world.setHealth(world.getHealth() - enemyDamage);
        output.append(enemyName).append(" tě zasáhl za ").append(enemyDamage).append(" HP!\n");
        output.append("Zbývá ti ").append(world.getHealth()).append(" HP.");

        if (world.getHealth() <= 0) {
            game.setGameOver(true);
            output.append("\n\nZEMŘEL(A) JSI.");
        }

        return output.toString();
    }
}
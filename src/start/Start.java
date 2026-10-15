package start;

import logic.Game;
import ui.TextUI;

/**
 * Spouštěcí třída aplikace.
 * Obsahuje metodu main, která vytváří instanci hry a uživatelského rozhraní.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class Start
{
    /**
     * Spouštěcí metoda aplikace.
     * Vytvoří instanci hry a textového rozhraní, následně spustí hru.
     *
     * @param args parametry aplikace z příkazové řádky (nevyužity)
     */
    public static void main(String[] args)
    {
        Game game = new Game();
        TextUI ui = new TextUI(game);

        ui.play();
    }
}
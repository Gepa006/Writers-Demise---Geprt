package logic;

/**
 * Enumerace představuje možné stavy hry <i>(běžící hra, výhra, prohra apod.)</i>.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025, 2025-12-13
 */
public enum GameState {
    /**
     * Hra stále běží.
     */
    RUNNING,

    /**
     * Hráč vyhrál.
     */
    WON,

    /**
     * Hráč prohrál.
     */
    LOST
}

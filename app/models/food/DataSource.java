package models.food;

/**
 * Respresents the source that each {@link Food} nutrition data is from.
 * FINELI = Institutet för Hälsa och Välfärd <a href="https://fineli.fi/fineli/sv/index?</a>
 * SLV = Livsmedelsverket <a href="https://www.livsmedelsverket.se/livsmedelsdatabasen</a>
 *
 * @author Fredrik Kindstrom
 */
public enum DataSource {
    FINELI, SLV, USDA
}

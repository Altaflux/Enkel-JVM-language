package radium;


import radium.annotations.NotNull;

/**
 * This is the representation of Void in Radium
 * Its definition is overwritten by ClassTypeFactory, this class is just present
 * to allow resolving references to it, see com.kubadziworski.domain.type.UnitType.
 */
public class Unit {

    @NotNull
    public static final Unit INSTANCE = new Unit();

    private Unit() {
    }

    @NotNull
    public String toString() {
        return "radium.Unit";
    }
}

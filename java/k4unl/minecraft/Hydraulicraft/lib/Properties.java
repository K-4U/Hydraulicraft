package k4unl.minecraft.Hydraulicraft.lib;

import net.minecraft.block.properties.PropertyBool;

/**
 * @author Koen Beckers (K-4U)
 */
public final class Properties {
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");

    private Properties() {

    }
}

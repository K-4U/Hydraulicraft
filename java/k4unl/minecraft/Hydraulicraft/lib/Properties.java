package k4unl.minecraft.Hydraulicraft.lib;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.util.EnumFacing;

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


    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public static final PropertyEnum ROTATION = PropertyDirection.create("rotation", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);

    public static final PropertyBool CHILD = PropertyBool.create("child");

    public static final PropertyInteger LIGHTVALUE = PropertyInteger.create("lightValue", 0, 15);


    public static final PropertyBool HARVESTER_FRAME_ROTATED = PropertyBool.create("rotated");

    public static final PropertyBool HAS_RUBBER_SPOT = PropertyBool.create("hasRubberSpot");

    private Properties() {

    }
}

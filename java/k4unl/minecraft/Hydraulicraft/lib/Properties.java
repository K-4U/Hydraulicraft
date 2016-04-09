package k4unl.minecraft.Hydraulicraft.lib;

import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.util.EnumFacing;

/**
 * @author Koen Beckers (K-4U)
 */
public final class Properties {

    public static final PropertyBool UP    = PropertyBool.create("up");
    public static final PropertyBool DOWN  = PropertyBool.create("down");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST  = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST  = PropertyBool.create("west");

    public static final PropertyInteger UP_ICON    = PropertyInteger.create("upicon", 0, 45);
    public static final PropertyInteger DOWN_ICON  = PropertyInteger.create("downicon", 0, 45);
    public static final PropertyInteger NORTH_ICON = PropertyInteger.create("northicon", 0, 45);
    public static final PropertyInteger EAST_ICON  = PropertyInteger.create("easticon", 0, 45);
    public static final PropertyInteger SOUTH_ICON = PropertyInteger.create("southicon", 0, 45);
    public static final PropertyInteger WEST_ICON  = PropertyInteger.create("westicon", 0, 45);

    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public static final PropertyEnum<EnumFacing> ROTATION = PropertyDirection.create("rotation", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyEnum<EnumFacing> FACING   = PropertyEnum.create("facing", EnumFacing.class);

    public static final PropertyBool CHILD = PropertyBool.create("child");

    public static final PropertyInteger LIGHTVALUE = PropertyInteger.create("lightvalue", 0, 15);

    public static final PropertyBool HARVESTER_FRAME_ROTATED = PropertyBool.create("rotated");

    public static final PropertyBool HAS_RUBBER_SPOT = PropertyBool.create("hasrubberspot");
    public static final PropertyEnum TIER            = PropertyEnum.create("tier", PressureTier.class);

    private Properties() {

    }
}

package k4unl.minecraft.Hydraulicraft.multipart;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicTransporter;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import mcmultipart.microblock.IMicroblock;
import mcmultipart.multipart.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MultipartHandler {

    public static ItemPartHose              itemPartHose;
    public static ItemPartFluidPipe         itemPartFluidPipe;
    public static ItemPartFluidInterface    itemPartFluidInterface;
    public static ItemPartPortalFrame       itemPartPortalFrame;
    public static ItemPartRubberSuckingPipe itemPartRubberSuckingPipe;

    public static void init() {

        itemPartHose = new ItemPartHose();
        itemPartFluidPipe = new ItemPartFluidPipe();
        itemPartFluidInterface = new ItemPartFluidInterface();
        itemPartPortalFrame = new ItemPartPortalFrame();
        itemPartRubberSuckingPipe = new ItemPartRubberSuckingPipe();

        MultipartRegistry.registerPart(PartFluidInterface.class, Names.partFluidInterface.unlocalized);
        MultipartRegistry.registerPart(PartFluidPipe.class, Names.partFluidPipe.unlocalized);
        MultipartRegistry.registerPart(PartHose.class, Names.partHose[0].unlocalized);
        MultipartRegistry.registerPart(PartPortalFrame.class, Names.portalFrame.unlocalized);
        MultipartRegistry.registerPart(PartRubberSuckingPipe.class, Names.partRubberSuckingPipe.unlocalized);

        GameRegistry.registerItem(itemPartHose, Names.partHose[0].unlocalized);
        GameRegistry.registerItem(itemPartFluidPipe, Names.partFluidPipe.unlocalized);
        GameRegistry.registerItem(itemPartFluidInterface, Names.partFluidInterface.unlocalized);
        GameRegistry.registerItem(itemPartPortalFrame, Names.portalFrame.unlocalized);
        GameRegistry.registerItem(itemPartRubberSuckingPipe, Names.partRubberSuckingPipe.unlocalized);
    }

    public static <T> T getMultipart(Class<T> part, IMultipartContainer mp) {

        for (IMultipart p : mp.getParts()) {
            if (part.isAssignableFrom(p.getClass())) {
                return (T) p;
            }
        }
        return null;
    }

    public static <T extends Multipart> T getMultipart(Class<T> tClass, World world, BlockPos pos, EnumFacing side) {

        IMultipartContainer container = MultipartHelper.getPartContainer(world, pos);
        if (container == null) {
            return null;
        }

        if (side != null) {
            ISlottedPart part = container.getPartInSlot(PartSlot.getFaceSlot(side));
            if (part instanceof IMicroblock.IFaceMicroblock && !((IMicroblock.IFaceMicroblock) part).isFaceHollow()) {
                return null;
            }
        }

        ISlottedPart part = container.getPartInSlot(PartSlot.CENTER);
        if (tClass.isAssignableFrom(part.getClass())) {
            return (T) part;
        } else {
            return null;
        }
    }

    public static boolean hasMultipart(Class<?> part, IMultipartContainer mp) {

        return getMultipart(part, mp) != null;
    }

    public static boolean hasTransporter(IMultipartContainer mp) {

        return hasMultipart(IHydraulicTransporter.class, mp);
    }

    public static IHydraulicTransporter getTransporter(IMultipartContainer mp) {

        return getMultipart(IHydraulicTransporter.class, mp);
    }

    public static PartHose getHose(IMultipartContainer mp) {

        return getMultipart(PartHose.class, mp);
    }

    public static boolean hasPartFluidPipe(IMultipartContainer mp) {

        return getMultipart(PartFluidPipe.class, mp) != null;
    }

    public static PartFluidPipe getFluidPipe(IMultipartContainer mp) {

        return getMultipart(PartFluidPipe.class, mp);
    }

    public static PartRubberSuckingPipe getRubberSuckingPipe(IMultipartContainer mp) {

        return getMultipart(PartRubberSuckingPipe.class, mp);
    }

    public static boolean hasPartPortalFrame(IMultipartContainer mp) {

        return getMultipart(PartPortalFrame.class, mp) != null;
    }

    public static PartFluidPipe getFluidPipe(World world, BlockPos blockPos, EnumFacing side) {

        return getMultipart(PartFluidPipe.class, world, blockPos, side);
    }

    public static PartRubberSuckingPipe getRubberSuckingPipe(World world, BlockPos blockPos, EnumFacing side) {

        return getMultipart(PartRubberSuckingPipe.class, world, blockPos, side);
    }


    public static PartHose getPartHose(World world, BlockPos blockPos, EnumFacing side) {

        return getMultipart(PartHose.class, world, blockPos, side);
    }


    public static PartPortalFrame getPartPortalFrame(World world, BlockPos blockPos, EnumFacing side) {

        return getMultipart(PartPortalFrame.class, world, blockPos, side);
    }


    public static PartPortalFrame getPartPortalFrame(IMultipartContainer mp) {

        return getMultipart(PartPortalFrame.class, mp);
    }

}

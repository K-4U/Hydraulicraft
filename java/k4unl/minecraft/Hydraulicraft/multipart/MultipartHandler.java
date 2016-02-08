package k4unl.minecraft.Hydraulicraft.multipart;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicTransporter;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.k4lib.lib.Location;
import mcmultipart.microblock.IMicroblock;
import mcmultipart.multipart.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Collection;

public class MultipartHandler {
    public static ItemPartHose           itemPartHose;
    public static ItemPartFluidPipe      itemPartFluidPipe;
    public static ItemPartFluidInterface itemPartFluidInterface;
    public static ItemPartPortalFrame    itemPartPortalFrame;

    public static void init() {

        itemPartHose = new ItemPartHose();
        itemPartFluidPipe = new ItemPartFluidPipe();
        itemPartFluidInterface = new ItemPartFluidInterface();
        itemPartPortalFrame = new ItemPartPortalFrame();

        MultipartRegistry.registerPart(PartFluidInterface.class, Names.partFluidInterface.unlocalized);
        MultipartRegistry.registerPart(PartFluidPipe.class, Names.partFluidPipe.unlocalized);
        MultipartRegistry.registerPart(PartHose.class, Names.partHose[0].unlocalized);
        MultipartRegistry.registerPart(PartPortalFrame.class, Names.portalFrame.unlocalized);

        GameRegistry.registerItem(itemPartHose, Names.partHose[0].unlocalized);
        GameRegistry.registerItem(itemPartFluidPipe, Names.partFluidPipe.unlocalized);
        GameRegistry.registerItem(itemPartFluidInterface, Names.partFluidInterface.unlocalized);
        GameRegistry.registerItem(itemPartPortalFrame, Names.portalFrame.unlocalized);
    }

    public static IMultipartContainer getMultipartTile(IBlockAccess access, Location pos) {

        TileEntity te = access.getTileEntity(pos.toBlockPos());
        return te instanceof IMultipartContainer ? (IMultipartContainer) te : null;
    }

    public static boolean hasTransporter(IMultipartContainer mp) {

        boolean ret = false;
        Collection<? extends IMultipart> t = mp.getParts();
        for (IMultipart p : t) {
            if (!ret) {
                if (p instanceof IHydraulicTransporter) {
                    ret = true;
                }
            }
        }
        return ret;
    }

    public static boolean hasPartHose(IMultipartContainer mp) {

        boolean ret = false;
        Collection<? extends IMultipart> t = mp.getParts();
        for (IMultipart p : t) {
            if (!ret) {
                if (p instanceof PartHose) {
                    ret = true;
                }
            }
        }
        return ret;
    }

    public static PartHose getHose(IMultipartContainer mp) {

        Collection<? extends IMultipart> t = mp.getParts();
        for (IMultipart p : t) {
            if (p instanceof PartHose) {
                return (PartHose) p;
            }
        }
        return null;
    }

    public static IHydraulicTransporter getTransporter(IMultipartContainer mp) {

        Collection<? extends IMultipart> t = mp.getParts();
        for (IMultipart p : t) {
            if (p instanceof IHydraulicTransporter) {
                return (IHydraulicTransporter) p;
            }
        }
        return null;
    }

    public static boolean hasPartFluidHandler(IMultipartContainer mp) {

        Collection<? extends IMultipart> t = mp.getParts();
        for (IMultipart p : t) {
            if (p instanceof IFluidHandler) {
                return true;
            }
        }
        return false;
    }

    public static IFluidHandler getFluidHandler(IMultipartContainer mp) {

        Collection<? extends IMultipart> t = mp.getParts();
        for (IMultipart p : t) {
            if (p instanceof IFluidHandler) {
                return (IFluidHandler) p;
            }
        }
        return null;
    }

    public static boolean hasPartFluidPipe(IMultipartContainer mp) {

        Collection<? extends IMultipart> t = mp.getParts();
        for (IMultipart p : t) {
            if (p instanceof PartFluidPipe) {
                return true;
            }
        }
        return false;
    }

    public static PartFluidPipe getFluidPipe(IMultipartContainer mp) {

        Collection<? extends IMultipart> t = mp.getParts();
        for (IMultipart p : t) {
            if (p instanceof PartFluidPipe) {
                return (PartFluidPipe) p;
            }
        }
        return null;
    }

    public static boolean hasPartPortalFrame(IMultipartContainer mp) {

        Collection<? extends IMultipart> t = mp.getParts();
        for (IMultipart p : t) {
            if (p instanceof PartPortalFrame) {
                return true;
            }
        }
        return false;
    }

    public static PartFluidPipe getFluidPipe(World world, BlockPos blockPos, EnumFacing side) {

        IMultipartContainer container = MultipartHelper.getPartContainer(world, blockPos);
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
        if (part instanceof PartFluidPipe) {
            return (PartFluidPipe) part;
        } else {
            return null;
        }
    }


    public static PartHose getPartHose(World world, BlockPos blockPos, EnumFacing side) {

        IMultipartContainer container = MultipartHelper.getPartContainer(world, blockPos);
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
        if (part instanceof PartHose) {
            return (PartHose) part;
        } else {
            return null;
        }
    }


    public static PartPortalFrame getPartPortalFrame(World world, BlockPos blockPos, EnumFacing side) {

        IMultipartContainer container = MultipartHelper.getPartContainer(world, blockPos);
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
        if (part instanceof PartPortalFrame) {
            return (PartPortalFrame) part;
        } else {
            return null;
        }
    }


    public static PartPortalFrame getPartPortalFrame(IMultipartContainer mp) {

        Collection<? extends IMultipart> t = mp.getParts();
        for (IMultipart p : t) {
            if (p instanceof PartPortalFrame) {
                return (PartPortalFrame) p;
            }
        }
        return null;
    }

}

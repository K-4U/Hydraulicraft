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
    public static ItemPartHose itemPartHose;
    /*public static ItemPartValve itemPartValve;*/
    public static ItemPartFluidPipe itemPartFluidPipe;
    public static ItemPartFluidInterface itemPartFluidInterface;

    public static void init() {

		itemPartHose = new ItemPartHose();
        /*itemPartValve = new ItemPartValve();*/
		itemPartFluidPipe = new ItemPartFluidPipe();
        itemPartFluidInterface = new ItemPartFluidInterface();

        MultipartRegistry.registerPart(PartFluidInterface.class, Names.partFluidInterface.unlocalized);
        MultipartRegistry.registerPart(PartFluidPipe.class, Names.partFluidPipe.unlocalized);
        MultipartRegistry.registerPart(PartHose.class, Names.partHose[0].unlocalized);

        GameRegistry.registerItem(itemPartHose, Names.partHose[0].unlocalized);
        /*GameRegistry.registerItem(itemPartValve, Names.partValve[0].unlocalized);*/
        GameRegistry.registerItem(itemPartFluidPipe, Names.partFluidPipe.unlocalized);
        GameRegistry.registerItem(itemPartFluidInterface, Names.partFluidInterface.unlocalized);
    }

    public static IMultipartContainer getMultipartTile(IBlockAccess access, Location pos) {
        TileEntity te = access.getTileEntity(pos.toBlockPos());
        return te instanceof IMultipartContainer ? (IMultipartContainer) te : null;
    }
/*
	public static Multipart getMultiPart(IBlockAccess w, Location bc, int part) {
		IMultipartContainer t = getMultipartTile(w, bc);
		if (t != null)
			return t.part(part);

		return null;
	}*/

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
    }/*

    public static boolean hasPartValve(IMultipartContainer mp) {
        boolean ret = false;
        Collection<? extends IMultipart> t = mp.getParts();
        for (IMultipart p : t) {
            if (!ret) {
                if (p instanceof PartValve) {
                    ret = true;
                }
            }
        }
        return ret;
    }

    public static PartValve getValve(IMultipartContainer mp) {
        Collection<? extends IMultipart> t = mp.getParts();
        for (IMultipart p : t) {
            if (p instanceof PartValve) {
                return (PartValve) p;
            }
        }
        return null;
    }*/


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
/*
	public static void updateMultiPart(TMultiPart tMp) {
		MCDataOutput writeStream = tMp.tile().getWriteStream(tMp);
		tMp.writeDesc(writeStream);
	}*/
}

package k4unl.minecraft.Hydraulicraft.tileEntities.gow;

import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.multipart.MultipartHandler;
import k4unl.minecraft.Hydraulicraft.network.NetworkHandler;
import k4unl.minecraft.Hydraulicraft.network.packets.PacketPortalEnabled;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBaseNoPower;
import k4unl.minecraft.k4lib.lib.Location;
import mcmultipart.block.TileMultipart;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;


public class TilePortalTeleporter extends TileHydraulicBaseNoPower {
    private boolean hasSendPacket         = true;
    private float   transparancy          = 0.2F;
    private float   prevTransparancy      = 0.2F;
    private float   directionTransparency = 0.01F;
    private EnumFacing baseDir;
    private EnumFacing portalDir;
    private Location   portalBase;

    //TODO: rewrite me to not use a TESR!
    @Override
    public void readFromNBT(NBTTagCompound tCompound) {

        super.readFromNBT(tCompound);

        baseDir = EnumFacing.byName(tCompound.getString("baseDir"));
        portalDir = EnumFacing.byName(tCompound.getString("portalDir"));

        portalBase = new Location(tCompound.getIntArray("portalBase"));
    }

    @Override
    public void writeToNBT(NBTTagCompound tCompound) {

        super.writeToNBT(tCompound);
        tCompound.setString("baseDir", baseDir.toString());
        tCompound.setString("portalDir", portalDir.toString());

        if (portalBase != null) {
            tCompound.setIntArray("portalBase", portalBase.getIntArray());
        }
    }

    public void setRotation(EnumFacing _baseDir, EnumFacing _portalDir) {
        baseDir = _baseDir;
        portalDir = _portalDir;
        hasSendPacket = false;
    }

    public TilePortalBase getPortalBase() {
        if (portalBase == null) {
            return null;
        } else {
            return (TilePortalBase) portalBase.getTE(getWorld());
        }
    }

    @Override
    public void update() {
        if (!getWorld().isRemote && !hasSendPacket && baseDir != null) {
            hasSendPacket = true;
            NetworkHandler.INSTANCE.sendToAllAround(new PacketPortalEnabled(getPos(), baseDir, portalDir), getWorld());
        }
        if (getWorld().isRemote) {
            prevTransparancy = transparancy;
            transparancy += directionTransparency;
            if (transparancy >= 0.8F) {
                directionTransparency = -0.01F;
            } else if (transparancy <= 0.3F) {
                directionTransparency = 0.01F;
            }
        }
    }


    public EnumFacing getBaseDir() {
        return baseDir;
    }

    public EnumFacing getPortalDir() {
        return portalDir;
    }

    public void teleport(Entity ent) {

    }

    public float getTransparancy(float frame) {
        return transparancy + ((prevTransparancy - transparancy) * frame);
    }

    public void setBase(TilePortalBase tilePortalBase) {
        portalBase = new Location(tilePortalBase.getPos());
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    public boolean isEdge(EnumFacing dir) {
        Location nLoc = new Location(getPos(), dir);

        return ((nLoc.getTE(getWorld()) instanceof TileMultipart) && MultipartHandler.hasPartPortalFrame(((TileMultipart)nLoc.getTE(getWorld()))
          .getPartContainer()));
    }

    public void usePressure() {
        getPortalBase().getHandler().setPressure(getPortalBase().getPressure
                (EnumFacing.UP) - HCConfig.INSTANCE.getInt("pressurePerTeleport"), EnumFacing.UP);
    }
}

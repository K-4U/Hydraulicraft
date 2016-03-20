package k4unl.minecraft.Hydraulicraft.network.packets;

import io.netty.buffer.ByteBuf;
import k4unl.minecraft.Hydraulicraft.tileEntities.rubberHarvesting.TileRubberTap;
import k4unl.minecraft.k4lib.network.messages.LocationIntPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Koen Beckers (K-4U)
 */
public class PacketHarvestingRubber extends LocationIntPacket<PacketHarvestingRubber> {

    private int     tankDepth = 0;
    private boolean isTapping = false;

    public PacketHarvestingRubber() {

    }

    public PacketHarvestingRubber(BlockPos pos, boolean isTapping, int tankDepth) {

        super(pos);
        this.isTapping = isTapping;
        this.tankDepth = tankDepth;
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        super.fromBytes(buf);
        isTapping = buf.readBoolean();
        tankDepth = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {

        super.toBytes(buf);
        buf.writeBoolean(isTapping);
        buf.writeInt(tankDepth);
    }

    @Override
    public void handleClientSide(PacketHarvestingRubber message, EntityPlayer player) {

        World world = player.worldObj;
        if (world.getTileEntity(message.pos) instanceof TileRubberTap) {
            ((TileRubberTap) world.getTileEntity(message.pos)).setTapping(message.isTapping);
            ((TileRubberTap) world.getTileEntity(message.pos)).setTankDepth(message.tankDepth);
        }
    }

    @Override
    public void handleServerSide(PacketHarvestingRubber message, EntityPlayer player) {

    }
}

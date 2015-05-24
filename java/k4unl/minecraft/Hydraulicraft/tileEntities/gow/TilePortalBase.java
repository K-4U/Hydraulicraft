package k4unl.minecraft.Hydraulicraft.tileEntities.gow;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicConsumer;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.items.ItemIPCard;
import k4unl.minecraft.Hydraulicraft.lib.IPs;
import k4unl.minecraft.Hydraulicraft.lib.config.HCConfig;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import k4unl.minecraft.k4lib.lib.Location;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class TilePortalBase extends TileHydraulicBase implements IInventory, IHydraulicConsumer {
    private boolean        portalFormed;
    private boolean        portalEnabled;
    private int            portalWidth;
    private int            portalHeight;
    private ForgeDirection baseDir;
    private ForgeDirection portalDir;
    private List<Location> frames;
    private long           ip;
    private boolean ipRegistered = false;
    private int     colorIndex   = 0;


    private boolean hasInterfaceValve = false;
    private Location interfaceValveLocation;

    private boolean hasHydraulicValve = false;
    private Location hydraulicValveLocation;

    private ItemStack linkingCard;

    public TilePortalBase(){
        super(1);
        super.init(this);
    }

    public TilePortalBase(PressureTier tier) {

        super(200);
        super.init(this);
        frames = new ArrayList<Location>();
    }

    private void genNewIP() {

        if (ip != 0) {
            Hydraulicraft.ipList.removeIP(ip);
        }
        if (getWorldObj() != null) {
            String IP = Hydraulicraft.ipList.generateNewRandomIP(getWorldObj().provider.dimensionId);
            Hydraulicraft.ipList.registerIP(IPs.ipToLong(IP), new Location(xCoord, yCoord, zCoord, getWorldObj().provider.dimensionId));
            ipRegistered = false;
            ip = IPs.ipToLong(IP);
            getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tCompound) {

        super.readFromNBT(tCompound);
        portalFormed = tCompound.getBoolean("portalFormed");
        portalEnabled = tCompound.getBoolean("portalEnabled");
        portalWidth = tCompound.getInteger("portalWidth");
        portalHeight = tCompound.getInteger("portalHeight");

        baseDir = ForgeDirection.getOrientation(tCompound.getInteger("baseDir"));
        portalDir = ForgeDirection.getOrientation(tCompound.getInteger("portalDir"));
		
		NBTTagCompound linkCardNBT = tCompound.getCompoundTag("linkingCard");
		if(linkCardNBT != null){
			linkingCard = ItemStack.loadItemStackFromNBT(linkCardNBT);
		}
		ip = tCompound.getLong("ip");
		if(ip == 0 && FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER){
			genNewIP();
		}else if(ip != 0){
			ipRegistered = false;
		}
		
		colorIndex = tCompound.getInteger("colorIndex");
		hasInterfaceValve = tCompound.getBoolean("hasInterfaceValve");
		
		readFramesFromNBT(tCompound);
	}
	
	private void readFramesFromNBT(NBTTagCompound tCompound){
		frames.clear();
		NBTTagCompound list = tCompound.getCompoundTag("portalFrames");
		int i = 0;
		for(i = 0; i < list.getInteger("max"); i++){
			Location frameLocation = new Location(list.getIntArray(""+i));
			frames.add(frameLocation);
		}
	}
	
	private void writeFramesToNBT(NBTTagCompound tCompound){
		NBTTagCompound list = new NBTTagCompound();
		int i = 0;
		for(Location fr : frames){
			list.setIntArray("" + i, fr.getIntArray());
			i++;
		}
		list.setInteger("max", frames.size());
		
		tCompound.setTag("portalFrames", list);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tCompound){
		super.writeToNBT(tCompound);
		
		tCompound.setBoolean("portalFormed", portalFormed);
		tCompound.setBoolean("portalEnabled", portalEnabled);
		tCompound.setInteger("portalWidth", portalWidth);
		tCompound.setInteger("portalHeight", portalHeight);
		
		if(baseDir != null){
			tCompound.setInteger("baseDir", baseDir.ordinal());
		}
		if(portalDir != null){
			tCompound.setInteger("portalDir", portalDir.ordinal());
		}
		if(linkingCard != null){
			NBTTagCompound linkCardNBT = linkingCard.writeToNBT(new NBTTagCompound());
			tCompound.setTag("linkingCard", linkCardNBT);
		}
		
		tCompound.setLong("ip", ip);
		tCompound.setInteger("colorIndex", colorIndex);

		tCompound.setBoolean("hasInterfaceValve", hasInterfaceValve);

		writeFramesToNBT(tCompound);
	}
	
	@Override
	public void updateEntity(){
		super.updateEntity();

		if(ip == 0){
			if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER){
				genNewIP();
			}
		}
		//Every 10 ticks, check for a complete portal.
		if(getWorldObj().getTotalWorldTime() % 20 == 0 && !getWorldObj().isRemote){
			if(checkPortalComplete()){
				if(portalFormed){
					invalidatePortal();
				}else{
					validatePortal();
				}
			}
		}
		if(!ipRegistered){
			ipRegistered = true;
			Hydraulicraft.ipList.registerIP(ip, new Location(xCoord,yCoord,zCoord, worldObj.provider.dimensionId));
		}
	}
	
	private boolean checkPortalComplete(){
		int i = 0;
		baseDir = ForgeDirection.NORTH;
		portalWidth = 0;
		int half = 0;
		while(i != 2){
			for(int z = 1; z <= HCConfig.INSTANCE.getInt("maxPortalWidth"); z++){
				Location nLocation = new Location(xCoord, yCoord, zCoord, baseDir, z);
				Location oLocation = new Location(xCoord, yCoord, zCoord, baseDir.getOpposite(), z);
				if(nLocation.getBlock(getWorldObj()) == HCBlocks.portalFrame){
					portalWidth++;
					half++;
				}else{
					break;
				}
				if(oLocation.getBlock(getWorldObj()) == HCBlocks.portalFrame){
					portalWidth++;
				}else{
					break;
				}
			}
			if(portalWidth > 0 && portalWidth % 2 == 0){
				//Valid portal found.
				//Break out of loop
				break;
			}else{
				portalWidth = 0;
			}
			
			baseDir = ForgeDirection.EAST;
			i++;
		}
		if(portalWidth == 0 || portalWidth % 2 != 0){
			return false;
		}
		
		
		//Now, that is the bottom taken care of. Let's see about the rest!
		i = 0;
		portalDir = baseDir.getRotation(ForgeDirection.UP);
		Location firstLocation = new Location(xCoord, yCoord, zCoord, baseDir, half);
		Location secondLocation = new Location(xCoord, yCoord, zCoord, baseDir.getOpposite(), half);
		portalHeight = 0;
		while(i != 3){
			//Log.info("Checking for portal with basedir at " + baseDir + " and top at " + portalDir);
			for(int y = 1; y <= HCConfig.INSTANCE.getInt("maxPortalHeight"); y++){
				Location nLocation = new Location(firstLocation, portalDir, y);
				Location oLocation = new Location(secondLocation, portalDir, y);
				if(nLocation.getBlock(getWorldObj()) == HCBlocks.portalFrame){
					portalHeight++;
				}else{
					break;
				}
				if(oLocation.getBlock(getWorldObj()) != HCBlocks.portalFrame){
					break;
				}
			}
			
			if(portalHeight > 1){
				break;
			}
			portalDir = portalDir.getRotation(baseDir);
			portalHeight = 0;
			i++;
		}
		
		if(portalHeight == 0){
			return false;
		}
		
		//Check other side (aka top):
		Location topCenter = new Location(xCoord, yCoord, zCoord, portalDir, portalHeight);
		if(topCenter.getBlock(getWorldObj()) != HCBlocks.portalFrame){
			return false;
		}
		for(int x = 1; x <= half; x++){
			Location nLocation = new Location(xCoord, yCoord, zCoord, baseDir, x);
			Location oLocation = new Location(xCoord, yCoord, zCoord, baseDir.getOpposite(), x);
			if(nLocation.getBlock(getWorldObj()) != HCBlocks.portalFrame){
				return false;
			}
			if(oLocation.getBlock(getWorldObj()) != HCBlocks.portalFrame){
				return false;
			}
		}

		//Now check the insides.

		
		//Log.info("Found a portal. It's " + portalWidth + " wide and " + portalHeight + " high in " + baseDir + " with the portal in the " + portalDir);
		
		return true;
	}
	
	private void validatePortal(){
		frames.clear();
		Location bottomLeft = new Location(xCoord, yCoord, zCoord, baseDir, (portalWidth/2));
		Location bottomRight = new Location(xCoord, yCoord, zCoord, baseDir.getOpposite(), (portalWidth/2));
		if(bottomLeft.getBlock(getWorldObj()) != HCBlocks.portalFrame){
			return;
		}
		
		for(int x = 0; x <= portalWidth+1; x++){
			Location handleLocation = new Location(bottomLeft, baseDir.getOpposite(), x);
			Location topLocation = new Location(handleLocation, portalDir, portalHeight);
			TileEntity te = handleLocation.getTE(getWorldObj());
			if(te instanceof TilePortalFrame){
				((TilePortalFrame)te).setPortalBase(this);
				((TilePortalFrame)te).dye(colorIndex);
				frames.add(handleLocation);
			}
			te = topLocation.getTE(getWorldObj());
			if(te instanceof TilePortalFrame){
				((TilePortalFrame)te).setPortalBase(this);
				((TilePortalFrame)te).dye(colorIndex);
				frames.add(topLocation);
			}
		}
		for(int y = 0; y <= portalHeight; y++){
			Location leftLocation = new Location(bottomLeft, portalDir, y);
			Location rightLocation = new Location(bottomRight, portalDir, y);
			TileEntity te = leftLocation.getTE(getWorldObj());
			if(te instanceof TilePortalFrame){
				((TilePortalFrame)te).setPortalBase(this);
				((TilePortalFrame)te).dye(colorIndex);
				frames.add(leftLocation);
			}
			te = rightLocation.getTE(getWorldObj());
			if(te instanceof TilePortalFrame){
				((TilePortalFrame)te).setPortalBase(this);
				((TilePortalFrame)te).dye(colorIndex);
				frames.add(rightLocation);
			}
		}
		
		portalFormed = true;
		markDirty();
	}
	
	private void invalidatePortal(){
		
	}
	
	
	@Override
	public void validate(){
		super.validate();
	}
	
	
	
	@Override
	public void redstoneChanged(boolean isPowered){
		if(getWorldObj() != null){
			if(portalFormed && linkingCard != null){
				if(portalEnabled && !getIsRedstonePowered()){
					portalEnabled = false;
					disablePortal();
				}else if(getIsRedstonePowered() && !portalEnabled && getPressure(ForgeDirection.UP) >= HCConfig.INSTANCE.getInt("portalmBarUsagePerTickPerBlock")){
					portalEnabled = true;
					enablePortal();
				}
				markDirty();
			}
		}
		
	}
	
	private void enablePortal(){
		if(baseDir != null){
			Location bottomLeft = new Location(xCoord, yCoord, zCoord, baseDir, (portalWidth/2));
			bottomLeft.offset(baseDir.getOpposite(), 1);
			bottomLeft.offset(portalDir, 1);
			for(int x = 0; x <= portalWidth-2; x++){
				Location handleLocation = new Location(bottomLeft, baseDir.getOpposite(), x);
				for(int y = 0; y < portalHeight-1; y++){
					Location portalLocation = new Location(handleLocation, portalDir, y);
					getWorldObj().setBlock(portalLocation.getX(), portalLocation.getY(), portalLocation.getZ(), HCBlocks.portalTeleporter);
					
					TilePortalTeleporter teleporter = (TilePortalTeleporter)portalLocation.getTE(getWorldObj());
					teleporter.setRotation(baseDir, portalDir);
					teleporter.setBase(this);
				}
			}
			for(Location fr : frames){
				if(fr.getTE(getWorldObj()) != null){
					if(fr.getTE(getWorldObj()) instanceof TilePortalFrame){
						((TilePortalFrame)fr.getTE(getWorldObj())).setActive(true);
					}
				}
			}
		}
	}
	
	private void disablePortal(){
		if(baseDir != null){
			Location bottomLeft = new Location(xCoord, yCoord, zCoord, baseDir, (portalWidth/2));
			bottomLeft.offset(baseDir.getOpposite(), 1);
			bottomLeft.offset(portalDir, 1);
			for(int x = 0; x <= portalWidth-2; x++){
				Location handleLocation = new Location(bottomLeft, baseDir.getOpposite(), x);
				for(int y = 0; y < portalHeight-1; y++){
					Location portalLocation = new Location(handleLocation, portalDir, y);
					getWorldObj().setBlockToAir(portalLocation.getX(), portalLocation.getY(), portalLocation.getZ());
				}
			}
			for(Location fr : frames){
				if(fr.getTE(getWorldObj()) != null){
					((TilePortalFrame)fr.getTE(getWorldObj())).setActive(false);
				}
			}
		}
	}
	
	public boolean getIsActive() {
		return portalEnabled;
	}

	public String getIPString() {
		if(ip == 0 && !getWorldObj().isRemote){
			genNewIP();
		}
		return IPs.longToIp(ip);
	}
	
	public Long getIPLong() {
		return ip;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return linkingCard;
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		ItemStack tempStack = linkingCard.copy();
		linkingCard = null;
		return tempStack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return linkingCard;
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		if(var1 == 0){
			linkingCard = var2;
		}
	}

	@Override
	public String getInventoryName() {
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {	}

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		return (var2.getItem() instanceof ItemIPCard);
	}

	public boolean getIsValid() {
		return portalFormed;
	}
	
	public Location getTarget(){
		//First: see if there's an item in the inventory:
		if(linkingCard == null) return null;
		//Next, see if it linked:
		if(linkingCard.getTagCompound() == null) return null;
		if(linkingCard.getTagCompound().getLong("linked") == 0) return null;
		long linked = linkingCard.getTagCompound().getLong("linked");
		if(Hydraulicraft.ipList.getLocation(linked) == null) return null;
		Location l = new Location(Hydraulicraft.ipList.getLocation(linked), portalDir, 1);
		return l;
	}

	public void dye(int i) {
		colorIndex = i;
		markDirty();
		getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
		//Update frames
		if(portalFormed){
			for(Location fr : frames){
				if(fr.getTE(getWorldObj()) != null){
					((TilePortalFrame)fr.getTE(getWorldObj())).dye(i);
				}
			}
		}
	}
	
	public int getDye(){
		return colorIndex;
	}
	
	public Location getBlockLocation() {
		return new Location(xCoord, yCoord, zCoord);
	}

	@Override
	public void onFluidLevelChanged(int old) {	}

	@Override
	public boolean canConnectTo(ForgeDirection side) {
		if(!side.equals(ForgeDirection.UP)){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public float workFunction(boolean simulate, ForgeDirection from) {
		if(from.equals(ForgeDirection.UP) && getIsActive()){
			if(getPressure(ForgeDirection.UP) >= (HCConfig.INSTANCE.getInt("portalmBarUsagePerTickPerBlock") * getBlockLocation().getDifference(getTarget()))){
				if(getIsActive()) {
					return HCConfig.INSTANCE.getInt("portalmBarUsagePerTickPerBlock") * getBlockLocation().getDifference(getTarget());
				}
			}else{
				if(getIsActive()){
					disablePortal();
				}
			}
		}
		return 0;
	}

	@Override
	public boolean canWork(ForgeDirection dir) {
		return dir.equals(ForgeDirection.UP);
	}

}


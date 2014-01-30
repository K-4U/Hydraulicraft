package k4unl.minecraft.Hydraulicraft.lib.helperClasses;

public class Seed {
	private final int harvesterMeta;
	private final int seedId;
	private final int itemId;
	private final int fullGrown;
	
	public Seed(int harvester, int iid, int grown){
		harvesterMeta = harvester;
		itemId = iid;
		fullGrown = grown;
		seedId = iid;
	}
	
	public Seed(int harvester, int iid, int grown, int sId){
		harvesterMeta = harvester;
		itemId = iid;
		fullGrown = grown;
		seedId = sId;
	}
	
	public int getHarvesterMeta(){
		return harvesterMeta;
	}
	
	public int getItemId(){
		return itemId;
	}
	
	public int getFullGrown(){
		return fullGrown;
	}
	
	public int getSeedId(){
		return seedId;
	}
}

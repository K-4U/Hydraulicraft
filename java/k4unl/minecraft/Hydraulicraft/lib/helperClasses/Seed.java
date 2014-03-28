package k4unl.minecraft.Hydraulicraft.lib.helperClasses;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class Seed {
	private final int harvesterMeta;
	private final Item seedId;
	private final Block itemId;
	private final int fullGrown;
	/*
	public Seed(int harvester, Item iSeed, Item iGrown){
		harvesterMeta = harvester;
		itemId = iSeed;
		fullGrown = iGrown;
		seedId = iSeed;
	}
	*/
	public Seed(int harvester, Block iid, int grown, Item sId){
		harvesterMeta = harvester;
		itemId = iid;
		fullGrown = grown;
		seedId = sId;
	}
	
	public int getHarvesterMeta(){
		return harvesterMeta;
	}
	
	public Block getItemId(){
		return itemId;
	}
	
	public int getFullGrown(){
		return fullGrown;
	}
	
	public Item getSeedId(){
		return seedId;
	}
}

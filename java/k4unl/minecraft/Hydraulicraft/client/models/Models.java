package k4unl.minecraft.Hydraulicraft.client.models;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.ores.Ores;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class Models {

    public static void init() {
        itemBlockModels();
        itemModels();
    }

    private static void itemBlockModels() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(HCBlocks.hydraulicCrusher), 0, new ModelResourceLocation("hydcraft:hydraulicCrusher", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(HCBlocks.hydraulicFiller), 0, new ModelResourceLocation("hydcraft:hydraulicFiller", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(HCBlocks.hydraulicFilter), 0, new ModelResourceLocation("hydcraft:hydraulicFilter", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(HCBlocks.hydraulicFrictionIncinerator), 0, new ModelResourceLocation("hydcraft:hydraulicFrictionIncinerator", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(HCBlocks.hydraulicPressureGlass), 0, new ModelResourceLocation("hydcraft:hydraulicPressureGlass", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(HCBlocks.hydraulicPressureWall), 0, new ModelResourceLocation("hydcraft:hydraulicPressureWall", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(HCBlocks.hydraulicWasher), 0, new ModelResourceLocation("hydcraft:hydraulicWasher", "inventory"));

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Ores.oreCopperReplacement), 0, new ModelResourceLocation("hydcraft:oreCopper", "inventory"));
    }

    private static void itemModels() {

    }
}

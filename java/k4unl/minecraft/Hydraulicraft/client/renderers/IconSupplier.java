package k4unl.minecraft.Hydraulicraft.client.renderers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

public class IconSupplier {
    
    public static IIcon tankGrid;
    
    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {
    
        if (event.map.getTextureType() == 0) {
	        tankGrid = event.map.registerIcon(ModInfo.ID + ":tankGrid");
        }
    }
}

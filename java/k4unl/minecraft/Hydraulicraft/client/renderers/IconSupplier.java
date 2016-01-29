package k4unl.minecraft.Hydraulicraft.client.renderers;

import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class IconSupplier {
    
    public static TextureAtlasSprite tankGrid;
    
    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {

        tankGrid = event.map.registerSprite(new ResourceLocation(ModInfo.ID + ":blocks/tankGrid"));
    }
}

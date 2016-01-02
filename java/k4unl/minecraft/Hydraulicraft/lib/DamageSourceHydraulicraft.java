package k4unl.minecraft.Hydraulicraft.lib;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

/**
 * @author Koen Beckers (K-4U)
 * Code copied from Pneumaticcraft, modified for use in Hydraulicraft
 */
public class DamageSourceHydraulicraft extends DamageSource {
    public static final DamageSourceHydraulicraft pressure = (DamageSourceHydraulicraft) new DamageSourceHydraulicraft("pressure", 3).setDamageBypassesArmor();
    public static final DamageSourceHydraulicraft noFluid = (DamageSourceHydraulicraft) new DamageSourceHydraulicraft("noFluid").setDamageBypassesArmor();

    private int deathMessages = 1;
    public DamageSourceHydraulicraft(String damageType) {

        super(damageType);
    }

    public DamageSourceHydraulicraft(String damageType, int dMessages) {
        super(damageType);
        deathMessages = dMessages;
    }

    /**
     * Returns the message to be displayed on player death.
     */
    @Override
    public IChatComponent func_151519_b(EntityLivingBase par1EntityLivingBase){
        String messageMeta = "";
        int messageNumber = par1EntityLivingBase.getRNG().nextInt(deathMessages) + 1;
        messageMeta = messageNumber + "";

        EntityLivingBase entitylivingbase1 = par1EntityLivingBase.func_94060_bK();
        String s = "hydcraft:death.attack." + damageType + messageMeta;
        String s1 = s + ".player";
        return entitylivingbase1 != null && StatCollector.canTranslate(s1) ? new ChatComponentTranslation(s1, new Object[]{par1EntityLivingBase.func_145748_c_(), entitylivingbase1.func_145748_c_()}) : new ChatComponentTranslation(s, new Object[]{par1EntityLivingBase.func_145748_c_()});
    }
}

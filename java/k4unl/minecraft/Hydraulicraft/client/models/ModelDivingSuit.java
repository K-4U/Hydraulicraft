package k4unl.minecraft.Hydraulicraft.client.models;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

/**
 * Diving Suit - Meadowcottage
 * Created using Tabula 5.0.0
 */
public class ModelDivingSuit extends ModelBiped {

    private static ModelDivingSuit modelHelm;
    private static ModelDivingSuit modelChest;
    private static ModelDivingSuit modelLegs;
    private static ModelDivingSuit modelBoots;
    public         ModelRenderer   HelmetLeftBack;
    public         ModelRenderer   HelmetRighBack;
    public         ModelRenderer   HelmetTop;
    public         ModelRenderer   HelmetBack;
    public         ModelRenderer   HelmetGlassFront;
    public         ModelRenderer   HelmetGlassRight;
    public         ModelRenderer   HelmetGlassLeft;
    public         ModelRenderer   TankBackPlate;
    public         ModelRenderer   TankController;
    public         ModelRenderer   TankFrontPlate;
    public         ModelRenderer   TankHelmetPipe;
    public         ModelRenderer   RightYellowTank;
    public         ModelRenderer   LeftYellowTank;
    public         ModelRenderer   RightTankPipe;
    public         ModelRenderer   LefttankPipe;
    public         ModelRenderer   FrontPanel;
    public         ModelRenderer   FrontGlow;
    public         ModelRenderer   FrontLeftBar;
    public         ModelRenderer   FrontRightBar;
    public         ModelRenderer   FrontBottomBar;
    public         ModelRenderer   BackRightShoulderBar;
    public         ModelRenderer   FrontRightShoulderBar;
    public         ModelRenderer   FrontLeftShoulderBar;
    public         ModelRenderer   FrontTopBar;
    public         ModelRenderer   FrontRightUpperArm;
    public         ModelRenderer   BackRightUpperArm;
    public         ModelRenderer   RightArmPanel;
    public         ModelRenderer   FrontRightLowerArm;
    public         ModelRenderer   BackRightLowerArm;
    public         ModelRenderer   BackLeftShoulderBar;
    public         ModelRenderer   FrontLeftUpperArm;
    public         ModelRenderer   BackLeftUpperArm;
    public         ModelRenderer   LeftArmPanel;
    public         ModelRenderer   FrontLeftLowerArm;
    public         ModelRenderer   BackLeftLowerArm;
    public         ModelRenderer   LeftHand;
    public         ModelRenderer   RightHand;
    public         ModelRenderer   BackBottomBar;
    public         ModelRenderer   BottomLeftLeg;
    public         ModelRenderer   BottomRightLeg;
    public         ModelRenderer   FrontLeftUpperLeg;
    public         ModelRenderer   BackLeftUpperLeg;
    public         ModelRenderer   LeftLegPanel;
    public         ModelRenderer   FrontLeftLowerLeg;
    public         ModelRenderer   BackLeftLowerLeg;
    public         ModelRenderer   FrontRightUpperLeg;
    public         ModelRenderer   BackRightUpperLeg;
    public         ModelRenderer   RightLegPanel;
    public         ModelRenderer   FrontRightLowerLeg;
    public         ModelRenderer   BackRightLowerLeg;
    public         ModelRenderer   RightFoot;
    public         ModelRenderer   LeftFoot;
    public         ModelRenderer   flipRightTop;
    public         ModelRenderer   flipLeftTop;
    public         ModelRenderer   flipRightBottom;
    public         ModelRenderer   flipLeftBottom;
    public         ModelRenderer   flipRightBack;
    public         ModelRenderer   flipLeftBack;

    public ModelRenderer flipperLeft;
    public ModelRenderer flipperRight;

    public ModelDivingSuit() {

        this.textureWidth = 128;
        this.textureHeight = 64;

        this.BackRightLowerArm = new ModelRenderer(this, 84, 2);
        this.BackRightLowerArm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.BackRightLowerArm.addBox(-4.0F, 6.0F, 1.0F, 1, 5, 1, 0.0F);
        this.TankHelmetPipe = new ModelRenderer(this, 24, 0);
        this.TankHelmetPipe.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.TankHelmetPipe.addBox(-1.5F, 0.0F, 3.0F, 3, 1, 2, 0.0F);
        this.LeftYellowTank = new ModelRenderer(this, 90, 9);
        this.LeftYellowTank.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.LeftYellowTank.addBox(3.25F, 2.5F, 3.0F, 2, 10, 2, 0.0F);
        this.LefttankPipe = new ModelRenderer(this, 4, 0);
        this.LefttankPipe.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.LefttankPipe.addBox(0.5F, 2.5F, 3.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(LefttankPipe, 0.0F, 0.0F, -0.7853981633974483F);
        this.BackLeftLowerLeg = new ModelRenderer(this, 18, 20);
        this.BackLeftLowerLeg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.BackLeftLowerLeg.addBox(2.1F, 7.0F, 0.9F, 1, 4, 1, 0.0F);
        this.FrontGlow = new ModelRenderer(this, 54, 0);
        this.FrontGlow.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.FrontGlow.addBox(-1.0F, 3.5F, -4.0F, 2, 2, 1, 0.0F);
        this.LeftLegPanel = new ModelRenderer(this, 0, 4);
        this.LeftLegPanel.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.LeftLegPanel.addBox(2.1F, 5.0F, -1.1F, 1, 2, 2, 0.0F);
        this.RightYellowTank = new ModelRenderer(this, 22, 9);
        this.RightYellowTank.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightYellowTank.addBox(-5.25F, 2.5F, 3.0F, 2, 10, 2, 0.0F);
        this.BackLeftUpperArm = new ModelRenderer(this, 50, 14);
        this.BackLeftUpperArm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.BackLeftUpperArm.addBox(3.0F, -2.0F, 1.0F, 1, 5, 1, 0.0F);
        this.BottomLeftLeg = new ModelRenderer(this, 44, 19);
        this.BottomLeftLeg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.BottomLeftLeg.addBox(-5.0F, 12.0F, -2.0F, 1, 1, 4, 0.0F);
        this.BackBottomBar = new ModelRenderer(this, 30, 19);
        this.BackBottomBar.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.BackBottomBar.addBox(-4.0F, 12.0F, 2.0F, 8, 1, 1, 0.0F);
        this.RightLegPanel = new ModelRenderer(this, 110, 4);
        this.RightLegPanel.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightLegPanel.addBox(-3.1F, 5.0F, -1.1F, 1, 2, 2, 0.0F);
        this.TankBackPlate = new ModelRenderer(this, 12, 9);
        this.TankBackPlate.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.TankBackPlate.addBox(-2.0F, 1.0F, 5.5F, 4, 10, 1, 0.0F);
        this.RightFoot = new ModelRenderer(this, 54, 3);
        this.RightFoot.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightFoot.addBox(-3.1F, 11.0F, -1.1F, 1, 1, 2, 0.0F);
        this.HelmetBack = new ModelRenderer(this, 92, 0);
        this.HelmetBack.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HelmetBack.addBox(-4.0F, -8.0F, 4.0F, 8, 8, 1, 0.0F);
        this.BackLeftLowerArm = new ModelRenderer(this, 106, 16);
        this.BackLeftLowerArm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.BackLeftLowerArm.addBox(3.0F, 6.0F, 1.0F, 1, 5, 1, 0.0F);
        this.setRotateAngle(BackLeftLowerArm, 0.0F, 0.0F, -0.03385938748868999F);
        this.BackRightLowerLeg = new ModelRenderer(this, 34, 21);
        this.BackRightLowerLeg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.BackRightLowerLeg.addBox(-3.1F, 7.0F, 0.9F, 1, 4, 1, 0.0F);
        this.FrontRightUpperArm = new ModelRenderer(this, 34, 0);
        this.FrontRightUpperArm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.FrontRightUpperArm.addBox(-4.0F, -2.0F, -2.0F, 1, 5, 1, 0.0F);
        this.HelmetGlassRight = new ModelRenderer(this, 0, 9);
        this.HelmetGlassRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HelmetGlassRight.addBox(-5.0F, -8.0F, -4.0F, 1, 8, 2, 0.0F);
        this.FrontLeftBar = new ModelRenderer(this, 98, 9);
        this.FrontLeftBar.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.FrontLeftBar.addBox(-1.5F, 1.0F, -3.0F, 1, 11, 1, 0.0F);
        this.TankController = new ModelRenderer(this, 60, 9);
        this.TankController.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.TankController.addBox(-3.0F, 0.5F, 2.5F, 6, 12, 3, 0.0F);
        this.LeftHand = new ModelRenderer(this, 110, 17);
        this.LeftHand.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.LeftHand.addBox(1.0F, 10.0F, -2.0F, 2, 1, 4, 0.0F);
        this.BackLeftShoulderBar = new ModelRenderer(this, 110, 2);
        this.BackLeftShoulderBar.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.BackLeftShoulderBar.addBox(0.0F, -3.0F, 1.0F, 3, 1, 1, 0.0F);
        this.FrontLeftUpperLeg = new ModelRenderer(this, 24, 3);
        this.FrontLeftUpperLeg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.FrontLeftUpperLeg.addBox(2.1F, 1.0F, -2.1F, 1, 4, 1, 0.0F);
        this.FrontRightShoulderBar = new ModelRenderer(this, 84, 0);
        this.FrontRightShoulderBar.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.FrontRightShoulderBar.addBox(-3.0F, -3.0F, -2.0F, 3, 1, 1, 0.0F);
        this.HelmetGlassFront = new ModelRenderer(this, 109, 8);
        this.HelmetGlassFront.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HelmetGlassFront.addBox(-4.0F, -8.0F, -5.0F, 8, 8, 1, 0.0F);
        this.HelmetGlassLeft = new ModelRenderer(this, 6, 9);
        this.HelmetGlassLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HelmetGlassLeft.addBox(4.0F, -8.0F, -4.0F, 1, 8, 2, 0.0F);
        this.RightArmPanel = new ModelRenderer(this, 60, 2);
        this.RightArmPanel.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightArmPanel.addBox(-4.0F, 3.0F, -1.5F, 1, 3, 3, 0.0F);
        this.FrontTopBar = new ModelRenderer(this, 30, 16);
        this.FrontTopBar.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.FrontTopBar.addBox(-4.0F, 0.0F, -4.0F, 8, 1, 2, 0.0F);
        this.BackRightUpperArm = new ModelRenderer(this, 118, 0);
        this.BackRightUpperArm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.BackRightUpperArm.addBox(-4.0F, -2.0F, 1.0F, 1, 5, 1, 0.0F);
        this.FrontPanel = new ModelRenderer(this, 40, 0);
        this.FrontPanel.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.FrontPanel.addBox(-2.0F, 2.5F, -3.5F, 4, 4, 1, 0.0F);
        this.BackRightShoulderBar = new ModelRenderer(this, 60, 0);
        this.BackRightShoulderBar.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.BackRightShoulderBar.addBox(-3.0F, -3.0F, 1.0F, 3, 1, 1, 0.0F);
        this.FrontLeftShoulderBar = new ModelRenderer(this, 110, 0);
        this.FrontLeftShoulderBar.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.FrontLeftShoulderBar.addBox(0.0F, -3.0F, -2.0F, 3, 1, 1, 0.0F);
        this.FrontRightBar = new ModelRenderer(this, 102, 9);
        this.FrontRightBar.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.FrontRightBar.addBox(0.5F, 1.0F, -3.0F, 1, 11, 1, 0.0F);
        this.FrontLeftUpperArm = new ModelRenderer(this, 88, 2);
        this.FrontLeftUpperArm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.FrontLeftUpperArm.addBox(3.0F, -2.0F, -2.0F, 1, 5, 1, 0.0F);
        this.FrontBottomBar = new ModelRenderer(this, 30, 14);
        this.FrontBottomBar.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.FrontBottomBar.addBox(-4.0F, 12.0F, -3.0F, 8, 1, 1, 0.0F);
        this.FrontRightLowerLeg = new ModelRenderer(this, 30, 21);
        this.FrontRightLowerLeg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.FrontRightLowerLeg.addBox(-3.1F, 7.0F, -2.1F, 1, 4, 1, 0.0F);
        this.BottomRightLeg = new ModelRenderer(this, 8, 20);
        this.BottomRightLeg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.BottomRightLeg.addBox(4.0F, 12.0F, -2.0F, 1, 1, 4, 0.0F);
        this.HelmetLeftBack = new ModelRenderer(this, 32, 0);
        this.HelmetLeftBack.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HelmetLeftBack.addBox(4.0F, -8.0F, -2.0F, 1, 8, 6, 0.0F);
        this.LeftFoot = new ModelRenderer(this, 38, 21);
        this.LeftFoot.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.LeftFoot.addBox(2.1F, 11.0F, -0.9F, 1, 1, 2, 0.0F);
        this.FrontLeftLowerArm = new ModelRenderer(this, 56, 14);
        this.FrontLeftLowerArm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.FrontLeftLowerArm.addBox(3.0F, 6.0F, -2.0F, 1, 5, 1, 0.0F);
        this.FrontRightLowerArm = new ModelRenderer(this, 122, 0);
        this.FrontRightLowerArm.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.FrontRightLowerArm.addBox(-4.0F, 6.0F, -2.0F, 1, 5, 1, 0.0F);
        this.FrontLeftLowerLeg = new ModelRenderer(this, 122, 17);
        this.FrontLeftLowerLeg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.FrontLeftLowerLeg.addBox(2.1F, 7.0F, -2.1F, 1, 4, 1, 0.0F);
        this.TankFrontPlate = new ModelRenderer(this, 78, 9);
        this.TankFrontPlate.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.TankFrontPlate.addBox(-2.5F, 1.0F, 2.0F, 5, 11, 1, 0.0F);
        this.HelmetRighBack = new ModelRenderer(this, 46, 0);
        this.HelmetRighBack.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HelmetRighBack.addBox(-5.0F, -8.0F, -2.0F, 1, 8, 6, 0.0F);
        this.RightHand = new ModelRenderer(this, 0, 19);
        this.RightHand.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightHand.addBox(-3.0F, 10.0F, -2.0F, 2, 1, 4, 0.0F);
        this.FrontRightUpperLeg = new ModelRenderer(this, 22, 21);
        this.FrontRightUpperLeg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.FrontRightUpperLeg.addBox(-3.1F, 1.0F, -2.1F, 1, 4, 1, 0.0F);
        this.HelmetTop = new ModelRenderer(this, 60, 0);
        this.HelmetTop.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.HelmetTop.addBox(-4.0F, -8.9F, -4.0F, 8, 1, 8, 0.0F);
        this.LeftArmPanel = new ModelRenderer(this, 51, 17);
        this.LeftArmPanel.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.LeftArmPanel.addBox(3.0F, 3.0F, -1.5F, 1, 3, 3, 0.0F);
        this.BackLeftUpperLeg = new ModelRenderer(this, 28, 3);
        this.BackLeftUpperLeg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.BackLeftUpperLeg.addBox(2.1F, 1.0F, 0.9F, 1, 4, 1, 0.0F);
        this.BackRightUpperLeg = new ModelRenderer(this, 26, 21);
        this.BackRightUpperLeg.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.BackRightUpperLeg.addBox(-3.1F, 1.0F, 0.9F, 1, 4, 1, 0.0F);
        this.RightTankPipe = new ModelRenderer(this, 0, 0);
        this.RightTankPipe.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.RightTankPipe.addBox(-1.5F, 2.5F, 3.5F, 1, 3, 1, 0.0F);
        this.setRotateAngle(RightTankPipe, 0.0F, 0.0F, 0.7853981633974483F);

        this.flipRightBottom = new ModelRenderer(this, 0, 0);
        this.flipRightBottom.setRotationPoint(-1.9F, 12.0F, 0.1F);
        this.flipRightBottom.addBox(-2.0F, 11.0F, -8.0F, 4, 1, 12, 0.0F);
        this.flipRightBack = new ModelRenderer(this, 0, 0);
        this.flipRightBack.setRotationPoint(-1.9F, 12.0F, 0.1F);
        this.flipRightBack.addBox(-1.5F, 10.5F, -1.0F, 3, 1, 3, 0.0F);
        this.setRotateAngle(flipRightBack, 0.17453292519943295F, 0.0F, 0.0F);
        this.flipLeftTop = new ModelRenderer(this, 0, 0);
        this.flipLeftTop.setRotationPoint(1.9F, 12.0F, 0.1F);
        this.flipLeftTop.addBox(-1.5F, 10.0F, -6.0F, 3, 1, 8, 0.0F);
        this.setRotateAngle(flipLeftTop, 0.17453292519943295F, 0.0F, 0.0F);
        this.flipLeftBottom = new ModelRenderer(this, 0, 0);
        this.flipLeftBottom.setRotationPoint(1.9F, 12.0F, 0.1F);
        this.flipLeftBottom.addBox(-2.0F, 11.0F, -8.0F, 4, 1, 12, 0.0F);
        this.flipLeftBack = new ModelRenderer(this, 0, 0);
        this.flipLeftBack.setRotationPoint(1.9F, 12.0F, 0.1F);
        this.flipLeftBack.addBox(-1.5F, 10.5F, -1.0F, 3, 1, 3, 0.0F);
        this.setRotateAngle(flipLeftBack, 0.17453292519943295F, 0.0F, 0.0F);
        this.flipRightTop = new ModelRenderer(this, 0, 0);
        this.flipRightTop.setRotationPoint(-1.9F, 12.0F, 0.1F);
        this.flipRightTop.addBox(-1.5F, 10.0F, -6.0F, 3, 1, 8, 0.0F);
        this.setRotateAngle(flipRightTop, 0.17453292519943295F, 0.0F, 0.0F);

        this.bipedHead.addChild(HelmetLeftBack);
        this.bipedHead.addChild(HelmetRighBack);
        this.bipedHead.addChild(HelmetTop);
        this.bipedHead.addChild(HelmetBack);
        this.bipedHead.addChild(HelmetGlassFront);
        this.bipedHead.addChild(HelmetGlassRight);
        this.bipedHead.addChild(HelmetGlassLeft);

        this.bipedBody.addChild(TankBackPlate);
        this.bipedBody.addChild(TankController);
        this.bipedBody.addChild(TankFrontPlate);
        this.bipedBody.addChild(TankHelmetPipe);
        this.bipedBody.addChild(RightYellowTank);
        this.bipedBody.addChild(LeftYellowTank);
        this.bipedBody.addChild(RightTankPipe);
        this.bipedBody.addChild(LefttankPipe);
        this.bipedBody.addChild(FrontPanel);
        this.bipedBody.addChild(FrontGlow);
        this.bipedBody.addChild(FrontLeftBar);
        this.bipedBody.addChild(FrontRightBar);
        this.bipedBody.addChild(FrontBottomBar);
        this.bipedBody.addChild(FrontTopBar);
        this.bipedBody.addChild(BackBottomBar);
        this.bipedBody.addChild(BottomRightLeg);
        this.bipedBody.addChild(BottomLeftLeg);

        this.bipedLeftArm.addChild(FrontLeftShoulderBar);
        this.bipedLeftArm.addChild(BackLeftShoulderBar);
        this.bipedLeftArm.addChild(FrontLeftUpperArm);
        this.bipedLeftArm.addChild(BackLeftUpperArm);
        this.bipedLeftArm.addChild(FrontLeftLowerArm);
        this.bipedLeftArm.addChild(BackLeftLowerArm);
        this.bipedLeftArm.addChild(LeftArmPanel);
        this.bipedLeftArm.addChild(LeftHand);

        this.bipedRightArm.addChild(FrontRightShoulderBar);
        this.bipedRightArm.addChild(BackRightShoulderBar);
        this.bipedRightArm.addChild(FrontRightUpperArm);
        this.bipedRightArm.addChild(BackRightUpperArm);
        this.bipedRightArm.addChild(FrontRightLowerArm);
        this.bipedRightArm.addChild(BackRightLowerArm);
        this.bipedRightArm.addChild(RightArmPanel);
        this.bipedRightArm.addChild(RightHand);

        this.bipedLeftLeg.addChild(FrontLeftUpperLeg);
        this.bipedLeftLeg.addChild(BackLeftUpperLeg);
        this.bipedLeftLeg.addChild(FrontLeftLowerLeg);
        this.bipedLeftLeg.addChild(BackLeftLowerLeg);
        this.bipedLeftLeg.addChild(LeftLegPanel);
        this.bipedLeftLeg.addChild(LeftFoot);

        this.bipedRightLeg.addChild(FrontRightUpperLeg);
        this.bipedRightLeg.addChild(BackRightUpperLeg);
        this.bipedRightLeg.addChild(FrontRightLowerLeg);
        this.bipedRightLeg.addChild(BackRightLowerLeg);
        this.bipedRightLeg.addChild(RightLegPanel);
        this.bipedRightLeg.addChild(RightFoot);

        flipperLeft = new ModelRenderer(this, 0, 0);
        flipperRight = new ModelRenderer(this, 0, 0);

        flipperLeft.addChild(flipLeftBack);
        flipperLeft.addChild(flipLeftBottom);
        flipperLeft.addChild(flipLeftTop);

        flipperRight.addChild(flipRightBack);
        flipperRight.addChild(flipRightBottom);
        flipperRight.addChild(flipRightTop);

        bipedLeftLeg.addChild(flipperLeft);
        bipedRightLeg.addChild(flipperRight);


    }

    public static ModelBiped getModel(EntityLivingBase entity, ItemStack stack) {
        if (stack == null || !(stack.getItem() instanceof ItemArmor))
            return null;
        int slot = ((ItemArmor) stack.getItem()).armorType;

        ModelDivingSuit armor;
        if (slot == 0 && modelHelm != null) {
            return modelHelm;
        } else if (slot == 1 && modelChest != null) {
            return modelChest;
        } else if (slot == 2 && modelLegs != null) {
            return modelLegs;
        } else if (slot == 3 && modelBoots != null) {
            return modelBoots;
        }

        armor = new ModelDivingSuit();
        armor.bipedBody.isHidden = true;
        armor.bipedLeftArm.isHidden = true;
        armor.bipedRightArm.isHidden = true;

        armor.bipedHead.isHidden = true;

        armor.bipedLeftLeg.isHidden = true;
        armor.bipedRightLeg.isHidden = true;

        armor.flipperRight.isHidden = true;
        armor.flipperLeft.isHidden = true;

        switch (slot) {
            case 0:
                armor.bipedHead.isHidden = false;
                modelHelm = armor;
                break;

            case 1:
                armor.bipedBody.isHidden = false;
                armor.bipedLeftArm.isHidden = false;
                armor.bipedRightArm.isHidden = false;
                modelChest = armor;
                break;

            case 2:
                armor.bipedLeftLeg.isHidden = false;
                armor.bipedRightLeg.isHidden = false;
                modelLegs = armor;
                break;

            case 3:
                armor.flipperLeft.isHidden = false;
                armor.flipperRight.isHidden = false;
                modelBoots = armor;
                break;

        }
        return armor;
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        /*GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        super.render(entity, f, f1, f2, f3, f4, f5);
        GL11.glDisable(GL11.GL_BLEND);
        return;*/

        this.isSneak = entity.isSneaking();
        this.isRiding = entity.isRiding();
        if (entity instanceof EntityLivingBase) {
            this.isChild = ((EntityLivingBase) entity).isChild();
            this.heldItemRight = (((EntityLivingBase) entity).getHeldItem() != null ? 1 : 0);
            if (entity instanceof EntityPlayer && ((EntityPlayer) entity).getItemInUse() != null)
                this.aimedBow = ((EntityPlayer) entity).getItemInUse().getItemUseAction() == EnumAction.BOW && ((EntityPlayer) entity).getItemInUseDuration() > 0;
        }

        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        if (((entity instanceof EntitySkeleton)) || ((entity instanceof EntityZombie))) {
            float f6 = (float) Math.sin(this.swingProgress * 3.141593F);
            float f7 = (float) Math.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * 3.141593F);
            this.bipedRightArm.rotateAngleZ = 0.0F;
            this.bipedLeftArm.rotateAngleZ = 0.0F;
            this.bipedRightArm.rotateAngleY = (-(0.1F - f6 * 0.6F));
            this.bipedLeftArm.rotateAngleY = (0.1F - f6 * 0.6F);
            this.bipedRightArm.rotateAngleX = -1.570796F;
            this.bipedLeftArm.rotateAngleX = -1.570796F;
            this.bipedRightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
            this.bipedLeftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
            this.bipedRightArm.rotateAngleZ += Math.cos(f2 * 0.09F) * 0.05F + 0.05F;
            this.bipedLeftArm.rotateAngleZ -= Math.cos(f2 * 0.09F) * 0.05F + 0.05F;
            this.bipedRightArm.rotateAngleX += Math.sin(f2 * 0.067F) * 0.05F;
            this.bipedLeftArm.rotateAngleX -= Math.sin(f2 * 0.067F) * 0.05F;
        }

        if (this.isChild) {
            float f6 = 2.0F;
            GL11.glPushMatrix();
            GL11.glScalef(1.5F / f6, 1.5F / f6, 1.5F / f6);
            GL11.glTranslatef(0.0F, 16.0F * f5, 0.0F);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_BLEND);
            this.bipedHead.render(f5);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
            GL11.glTranslatef(0.0F, 24.0F * f5, 0.0F);
            this.bipedBody.render(f5);
            this.bipedRightArm.render(f5);
            this.bipedLeftArm.render(f5);
            this.bipedRightLeg.render(f5);
            this.bipedLeftLeg.render(f5);
            GL11.glPopMatrix();
        } else {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_BLEND);
            this.bipedHead.render(f5);
            GL11.glDisable(GL11.GL_BLEND);
            this.bipedBody.render(f5);
            this.bipedRightArm.render(f5);
            this.bipedLeftArm.render(f5);
            this.bipedRightLeg.render(f5);
            this.bipedLeftLeg.render(f5);
            flipperLeft.rotateAngleX = bipedLeftLeg.rotateAngleX / 2;
            flipperLeft.rotationPointY = -Math.abs((float) Math.sin(bipedLeftLeg.rotateAngleX) * 4);
            flipperLeft.render(f5);
            flipperRight.rotateAngleX = bipedRightLeg.rotateAngleX / 2;
            flipperRight.rotationPointY = -Math.abs((float) Math.sin(bipedRightLeg.rotateAngleX) * 4);
            flipperRight.render(f5);
        }
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {

        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}

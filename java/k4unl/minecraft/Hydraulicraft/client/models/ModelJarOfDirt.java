package k4unl.minecraft.Hydraulicraft.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelJarOfDirt extends ModelBase
{

    ModelRenderer tapa1;
    ModelRenderer tapa2;
    ModelRenderer tapa3;
    ModelRenderer tapa4;
    ModelRenderer tapa5;
    ModelRenderer tapa6;
    ModelRenderer tapa7;
    ModelRenderer tapa8;
    ModelRenderer tapa9;
    ModelRenderer tapa10;
    ModelRenderer Dirt1;
    ModelRenderer Dirt2;
    ModelRenderer Dirt3;
    ModelRenderer Dirt4;
    ModelRenderer Dirt5;
    ModelRenderer Dirt6;
    ModelRenderer Dirt7;
    ModelRenderer Jar1;
    ModelRenderer Jar2;
    ModelRenderer Jar3;
    ModelRenderer Jar4;
    ModelRenderer Jar5;
    ModelRenderer Jar6;
    ModelRenderer Jar7;
    ModelRenderer Jar8;
    ModelRenderer Jar19;
    ModelRenderer Jar9;
    ModelRenderer Jar10;
    ModelRenderer Jar11;
    ModelRenderer Jar12;
    ModelRenderer Jar13;
    ModelRenderer Jar14;
    ModelRenderer Jar15;
    ModelRenderer Jar16;
    ModelRenderer Jar17;
    ModelRenderer Jar18;
    ModelRenderer Jar20;
    ModelRenderer Jar21;
    ModelRenderer Jar22;
    ModelRenderer Jar23;
    ModelRenderer Jar24;
    ModelRenderer Jar25;
    ModelRenderer Jar26;
    ModelRenderer cuello1;
    ModelRenderer cuello2;
    ModelRenderer cuello4;
    ModelRenderer cuello5;
    ModelRenderer cuello3;
    ModelRenderer cuello6;
    ModelRenderer cuello7;
    ModelRenderer cuello8;
    ModelRenderer cuello9;
    ModelRenderer cuello10;
    ModelRenderer cuello11;

    public ModelJarOfDirt() {

        this(0.0f);
    }

    public ModelJarOfDirt(float par1) {

        tapa1 = new ModelRenderer(this, 93, 1);
        tapa1.setTextureSize(128, 64 );
        tapa1.addBox( -0.5F, -7F, -7F, 1, 14, 14);
        tapa1.setRotationPoint( 0F, -10.5F, 0F );
        tapa2 = new ModelRenderer( this, 104, 15 );
        tapa2.setTextureSize( 128, 64 );
        tapa2.addBox( -1F, -6F, -0.5F, 2, 12, 1);
        tapa2.setRotationPoint( 6.000001F, -5.999996F, -2.652408E-06F );
        tapa3 = new ModelRenderer( this, 104, 15 );
        tapa3.setTextureSize( 128, 64 );
        tapa3.addBox( -1F, -6F, -0.5F, 2, 12, 1);
        tapa3.setRotationPoint( -5.999999F, -5.999992F, -4.08292E-06F );
        tapa4 = new ModelRenderer( this, 104, 15 );
        tapa4.setTextureSize( 128, 64 );
        tapa4.addBox( -1F, -6.5F, -0.5F, 2, 13, 1);
        tapa4.setRotationPoint( 1.78814E-06F, -5.999989F, -6F );
        tapa5 = new ModelRenderer( this, 104, 15 );
        tapa5.setTextureSize( 128, 64 );
        tapa5.addBox( -1F, -6.5F, -0.5F, 2, 13, 1);
        tapa5.setRotationPoint( -8.195623E-07F, -5.999985F, 5.999999F );
        tapa6 = new ModelRenderer( this, 106, 21 );
        tapa6.setTextureSize( 128, 64 );
        tapa6.addBox( -1F, -0.5F, -1.5F, 2, 1, 3);
        tapa6.setRotationPoint( 2.205372E-06F, -6F, -8F );
        tapa7 = new ModelRenderer( this, 95, 20 );
        tapa7.setTextureSize( 128, 64 );
        tapa7.addBox( -3.5F, -1F, -0.5F, 7, 2, 1);
        tapa7.setRotationPoint( 1.901388E-06F, -7.5F, -7.4F );
        tapa8 = new ModelRenderer( this, 105, 20 );
        tapa8.setTextureSize( 128, 64 );
        tapa8.addBox( -0.5F, -1F, -2F, 1, 2, 4);
        tapa8.setRotationPoint( 1.087785E-06F, -11F, -5.5F );
        tapa9 = new ModelRenderer( this, 104, 19 );
        tapa9.setTextureSize( 128, 64 );
        tapa9.addBox( -0.5F, -1F, -2.5F, 1, 2, 5);
        tapa9.setRotationPoint( -1.102686E-06F, -11F, 5F );
        tapa10 = new ModelRenderer( this, 108, 23 );
        tapa10.setTextureSize( 128, 64 );
        tapa10.addBox( -3F, -1F, -0.5F, 6, 2, 1);
        tapa10.setRotationPoint( -1.16229E-06F, -8F, 6.999999F );
        Dirt1 = new ModelRenderer( this, 34, 33 );
        Dirt1.setTextureSize( 128, 64 );
        Dirt1.addBox( -10.5F, -8F, -7F, 21, 16, 14);
        Dirt1.setRotationPoint( -2.130864E-06F, 11.50001F, 1.162289E-06F );
        Dirt2 = new ModelRenderer( this, 45, 47 );
        Dirt2.setTextureSize( 128, 64 );
        Dirt2.addBox( -10.5F, -7F, -0.5F, 21, 14, 1);
        Dirt2.setRotationPoint( -7.390631E-07F, 11.50001F, -7.499999F );
        Dirt3 = new ModelRenderer( this, 45, 47 );
        Dirt3.setTextureSize( 128, 64 );
        Dirt3.addBox( -10.5F, -7F, -0.5F, 21, 14, 1);
        Dirt3.setRotationPoint( -3.212618E-06F, 11.50001F, 7.500001F );
        Dirt4 = new ModelRenderer( this, 36, 35 );
        Dirt4.setTextureSize( 128, 64 );
        Dirt4.addBox( -0.5F, -6F, -6F, 1, 12, 12);
        Dirt4.setRotationPoint( -4.334974E-06F, 0.5000095F, 1.320508E-06F );
        Dirt5 = new ModelRenderer( this, 38, 37 );
        Dirt5.setTextureSize( 128, 64 );
        Dirt5.addBox( -0.5F, -5F, -5F, 1, 10, 10);
        Dirt5.setRotationPoint( -4.533184E-06F, -0.4999905F, 1.210989E-06F );
        Dirt6 = new ModelRenderer( this, 41, 40 );
        Dirt6.setTextureSize( 128, 64 );
        Dirt6.addBox( -0.5F, -3.5F, -3.5F, 1, 7, 7);
        Dirt6.setRotationPoint( -4.791E-06F, -1.49999F, 3.008819E-06F );
        Dirt7 = new ModelRenderer( this, 43, 42 );
        Dirt7.setTextureSize( 128, 64 );
        Dirt7.addBox( -0.5F, -2.5F, -2.5F, 1, 5, 5);
        Dirt7.setRotationPoint( -4.989211E-06F, -2.49999F, 4.806648E-06F );
        Jar1 = new ModelRenderer( this, 0, 0 );
        Jar1.setTextureSize( 128, 64 );
        Jar1.addBox( -0.5F, -7F, -7F, 1, 14, 14);
        Jar1.setRotationPoint( 7.301588E-07F, 23.50001F, -2.652408E-06F );
        Jar2 = new ModelRenderer( this, 2, 0 );
        Jar2.setTextureSize( 128, 64 );
        Jar2.addBox( -0.5F, -8F, -8F, 1, 16, 16);
        Jar2.setRotationPoint( -5.89809E-08F, 22.50001F, -1.309769E-06F );
        Jar3 = new ModelRenderer( this, 2, 7 );
        Jar3.setTextureSize( 128, 64 );
        Jar3.addBox( -12.5F, -0.5F, -0.5F, 25, 1, 1);
        Jar3.setRotationPoint( -7.500002F, 9.500007F, 7.499998F );
        Jar4 = new ModelRenderer( this, 0, 6 );
        Jar4.setTextureSize( 128, 64 );
        Jar4.addBox( -11.5F, -6F, -0.5F, 23, 12, 1);
        Jar4.setRotationPoint( -3.08454E-06F, 9.500008F, 8.999998F );
        Jar5 = new ModelRenderer( this, 0, 11 );
        Jar5.setTextureSize( 128, 64 );
        Jar5.addBox( -12.5F, -0.5F, -0.5F, 25, 1, 1);
        Jar5.setRotationPoint( 7.499997F, 9.50001F, 7.5F );
        Jar6 = new ModelRenderer( this, 6, 13 );
        Jar6.setTextureSize( 128, 64 );
        Jar6.addBox( -12.5F, -0.5F, -0.5F, 25, 1, 1);
        Jar6.setRotationPoint( 6.499997F, 9.500009F, 8.5F );
        Jar7 = new ModelRenderer( this, 6, 7 );
        Jar7.setTextureSize( 128, 64 );
        Jar7.addBox( -12.5F, -0.5F, -0.5F, 25, 1, 1);
        Jar7.setRotationPoint( -6.500002F, 9.500009F, 8.499999F );
        Jar8 = new ModelRenderer( this, 14, 6 );
        Jar8.setTextureSize( 128, 64 );
        Jar8.addBox( -0.5F, -6F, -0.5F, 1, 12, 1);
        Jar8.setRotationPoint( -1.693657E-06F, 21.50001F, 8.499999F );
        Jar19 = new ModelRenderer( this, 14, 6 );
        Jar19.setTextureSize( 128, 64 );
        Jar19.addBox( -0.5F, -6F, -0.5F, 1, 12, 1);
        Jar19.setRotationPoint( -5.280086E-06F, -2.499992F, 8.5F );
        Jar9 = new ModelRenderer( this, 3, 3 );
        Jar9.setTextureSize( 128, 64 );
        Jar9.addBox( -12.5F, -0.5F, -0.5F, 25, 1, 1);
        Jar9.setRotationPoint( 7.5F, 9.50001F, -7.5F );
        Jar10 = new ModelRenderer( this, 4, 3 );
        Jar10.setTextureSize( 128, 64 );
        Jar10.addBox( -11.5F, -6F, -0.5F, 23, 12, 1);
        Jar10.setRotationPoint( 7.00355E-07F, 9.500013F, -9F );
        Jar11 = new ModelRenderer( this, 1, 6 );
        Jar11.setTextureSize( 128, 64 );
        Jar11.addBox( -12.5F, -0.5F, -0.5F, 25, 1, 1);
        Jar11.setRotationPoint( -7.5F, 9.500016F, -7.500002F );
        Jar12 = new ModelRenderer( this, 6, 11 );
        Jar12.setTextureSize( 128, 64 );
        Jar12.addBox( -12.5F, -0.5F, -0.5F, 25, 1, 1);
        Jar12.setRotationPoint( -6.5F, 9.500016F, -8.500002F );
        Jar13 = new ModelRenderer( this, 5, 3 );
        Jar13.setTextureSize( 128, 64 );
        Jar13.addBox( -12.5F, -0.5F, -0.5F, 25, 1, 1);
        Jar13.setRotationPoint( 6.5F, 9.500012F, -8.500001F );
        Jar14 = new ModelRenderer( this, 23, 1 );
        Jar14.setTextureSize( 128, 64 );
        Jar14.addBox( -0.5F, -6F, -0.5F, 1, 12, 1);
        Jar14.setRotationPoint( 4.269788E-06F, 21.50001F, -8.500001F );
        Jar15 = new ModelRenderer( this, 4, 5 );
        Jar15.setTextureSize( 128, 64 );
        Jar15.addBox( -11.5F, -6F, -0.5F, 23, 12, 1);
        Jar15.setRotationPoint( 8.999998F, 9.50001F, 5.662442E-07F );
        Jar16 = new ModelRenderer( this, 4, 6 );
        Jar16.setTextureSize( 128, 64 );
        Jar16.addBox( -12.5F, -0.5F, -0.5F, 25, 1, 1);
        Jar16.setRotationPoint( 8.500002F, 9.50001F, -6.499999F );
        Jar17 = new ModelRenderer( this, 4, 12 );
        Jar17.setTextureSize( 128, 64 );
        Jar17.addBox( -12.5F, -0.5F, -0.5F, 25, 1, 1);
        Jar17.setRotationPoint( 8.499998F, 9.50001F, 6.499999F );
        Jar18 = new ModelRenderer( this, 12, 2 );
        Jar18.setTextureSize( 128, 64 );
        Jar18.addBox( -0.5F, -6F, -0.5F, 1, 12, 1);
        Jar18.setRotationPoint( 8.5F, 21.50001F, -1.618336E-07F );
        Jar20 = new ModelRenderer( this, 12, 2 );
        Jar20.setTextureSize( 128, 64 );
        Jar20.addBox( -0.5F, -6F, -0.5F, 1, 12, 1);
        Jar20.setRotationPoint( 8.499996F, -2.49999F, 6.564886E-07F );
        Jar21 = new ModelRenderer( this, 23, 1 );
        Jar21.setTextureSize( 128, 64 );
        Jar21.addBox( -0.5F, -6F, -0.5F, 1, 12, 1);
        Jar21.setRotationPoint( -2.541253E-06F, -2.499987F, -8.5F );
        Jar22 = new ModelRenderer( this, 4, 5 );
        Jar22.setTextureSize( 128, 64 );
        Jar22.addBox( -11.5F, -6F, -0.5F, 23, 12, 1);
        Jar22.setRotationPoint( -9.000002F, 9.500014F, -5.066399E-07F );
        Jar23 = new ModelRenderer( this, 4, 6 );
        Jar23.setTextureSize( 128, 64 );
        Jar23.addBox( -12.5F, -0.5F, -0.5F, 25, 1, 1);
        Jar23.setRotationPoint( -8.500005F, 9.500014F, 6.499999F );
        Jar24 = new ModelRenderer( this, 4, 12 );
        Jar24.setTextureSize( 128, 64 );
        Jar24.addBox( -12.5F, -0.5F, -0.5F, 25, 1, 1);
        Jar24.setRotationPoint( -8.500003F, 9.500015F, -6.499998F );
        Jar25 = new ModelRenderer( this, 12, 2 );
        Jar25.setTextureSize( 128, 64 );
        Jar25.addBox( -0.5F, -6F, -0.5F, 1, 12, 1);
        Jar25.setRotationPoint( -8.499998F, 21.50001F, -1.252615E-06F );
        Jar26 = new ModelRenderer( this, 12, 2 );
        Jar26.setTextureSize( 128, 64 );
        Jar26.addBox( -0.5F, -6F, -0.5F, 1, 12, 1);
        Jar26.setRotationPoint( -8.500006F, -2.499985F, 8.175635E-07F );
        cuello1 = new ModelRenderer( this, 17, 15 );
        cuello1.setTextureSize( 128, 64 );
        cuello1.addBox( -0.5F, -1.5F, -7F, 1, 3, 14);
        cuello1.setRotationPoint( -6.500001F, -3.499992F, -1.656521E-06F );
        cuello2 = new ModelRenderer( this, 17, 15 );
        cuello2.setTextureSize( 128, 64 );
        cuello2.addBox( -0.5F, -1.5F, -7F, 1, 3, 14);
        cuello2.setRotationPoint( 6.499999F, -3.49999F, -1.306896E-07F );
        cuello4 = new ModelRenderer( this, 17, 15 );
        cuello4.setTextureSize( 128, 64 );
        cuello4.addBox( -0.5F, -0.5F, -7F, 1, 1, 14);
        cuello4.setRotationPoint( 6.407504E-07F, -3.499985F, -7.499998F );
        cuello5 = new ModelRenderer( this, 21, 19 );
        cuello5.setTextureSize( 128, 64 );
        cuello5.addBox( -0.5F, -1F, -5F, 1, 2, 10);
        cuello5.setRotationPoint( -2.199836E-07F, -3.499983F, -5.999997F );
        cuello3 = new ModelRenderer( this, 17, 15 );
        cuello3.setTextureSize( 128, 64 );
        cuello3.addBox( -0.5F, -0.5F, -7F, 1, 1, 14);
        cuello3.setRotationPoint( -2.548099E-06F, -3.499994F, 7.499999F );
        cuello6 = new ModelRenderer( this, 21, 19 );
        cuello6.setTextureSize( 128, 64 );
        cuello6.addBox( -0.5F, -1F, -5F, 1, 2, 10);
        cuello6.setRotationPoint( -1.866178E-06F, -3.499992F, 5.999998F );
        cuello7 = new ModelRenderer( this, 11, 5 );
        cuello7.setTextureSize( 128, 64 );
        cuello7.addBox( -2.5F, -0.5F, -5F, 5, 1, 10);
        cuello7.setRotationPoint( 5.499998F, -6.499992F, 1.699893E-06F );
        cuello8 = new ModelRenderer( this, 12, 6 );
        cuello8.setTextureSize( 128, 64 );
        cuello8.addBox( -2.5F, -0.5F, -5F, 5, 1, 10);
        cuello8.setRotationPoint( -5.500002F, -6.499989F, 3.039013E-06F );
        cuello9 = new ModelRenderer( this, 7, 5 );
        cuello9.setTextureSize( 128, 64 );
        cuello9.addBox( -2.5F, -0.5F, -6F, 5, 1, 12);
        cuello9.setRotationPoint( -9.400417E-07F, -6.499992F, -5.500001F );
        cuello10 = new ModelRenderer( this, 9, 4 );
        cuello10.setTextureSize( 128, 64 );
        cuello10.addBox( -2.5F, -0.5F, -6F, 5, 1, 12);
        cuello10.setRotationPoint( -2.57917E-06F, -6.499992F, 5.499999F );
        cuello11 = new ModelRenderer( this, 8, 3 );
        cuello11.setTextureSize( 128, 64 );
        cuello11.addBox( -0.5F, -6.5F, -6.5F, 1, 13, 13);
        cuello11.setRotationPoint( -2.565537E-06F, -9.499992F, -1.390838E-06F );
    }

   public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
   {
        tapa1.rotateAngleX = -2.980231E-08F;
        tapa1.rotateAngleY = -1.570796F;
        tapa1.rotateAngleZ = 1.570796F;
        tapa1.renderWithRotation(par7);

        tapa2.rotateAngleX = 2.980233E-08F;
        tapa2.rotateAngleY = -1.570796F;
        tapa2.rotateAngleZ = 1.570796F;
        tapa2.renderWithRotation(par7);

        tapa3.rotateAngleX = 2.980233E-08F;
        tapa3.rotateAngleY = -1.570796F;
        tapa3.rotateAngleZ = 1.570796F;
        tapa3.renderWithRotation(par7);

        tapa4.rotateAngleX = -8.940697E-08F;
        tapa4.rotateAngleY = -2.086162E-07F;
        tapa4.rotateAngleZ = 1.570796F;
        tapa4.renderWithRotation(par7);

        tapa5.rotateAngleX = -1.094214E-07F;
        tapa5.rotateAngleY = -2.086162E-07F;
        tapa5.rotateAngleZ = 1.570796F;
        tapa5.renderWithRotation(par7);

        tapa6.rotateAngleX = -1.094214E-07F;
        tapa6.rotateAngleY = -2.086162E-07F;
        tapa6.rotateAngleZ = 1.570796F;
        tapa6.renderWithRotation(par7);

        tapa7.rotateAngleX = 0.1277351F;
        tapa7.rotateAngleY = 3.141592F;
        tapa7.rotateAngleZ = -1.570796F;
        tapa7.renderWithRotation(par7);

        tapa8.rotateAngleX = -1.129642E-07F;
        tapa8.rotateAngleY = -2.086162E-07F;
        tapa8.rotateAngleZ = 1.570796F;
        tapa8.renderWithRotation(par7);

        tapa9.rotateAngleX = -1.129379E-07F;
        tapa9.rotateAngleY = -2.086162E-07F;
        tapa9.rotateAngleZ = 1.570796F;
        tapa9.renderWithRotation(par7);

        tapa10.rotateAngleX = -1.129642E-07F;
        tapa10.rotateAngleY = -2.086162E-07F;
        tapa10.rotateAngleZ = 1.570796F;
        tapa10.renderWithRotation(par7);

        Dirt1.rotateAngleX = -1.095192E-07F;
        Dirt1.rotateAngleY = 3.141592F;
        Dirt1.rotateAngleZ = 1.570796F;
        Dirt1.renderWithRotation(par7);

        Dirt2.rotateAngleX = -1.095192E-07F;
        Dirt2.rotateAngleY = 3.141592F;
        Dirt2.rotateAngleZ = 1.570796F;
        Dirt2.renderWithRotation(par7);

        Dirt3.rotateAngleX = -1.095192E-07F;
        Dirt3.rotateAngleY = 3.141592F;
        Dirt3.rotateAngleZ = 1.570796F;
        Dirt3.renderWithRotation(par7);

        Dirt4.rotateAngleX = -1.095192E-07F;
        Dirt4.rotateAngleY = 3.141592F;
        Dirt4.rotateAngleZ = 1.570796F;
        Dirt4.renderWithRotation(par7);

        Dirt5.rotateAngleX = -1.095192E-07F;
        Dirt5.rotateAngleY = 3.141592F;
        Dirt5.rotateAngleZ = 1.570796F;
        Dirt5.renderWithRotation(par7);

        Dirt6.rotateAngleX = -1.095192E-07F;
        Dirt6.rotateAngleY = 3.141592F;
        Dirt6.rotateAngleZ = 1.570796F;
        Dirt6.renderWithRotation(par7);

        Dirt7.rotateAngleX = -1.095192E-07F;
        Dirt7.rotateAngleY = 3.141592F;
        Dirt7.rotateAngleZ = 1.570796F;
        Dirt7.renderWithRotation(par7);

        Jar1.rotateAngleX = -2.029113E-08F;
        Jar1.rotateAngleY = 3.141592F;
        Jar1.rotateAngleZ = 1.570796F;
        Jar1.renderWithRotation(par7);

        Jar2.rotateAngleX = -2.029113E-08F;
        Jar2.rotateAngleY = 3.141592F;
        Jar2.rotateAngleZ = 1.570796F;
        Jar2.renderWithRotation(par7);

        Jar3.rotateAngleX = -2.029113E-08F;
        Jar3.rotateAngleY = 3.141592F;
        Jar3.rotateAngleZ = 1.570796F;
        Jar3.renderWithRotation(par7);

        Jar4.rotateAngleX = -2.029113E-08F;
        Jar4.rotateAngleY = 3.141592F;
        Jar4.rotateAngleZ = 1.570796F;
        Jar4.renderWithRotation(par7);

        Jar5.rotateAngleX = -2.029113E-08F;
        Jar5.rotateAngleY = 3.141592F;
        Jar5.rotateAngleZ = 1.570796F;
        Jar5.renderWithRotation(par7);

        Jar6.rotateAngleX = -2.029113E-08F;
        Jar6.rotateAngleY = 3.141592F;
        Jar6.rotateAngleZ = 1.570796F;
        Jar6.renderWithRotation(par7);

        Jar7.rotateAngleX = -2.029113E-08F;
        Jar7.rotateAngleY = 3.141592F;
        Jar7.rotateAngleZ = 1.570796F;
        Jar7.renderWithRotation(par7);

        Jar8.rotateAngleX = -2.029113E-08F;
        Jar8.rotateAngleY = 3.141592F;
        Jar8.rotateAngleZ = 1.570796F;
        Jar8.renderWithRotation(par7);

        Jar19.rotateAngleX = -2.029113E-08F;
        Jar19.rotateAngleY = 3.141592F;
        Jar19.rotateAngleZ = 1.570796F;
        Jar19.renderWithRotation(par7);

        Jar9.rotateAngleX = -6.713164E-08F;
        Jar9.rotateAngleY = -4.002888E-08F;
        Jar9.rotateAngleZ = 1.570796F;
        Jar9.renderWithRotation(par7);

        Jar10.rotateAngleX = -6.713164E-08F;
        Jar10.rotateAngleY = -4.002888E-08F;
        Jar10.rotateAngleZ = 1.570796F;
        Jar10.renderWithRotation(par7);

        Jar11.rotateAngleX = -6.713164E-08F;
        Jar11.rotateAngleY = -4.002888E-08F;
        Jar11.rotateAngleZ = 1.570796F;
        Jar11.renderWithRotation(par7);

        Jar12.rotateAngleX = -6.713164E-08F;
        Jar12.rotateAngleY = -4.002888E-08F;
        Jar12.rotateAngleZ = 1.570796F;
        Jar12.renderWithRotation(par7);

        Jar13.rotateAngleX = -6.713164E-08F;
        Jar13.rotateAngleY = -4.002888E-08F;
        Jar13.rotateAngleZ = 1.570796F;
        Jar13.renderWithRotation(par7);

        Jar14.rotateAngleX = -6.713164E-08F;
        Jar14.rotateAngleY = -4.002888E-08F;
        Jar14.rotateAngleZ = 1.570796F;
        Jar14.renderWithRotation(par7);

        Jar15.rotateAngleX = -1.490116E-07F;
        Jar15.rotateAngleY = -1.570796F;
        Jar15.rotateAngleZ = 1.570796F;
        Jar15.renderWithRotation(par7);

        Jar16.rotateAngleX = -8.940695E-08F;
        Jar16.rotateAngleY = -1.570797F;
        Jar16.rotateAngleZ = 1.570797F;
        Jar16.renderWithRotation(par7);

        Jar17.rotateAngleX = -8.940695E-08F;
        Jar17.rotateAngleY = -1.570797F;
        Jar17.rotateAngleZ = 1.570797F;
        Jar17.renderWithRotation(par7);

        Jar18.rotateAngleX = -8.940695E-08F;
        Jar18.rotateAngleY = -1.570797F;
        Jar18.rotateAngleZ = 1.570797F;
        Jar18.renderWithRotation(par7);

        Jar20.rotateAngleX = -8.940695E-08F;
        Jar20.rotateAngleY = -1.570797F;
        Jar20.rotateAngleZ = 1.570797F;
        Jar20.renderWithRotation(par7);

        Jar21.rotateAngleX = -6.713164E-08F;
        Jar21.rotateAngleY = -4.002888E-08F;
        Jar21.rotateAngleZ = 1.570796F;
        Jar21.renderWithRotation(par7);

        Jar22.rotateAngleX = 2.980232E-07F;
        Jar22.rotateAngleY = 1.570796F;
        Jar22.rotateAngleZ = 1.570796F;
        Jar22.renderWithRotation(par7);

        Jar23.rotateAngleX = 1.788139E-07F;
        Jar23.rotateAngleY = 1.570796F;
        Jar23.rotateAngleZ = 1.570796F;
        Jar23.renderWithRotation(par7);

        Jar24.rotateAngleX = 1.788139E-07F;
        Jar24.rotateAngleY = 1.570796F;
        Jar24.rotateAngleZ = 1.570796F;
        Jar24.renderWithRotation(par7);

        Jar25.rotateAngleX = 1.788139E-07F;
        Jar25.rotateAngleY = 1.570796F;
        Jar25.rotateAngleZ = 1.570796F;
        Jar25.renderWithRotation(par7);

        Jar26.rotateAngleX = 1.788139E-07F;
        Jar26.rotateAngleY = 1.570796F;
        Jar26.rotateAngleZ = 1.570796F;
        Jar26.renderWithRotation(par7);

        cuello1.rotateAngleX = -2.029113E-08F;
        cuello1.rotateAngleY = 3.141592F;
        cuello1.rotateAngleZ = 1.570796F;
        cuello1.renderWithRotation(par7);

        cuello2.rotateAngleX = -2.029113E-08F;
        cuello2.rotateAngleY = 3.141592F;
        cuello2.rotateAngleZ = 1.570796F;
        cuello2.renderWithRotation(par7);

        cuello4.rotateAngleX = -1.490116E-07F;
        cuello4.rotateAngleY = -1.570796F;
        cuello4.rotateAngleZ = 1.570796F;
        cuello4.renderWithRotation(par7);

        cuello5.rotateAngleX = -2.980232E-08F;
        cuello5.rotateAngleY = -1.570796F;
        cuello5.rotateAngleZ = 1.570796F;
        cuello5.renderWithRotation(par7);

        cuello3.rotateAngleX = 8.940697E-08F;
        cuello3.rotateAngleY = 1.570796F;
        cuello3.rotateAngleZ = 1.570796F;
        cuello3.renderWithRotation(par7);

        cuello6.rotateAngleX = -5.960464E-08F;
        cuello6.rotateAngleY = 1.570796F;
        cuello6.rotateAngleZ = 1.570796F;
        cuello6.renderWithRotation(par7);

        cuello7.rotateAngleX = -2.029113E-08F;
        cuello7.rotateAngleY = 3.141592F;
        cuello7.rotateAngleZ = 1.570796F;
        cuello7.renderWithRotation(par7);

        cuello8.rotateAngleX = -2.029113E-08F;
        cuello8.rotateAngleY = 3.141592F;
        cuello8.rotateAngleZ = 1.570796F;
        cuello8.renderWithRotation(par7);

        cuello9.rotateAngleX = -8.940696E-08F;
        cuello9.rotateAngleY = -1.570796F;
        cuello9.rotateAngleZ = 1.570796F;
        cuello9.renderWithRotation(par7);

        cuello10.rotateAngleX = -2.980233E-08F;
        cuello10.rotateAngleY = -1.570796F;
        cuello10.rotateAngleZ = 1.570796F;
        cuello10.renderWithRotation(par7);

        cuello11.rotateAngleX = -8.940697E-08F;
        cuello11.rotateAngleY = -1.570797F;
        cuello11.rotateAngleZ = 1.570796F;
        cuello11.renderWithRotation(par7);

    }

}

package k4unl.minecraft.Hydraulicraft.lib.config;

import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;

public class Names {

    public static final Name blockHydraulicMixer               = new Name("Mixer", "hydraulicMixer");
    public static final Name blockHydraulicFrictionIncinerator = new Name("Friction Incinerator", "hydraulicFrictionIncinerator");
    public static final Name blockHydraulicCrusher             = new Name("Crusher", "hydraulicCrusher");
    public static final Name blockHydraulicPressureGauge       = new Name("Pressure Gauge", "hydraulicPressureGauge");
    public static final Name blockHydraulicPressureValve       = new Name("Pressure Valve", "hydraulicPressureValve");
    public static final Name blockHydraulicPiston              = new Name("Hydraulic Piston (TODO)", "hydraulicPiston");
    public static final Name blockHydraulicWasher              = new Name("Washer", "hydraulicWasher");
    public static final Name blockHydraulicPneumaticCompressor = new Name("Pneumatic Compressor", "hydraulicPneumaticCompressor");
    public static final Name blockHydraulicPressureWall        = new Name("Wall", "hydraulicPressureWall");
    public static final Name blockHydraulicPressureGlass       = new Name("Glass", "hydraulicPressureGlass");
    public static final Name blockPressureDisposal             = new Name("Pressure Disposal", "pressureDisposal");
    public static final Name blockValve                        = new Name("Valve", "valve");
    public static final Name blockInterfaceValve               = new Name("Interface Valve", "interfaceValve");

    public static final Name blockHydraulicDynamo    = new Name("Hydraulic Dynamo", "hydraulicDynamo");
    public static final Name blockHydraulicGenerator = new Name("Hydraulic Generator", "hydraulicGenerator");
    public static final Name blockHydraulicSaw       = new Name("Hydraulic Saw", "hydraulicSaw");
    public static final Name blockHydraulicFluidPump = new Name("Fluid Pump", "hydraulicFluidPump");

    public static final Name blockHydraulicCharger = new Name("Hydraulic charger", "hydraulicCharger");
    public static final Name blockChunkLoader      = new Name("Chunk Loader", "chunkLoader");

    public static final Name blockHydraulicAssembler = new Name("Hydraulic assembler", "hydraulicAssembler");

    public static final Name[] blockRFPump = { new Name("LP RF Pump", "LPRFPump"), new Name("MP RF Pump", "MPRFPump"),
      new Name("HP RF Pump", "HPRFPump") };

    public static final Name[] blockElectricPump = { new Name("LP MJ Pump", "LPElectricPump"), new Name("MP MJ Pump", "MPElectricPump"),
      new Name("HP MJ Pump", "HPElectricPump") };

    public static final Name blockHydraulicHarvester = new Name("Harvester", "hydraulicHarvesterSource");
    public static final Name blockHarvesterFrame = new Name("Harvester frame", "hydraulicHarvesterFrame");
	
	/*public static final Name[] blockHarvesterTrolley = {
		new Name("Wheat Harvester", "wheatHarvester"),
		new Name("Ender Lily Harvester", "enderLilyHarvester"),
		new Name("Sugar Cane Harvester", "sugarCaneHarvester")
	};*/

    public static final Name blockHarvesterTrolley = new Name("Harvester Trolley", "wheatHarvester");

    public static final Name[] blockCore = { new Name("LP Core", "LPBlockCore"), new Name("MP Core", "MPBlockCore"),
      new Name("HP Core", "HPBlockCore") };

    public static final Name[] partHose                  = { new Name("LP Hydraulic Pressure Pipe", "LPHydraulicPipe"),
      new Name("MP Hydraulic Pressure Pipe", "MPHydraulicPipe"), new Name("HP Hydraulic Pressure Pipe", "HPHydraulicPipe") };

    public static final Name[] blockHydraulicPressurevat = { new Name("LP Hydraulic Pressure Vat", "LPHydraulicPressureVat"),
      new Name("MP Hydraulic Pressure Vat", "MPHydraulicPressureVat"), new Name("HP Hydraulic Pressure Vat", "HPHydraulicPressureVat") };

    public static final Name[] blockHydraulicPump = { new Name("LP Hydraulic Pump", "LPHydraulicPump"),
      new Name("MP Hydraulic Pump", "MPHydraulicPump"), new Name("HP Hydraulic Pump", "HPHydraulicPump") };

    public static final Name[] blockHydraulicLavaPump = { new Name("LP Hydraulic Lava Pump", "LPHydraulicLavaPump"),
      new Name("MP Hydraulic Lava Pump", "MPHydraulicLavaPump"), new Name("HP Hydraulic Lava Pump", "HPHydraulicLavaPump") };

    public static final Name[] partValve = { new Name("LP Valve", "LPPartValve"), new Name("MP Valve", "MPPartValve"),
      new Name("HP Valve", "HPPartValve") };

    public static final Name partFluidPipe            = new Name("Fluid Pipe", "fluidPipe");
    public static final Name partFluidInterface = new Name("Fluid Interface", "fluidInterface");

    public static final Name portalBase       = new Name("Portal Base", "portalBase");
    public static final Name portalFrame      = new Name("Portal Frame", "portalFrame");
    public static final Name portalTeleporter = new Name("Portal Teleporter", "portalTeleporter");

    public static final Name itemIPCard = new Name("Portal IP Card", "portalIPCard");

    public static final Name oreCopper = new Name("Copper Ore", "oreCopper");
    public static final Name oreLead   = new Name("Lead Ore", "oreLead");

    public static final Name itemGasket          = new Name("Gasket", "gasket");
    public static final Name itemDebugger        = new Name("Debug", "itemDebug");
    public static final Name itemFrictionPlate   = new Name("Friction Plate", "frictionPlate");
    public static final Name ingotCopper         = new Name("Copper Ingot", "ingotCopper");
    public static final Name ingotLead           = new Name("Lead Ingot", "ingotLead");
    public static final Name ingotEnrichedCopper = new Name("Enriched Copper Ingot", "ingotEnrichedCopper");
    public static final Name itemChunk           = new Name("Chunks", "chunk");
    public static final Name itemDust            = new Name("Dusts", "dust");

    public static final Name itemBacon           = new Name("Bacon", "bacon");
    public static final Name itemEnderLolly      = new Name("Ender Lolly", "enderLolly");
    public static final Name itemDiamondShard    = new Name("Diamond Shard", "diamondShard");


    public static final Name blockInfiniteSource    = new Name("Infinite Source", "blockInfiniteSource");
    public static final Name blockMovingPane        = new Name("Moving pane", "movingPane");
    public static final Name itemMiningHelmet       = new Name("Mining helmet", "miningHelmet");
    public static final Name blockLight             = new Name("Light Block", "blockTransparentLight");
    public static final Name itemLamp               = new Name("Lamp", "itemLamp");
    public static final Name blockLead              = new Name("Lead Block", "blockLead");
    public static final Name blockCopper            = new Name("Copper Block", "blockCopper");
    public static final Name itemCopperEnrichedDust = new Name("Copper enriched dust", "dustDiamondCopper");

    public static final Name blockJarDirt = new Name("Jar of Dirt", "blockJarDirt");

    public static final Name fluidHydraulicOil      = new Name("Hydraulic Oil", "hydraulicOil");
    public static final Name fluidOil               = new Name("Oil", "oil");
    public static final Name fluidLubricant         = new Name("Lubricant", "lubricant");
    public static final Name itemBucketHydraulicOil = new Name("Hydraulic Oil Bucket", "bucketHydraulicOil");
    public static final Name itemBucketOil          = new Name("Oil Bucket", "bucketOil");
    public static final Name itemBucketLubricant    = new Name("Lubricant Bucket", "bucketLubricant");



}

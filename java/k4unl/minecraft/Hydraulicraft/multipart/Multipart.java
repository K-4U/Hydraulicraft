package k4unl.minecraft.Hydraulicraft.multipart;

public class Multipart /*implements IPartFactory, IPartConverter */{
	/*public static ItemPartHose itemPartHose;
	public static ItemPartValve itemPartValve;
	public static ItemPartFluidPipe itemPartFluidPipe;
	public static ItemPartFluidInterface itemPartFluidInterface;
*/
	public static void init() {
		/*MultiPartRegistry.registerParts(new Multipart(), new String[]{
				"tile." + Names.partHose[0].unlocalized,
				"tile." + Names.partHose[1].unlocalized,
				"tile." + Names.partHose[2].unlocalized,
				"tile." + Names.partValve[0].unlocalized,
				"tile." + Names.partValve[1].unlocalized,
				"tile." + Names.partValve[2].unlocalized,
				"tile." + Names.partFluidPipe.unlocalized,
				"tile." + Names.partFluidInterface.unlocalized
		});


		itemPartHose = new ItemPartHose();
		itemPartValve = new ItemPartValve();
		itemPartFluidPipe = new ItemPartFluidPipe();
		itemPartFluidInterface = new ItemPartFluidInterface();

		GameRegistry.registerItem(itemPartHose, Names.partHose[0].unlocalized);
		GameRegistry.registerItem(itemPartValve, Names.partValve[0].unlocalized);
		GameRegistry.registerItem(itemPartFluidPipe, Names.partFluidPipe.unlocalized);
		GameRegistry.registerItem(itemPartFluidInterface, Names.partFluidInterface.unlocalized);*/
	}
/*
	@Override
	public TMultiPart createPart(String id, boolean client) {
		if (id.equals("tile." + Names.partHose[0].unlocalized) || id.equals("tile." + Names.partHose[1].unlocalized) || id.equals("tile." + Names.partHose[2].unlocalized)) {
			return new PartHose();
		} else if (id.equals("tile." + Names.partValve[0].unlocalized) || id.equals("tile." + Names.partValve[1].unlocalized) || id.equals("tile." + Names.partValve[2].unlocalized)) {
			return new PartValve();
		} else if (id.equals("tile." + Names.partFluidPipe.unlocalized)) {
			return new PartFluidPipe();
		} else if (id.equals("tile." + Names.partFluidInterface.unlocalized)) {
			return new PartFluidInterface();
		}
		return null;
	}


	public static TileMultipart getMultipartTile(IBlockAccess access, Location pos) {
		TileEntity te = access.getTileEntity(pos.getX(), pos.getY(), pos.getZ());
		return te instanceof TileMultipart ? (TileMultipart) te : null;
	}

	public static TMultiPart getMultiPart(IBlockAccess w, Location bc, int part) {
		TileMultipart t = getMultipartTile(w, bc);
		if (t != null)
			return t.partMap(part);

		return null;
	}

	public static boolean hasTransporter(TileMultipart mp) {
		boolean ret = false;
		List<TMultiPart> t = mp.jPartList();
		for (TMultiPart p : t) {
			if (!ret) {
				if (p instanceof IHydraulicTransporter) {
					ret = true;
				}
			}
		}
		return ret;
	}

	public static boolean hasPartHose(TileMultipart mp) {
		boolean ret = false;
		List<TMultiPart> t = mp.jPartList();
		for (TMultiPart p : t) {
			if (!ret) {
				if (p instanceof PartHose) {
					ret = true;
				}
			}
		}
		return ret;
	}

	public static PartHose getHose(TileMultipart mp) {
		List<TMultiPart> t = mp.jPartList();
		for (TMultiPart p : t) {
			if (p instanceof PartHose) {
                return (PartHose) p;
            }
		}
		return null;
	}

	public static boolean hasPartValve(TileMultipart mp) {
		boolean ret = false;
		List<TMultiPart> t = mp.jPartList();
		for (TMultiPart p : t) {
			if (!ret) {
				if (p instanceof PartValve) {
					ret = true;
				}
			}
		}
		return ret;
	}

	public static PartValve getValve(TileMultipart mp) {
		List<TMultiPart> t = mp.jPartList();
		for (TMultiPart p : t) {
			if (p instanceof PartValve) {
                return (PartValve) p;
            }
		}
		return null;
	}


	public static IHydraulicTransporter getTransporter(TileMultipart mp) {
		List<TMultiPart> t = mp.jPartList();
		for (TMultiPart p : t) {
			if (p instanceof IHydraulicTransporter) {
                return (IHydraulicTransporter) p;
            }
		}
		return null;
	}

	public static boolean hasPartFluidHandler(TileMultipart mp) {
		List<TMultiPart> t = mp.jPartList();
		for (TMultiPart p : t) {
			if (p instanceof IFluidHandler) {
				return true;
			}
		}
		return false;
	}

	public static IFluidHandler getFluidHandler(TileMultipart mp) {
		List<TMultiPart> t = mp.jPartList();
		for (TMultiPart p : t) {
			if (p instanceof IFluidHandler) {
				return (IFluidHandler) p;
			}
		}
		return null;
	}

	public static boolean hasPartFluidPipe(TileMultipart mp) {
		List<TMultiPart> t = mp.jPartList();
		for (TMultiPart p : t) {
			if (p instanceof PartFluidPipe) {
				return true;
			}
		}
		return false;
	}

	public static PartFluidPipe getFluidPipe(TileMultipart mp) {
		List<TMultiPart> t = mp.jPartList();
		for (TMultiPart p : t) {
			if (p instanceof PartFluidPipe) {
				return (PartFluidPipe) p;
			}
		}
		return null;
	}

	@Override
	public Iterable<Block> blockTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TMultiPart convert(World arg0, BlockCoord arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void updateMultiPart(TMultiPart tMp) {
		MCDataOutput writeStream = tMp.tile().getWriteStream(tMp);
		tMp.writeDesc(writeStream);
	}
*/
}

package pet.minecraft.Hydraulicraft.lib;

import java.util.List;

public class Functions {

	public static List mergeList(List l1, List l2){
		for (Object object : l1) {
			if(!l2.contains(object)){
				l2.add(object);
			}
		}
		return l2;
	}
}

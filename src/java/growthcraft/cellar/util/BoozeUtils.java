package growthcraft.cellar.util;

import java.util.List;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.booze.BoozeEffect;
import growthcraft.api.cellar.booze.BoozeTag;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BoozeUtils
{
	private BoozeUtils() {}

	public static boolean isFermentedBooze(Fluid booze)
	{
		return CellarRegistry.instance().booze().hasTags(booze, BoozeTag.FERMENTED);
	}

	public static void addEffects(Fluid booze, ItemStack stack, World world, EntityPlayer player)
	{
		if (booze == null) return;

		final BoozeEffect effect = CellarRegistry.instance().booze().getEffect(booze);
		if (effect != null)
		{
			effect.apply(world, player, world.rand, null);
		}
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void addInformation(Fluid booze, ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		if (booze == null) return;
		final BoozeEffect effect = CellarRegistry.instance().booze().getEffect(booze);

		if (effect != null)
		{
			effect.getDescription((List<String>)list);
		}
	}

	public static boolean hasEffect(Fluid booze)
	{
		final BoozeEffect effect = CellarRegistry.instance().booze().getEffect(booze);
		if (effect != null)
		{
			return effect.isValid();
		}
		return false;
	}
}

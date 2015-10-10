package growthcraft.grapes.village;

import java.util.List;
import java.util.Random;

import growthcraft.grapes.GrowthCraftGrapes;
import growthcraft.grapes.village.ComponentVillageGrapeVineyard;

import cpw.mods.fml.common.registry.VillagerRegistry.IVillageCreationHandler;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;

public class VillageHandlerGrapes implements IVillageTradeHandler, IVillageCreationHandler
{
	@Override
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random)
	{
		//		recipeList.add(new MerchantRecipe(new ItemStack(GrowthCraftGrapes.grapes, 18 + random.nextInt(3)), new ItemStack(Item.emerald, 1)));
		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 1 + random.nextInt(2)), GrowthCraftGrapes.grapeWine.asStack(1, 1)));
		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 2 + random.nextInt(2)), GrowthCraftGrapes.grapeWine.asStack(1, 2)));
		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 2 + random.nextInt(2)), GrowthCraftGrapes.grapeWine.asStack(1, 3)));
	}

	@Override
	public PieceWeight getVillagePieceWeight(Random random, int i)
	{
		int num = MathHelper.getRandomIntegerInRange(random, 0 + i, 1 + i);
		if (!GrowthCraftGrapes.getConfig().generateGrapeVineyardStructure)
			num = 0;

		return new PieceWeight(ComponentVillageGrapeVineyard.class, 21, num);
	}

	@Override
	public Class<?> getComponentClass()
	{
		return ComponentVillageGrapeVineyard.class;
	}

	@Override
	public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int p1, int p2, int p3, int p4, int p5)
	{
		return ComponentVillageGrapeVineyard.buildComponent(startPiece, pieces, random, p1, p2, p3, p4, p5);
	}
}

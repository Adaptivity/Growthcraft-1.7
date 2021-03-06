/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.api.cellar.brewing;

import java.io.BufferedReader;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.common.Residue;
import growthcraft.api.core.schema.ItemKeySchema;
import growthcraft.api.core.schema.FluidStackSchema;
import growthcraft.api.core.schema.ResidueSchema;
import growthcraft.api.core.util.JsonConfigDef;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * This allows users to define new brewing recipes.
 */
public class UserBrewingRecipes extends JsonConfigDef
{
	public static class UserBrewingRecipeEntry
	{
		public ItemKeySchema item;
		public FluidStackSchema input_fluid;
		public FluidStackSchema output_fluid;
		public ResidueSchema residue;
		public int time;

		public String toString()
		{
			return "" + item + " + " + input_fluid + " @" + time + " = " + output_fluid + " + " + residue;
		}
	}

	private static final UserBrewingRecipeEntry[] DEFAULT_ENTRIES = {};
	private UserBrewingRecipeEntry[] recipes;

	@Override
	protected String getDefault()
	{
		return gson.toJson(DEFAULT_ENTRIES, UserBrewingRecipeEntry[].class);
	}

	@Override
	protected void loadFromBuffer(BufferedReader reader)
	{
		this.recipes = gson.fromJson(reader, UserBrewingRecipeEntry[].class);
	}

	private void addBrewingRecipe(UserBrewingRecipeEntry recipe)
	{
		if (recipe == null)
		{
			logger.error("Invalid recipe");
			return;
		}

		final ItemStack item = recipe.item.asStack();
		if (item == null)
		{
			logger.error("Invalid item for recipe %s", recipe);
			return;
		}

		if (recipe.input_fluid == null)
		{
			logger.error("Invalid input_fluid %s", recipe);
			return;
		}

		final FluidStack inputFluidStack = recipe.input_fluid.asFluidStack();
		if (inputFluidStack == null)
		{
			logger.error("Invalid input_fluid for recipe %s", recipe);
			return;
		}

		if (recipe.output_fluid == null)
		{
			logger.error("Invalid output_fluid %s", recipe);
			return;
		}

		final FluidStack outputFluidStack = recipe.output_fluid.asFluidStack();
		if (outputFluidStack == null)
		{
			logger.error("Invalid output_fluid for recipe %s", recipe);
			return;
		}

		Residue residue = null;
		if (recipe.residue == null)
		{
			logger.warn("No residue specified for %s", recipe);
			residue = Residue.newDefault(1.0f);
		}
		else
		{
			residue = recipe.residue.asResidue();
		}

		if (residue == null)
		{
			logger.error("Not a valid residue found for %s", recipe);
			return;
		}

		logger.info("Adding brewing recipe %s", recipe);
		CellarRegistry.instance().brewing().addBrewing(inputFluidStack, item, outputFluidStack, recipe.time, residue);
	}

	@Override
	public void postInit()
	{
		if (recipes != null)
		{
			logger.info("Adding %d brewing recipes.", recipes.length);
			for (UserBrewingRecipeEntry recipe : recipes) addBrewingRecipe(recipe);
		}
	}
}

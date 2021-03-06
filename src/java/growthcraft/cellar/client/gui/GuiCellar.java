package growthcraft.cellar.client.gui;

import java.util.List;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.common.tileentity.CellarTank;
import growthcraft.core.client.gui.GrcGuiContainer;
import growthcraft.core.util.UnitFormatter;
import growthcraft.cellar.common.tileentity.TileEntityCellarDevice;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

@SideOnly(Side.CLIENT)
public class GuiCellar extends GrcGuiContainer
{
	private TileEntityCellarDevice cellarDevice;

	public GuiCellar(Container container, TileEntityCellarDevice cd)
	{
		super(container, cd);
		this.cellarDevice = cd;
	}

	protected void drawTank(int w, int h, int wp, int hp, int width, int amount, FluidStack fluidstack, CellarTank _tank)
	{
		drawFluidStack(w, h, wp, hp, width, amount, fluidstack);
	}

	protected void addFluidTooltips(FluidStack fluid, List<String> tooltip)
	{
		if (fluid == null) return;
		if (fluid.amount <= 0) return;

		tooltip.add(fluid.getLocalizedName());

		final String s = UnitFormatter.fluidModifier(fluid.getFluid());
		if (s != null) tooltip.add(s);
	}

	protected void addFermentTooltips(FluidStack fluid, List<String> tooltip)
	{
		if (fluid == null) return;
		if (fluid.amount <= 0) return;

		if (CellarRegistry.instance().fermenting().canFerment(fluid))
		{
			addFluidTooltips(fluid, tooltip);
		}
		else
		{
			tooltip.add(fluid.getLocalizedName());
			tooltip.add("");
			tooltip.add(EnumChatFormatting.RED + I18n.format("gui.grc.cantferment"));
		}
	}
}

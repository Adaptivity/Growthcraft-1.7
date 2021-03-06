package growthcraft.cellar.client.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;

import growthcraft.cellar.client.gui.widget.GuiButtonDiscard;
import growthcraft.cellar.common.inventory.ContainerFermentJar;
import growthcraft.cellar.common.tileentity.TileEntityFermentJar;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.network.PacketClearTankButton;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiFermentJar extends GuiCellar
{
	protected static final ResourceLocation fermentJarResource = new ResourceLocation("grccellar" , "textures/guis/gui_ferment_jar.png");
	private TileEntityFermentJar te;
	private GuiButtonDiscard discardButton;

	public GuiFermentJar(InventoryPlayer inv, TileEntityFermentJar fermentJar)
	{
		super(new ContainerFermentJar(inv, fermentJar), fermentJar);
		this.te = fermentJar;
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void initGui()
	{
		super.initGui();
		this.discardButton = new GuiButtonDiscard(fermentJarResource, 1, guiLeft + 116, guiTop + 54);
		discardButton.enabled = false;
		buttonList.add(discardButton);

		addTooltipIndex("fluidtank0", 36, 17, 16, 52);
		addTooltipIndex("discardtank0", 16, 52, 16, 16);
	}

	@Override
	protected void actionPerformed(GuiButton butn)
	{
		GrowthCraftCellar.packetPipeline.sendToServer(new PacketClearTankButton(te.xCoord, te.yCoord, te.zCoord));
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
		discardButton.enabled = te.isFluidTankFilled(0);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		super.drawGuiContainerForegroundLayer(par1, par2);

		if (!te.isFluidTankEmpty(0))
		{
			final String s = String.valueOf(te.getFluidAmount(0));
			fontRendererObj.drawStringWithShadow(s, xSize - 124 - fontRendererObj.getStringWidth(s), ySize - 104, 0xFFFFFF);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(fermentJarResource);
		final int x = (width - xSize) / 2;
		final int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		final int progressWidth = te.getFermentProgressScaled(24);
		drawTexturedModalRect(x + 54, y + 35, 176, 0, progressWidth, 17);

		final int fluidScaled = te.getFluidAmountScaled(52, 0);
		if (fluidScaled > 0)
		{
			drawTank(x, y, 36, 17, 16, fluidScaled, te.getFluidStack(0), te.getFluidTank(0));
			mc.getTextureManager().bindTexture(fermentJarResource);
		}
	}

	@Override
	protected void addTooltips(String handle, List<String> tooltip)
	{
		switch (handle)
		{
			case "fluidtank0":
				addFluidTooltips(te.getFluidStack(0), tooltip);
				break;
			case "discardtank0":
				tooltip.add(I18n.format("gui.grc.discard"));
				break;
			default:
				break;
		}
	}
}

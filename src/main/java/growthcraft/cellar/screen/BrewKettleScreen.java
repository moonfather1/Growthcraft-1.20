package growthcraft.cellar.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import growthcraft.cellar.screen.container.BrewKettleMenu;
import growthcraft.cellar.shared.Reference;
import growthcraft.lib.kaupenjoe.screen.renderer.FluidTankRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;

import java.util.Optional;

public class BrewKettleScreen extends AbstractContainerScreen<BrewKettleMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(
            Reference.MODID, "textures/gui/brew_kettle_screen.png"
    );
    private static final Component MESSAGE_REMOVE_LID = Component.translatable("message.growthcraft_cellar.kettle.gui_lid_off").withStyle(Style.EMPTY.withColor(0xffcfccc5));
    private static final Component MESSAGE_PUT_LID = Component.translatable("message.growthcraft_cellar.kettle.gui_lid_on").withStyle(Style.EMPTY.withColor(0xffcfccc5));

    private FluidTankRenderer fluidTankRenderer0;
    private FluidTankRenderer fluidTankRenderer1;

    public BrewKettleScreen(BrewKettleMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        assignFluidRender();
    }

    private void assignFluidRender() {
        fluidTankRenderer0 = new FluidTankRenderer(4000, true, 16, 52);
        fluidTankRenderer1 = new FluidTankRenderer(4000, true, 16, 52);
    }

    @Override
    protected void renderBg(GuiGraphics poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // Full background image
        poseStack.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // lid on/off gui
        int xOffset = 186, yOffset = 112;
        if (this.menu.hasLid()) { yOffset = yOffset - 54 - 1; }
        if (isMouseAboveArea(mouseX, mouseY, this.leftPos + 10, this.topPos + 27, 13, 16)) { xOffset = xOffset + 34 + 1; }
        poseStack.blit(TEXTURE, x + 7, y + 16, xOffset, yOffset, 34, 54);

        // Progress bar
        poseStack.blit(TEXTURE,
                x + 98, y + 30,
                176, 0,
                9, menu.getProgressionScaled(28)
        );

        // Heat indicator
        if (this.menu.isHeated()) {
            poseStack.blit(TEXTURE,
                    x + 68, y + 53,
                    176, 28,
                    13, 13
            );
        }

        fluidTankRenderer0.render(poseStack.pose(),
                x + 46, y + 17,
                menu.getFluidStack(0));

        fluidTankRenderer1.render(poseStack.pose(),
                x + 114, y + 17,
                menu.getFluidStack(1));
    }

    @Override
    public void render(GuiGraphics poseStack, int mouseX, int mouseY, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);
        // Render any tooltips for this mouse over location.
        renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics poseStack, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // Screen Title
        poseStack.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        // Inventory Title
        poseStack.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);

        // FluidTank Tooltips
        renderFluidTankTooltips(poseStack, mouseX, mouseY, x, y);
    }


    private void renderFluidTankTooltips(GuiGraphics poseStack, int mouseX, int mouseY, int x, int y) {
        if (isMouseAboveArea(mouseX, mouseY, x + 46, y + 17, 16, 52)) {
            poseStack.renderTooltip(
                    this.font,
                    fluidTankRenderer0.getTooltip(menu.getFluidStack(0), TooltipFlag.Default.NORMAL),
                    Optional.empty(),
                    mouseX - x,
                    mouseY - y
            );
        } else if (isMouseAboveArea(mouseX, mouseY, x + 114, y + 17, 16, 52)) {
            poseStack.renderTooltip(
                    this.font,
                    fluidTankRenderer1.getTooltip(menu.getFluidStack(1), TooltipFlag.Default.NORMAL),
                    Optional.empty(),
                    mouseX - x,
                    mouseY - y
            );
        }
    }

    private boolean isMouseAboveArea(int mouseX, int mouseY, int baseX, int baseY, int areaSizeX, int areaSizeY) {
        return (mouseX >= baseX && mouseX <= (baseX + areaSizeX)) && (mouseY >= baseY && mouseY <= (baseY + areaSizeY));
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderTooltip(graphics, mouseX, mouseY);
        if (this.menu.getCarried().isEmpty() && isMouseAboveArea(mouseX, mouseY, this.leftPos + 10, this.topPos + 27, 13, 16)) {
            graphics.renderTooltip(this.font, this.menu.hasLid() ? MESSAGE_REMOVE_LID : MESSAGE_PUT_LID, this.leftPos + 25, mouseY + 8);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseKey) {
        if (this.menu.getCarried().isEmpty() && isMouseAboveArea((int) mouseX, (int) mouseY, this.leftPos + 10, this.topPos + 27, 13, 16) && this.minecraft != null && this.minecraft.gameMode != null) {
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 1234); // containerId, buttonId
            return true;
        }
        else {
            return super.mouseClicked(mouseX, mouseY, mouseKey);
        }
    }
}

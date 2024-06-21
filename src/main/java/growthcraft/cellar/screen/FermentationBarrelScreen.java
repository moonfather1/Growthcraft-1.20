package growthcraft.cellar.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import growthcraft.cellar.screen.container.FermentationBarrelMenu;
import growthcraft.cellar.shared.Reference;
import growthcraft.lib.kaupenjoe.screen.renderer.FluidTankRenderer;
import growthcraft.lib.utils.TextureHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FermentationBarrelScreen extends AbstractContainerScreen<FermentationBarrelMenu> {

    private static final ResourceLocation TEXTURE = TextureHelper.getTextureGui(Reference.MODID, Reference.UnlocalizedName.FERMENT_BARREL);
    private static final List<Component> YEAST_WARNING = List.of(Component.translatable("growthcraft_cellar.tooltip.fermentation.yeast_warning").withStyle(Style.EMPTY.withColor(0xd5bb88)));
    private static final List<Component> YEAST_ERROR = List.of(Component.translatable("growthcraft_cellar.tooltip.fermentation.yeast_error").withStyle(Style.EMPTY.withColor(0xd68a71)));

    private FluidTankRenderer fluidTankRenderer0;

    public FermentationBarrelScreen(FermentationBarrelMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        assignFluidRender();
    }

    private void assignFluidRender() {
        fluidTankRenderer0 = new FluidTankRenderer(4000, true, 50, 52);
    }
    @Override
    protected void renderBg(@NotNull GuiGraphics poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // Full background image
        poseStack.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // Progress Bar
        poseStack.blit(TEXTURE,
                x + 51, y + 48 - menu.getProgressionScaled(28),
                188, 28 - menu.getProgressionScaled(28),
                8, menu.getProgressionScaled(28)
        );

        fluidTankRenderer0.render(poseStack.pose(), x + 72, y + 17, menu.getFluidStack(0));
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

        // Fermentation progress Tooltips
        renderProgressToolTip(poseStack, mouseX, mouseY, x, y);

    }

    private void renderProgressToolTip(GuiGraphics poseStack, int mouseX, int mouseY, int x, int y) {
        if (isMouseAboveArea(mouseX, mouseY, x + 48, y + 18, 20, 30, 20, 30)) {
            List<Component> tooltip = new ArrayList<>();
            MutableComponent progressString = Component.translatable(Reference.MODID.concat(".tooltip.fermentation.progress"), menu.getPercentProgress());
            tooltip.add(progressString);

            poseStack.renderTooltip(
                    this.font,
                    tooltip,
                    Optional.empty(),
                    mouseX - x,
                    mouseY - y
            );
        }
    }

    private void renderFluidTankTooltips(GuiGraphics poseStack, int mouseX, int mouseY, int x, int y) {
        if (isMouseAboveArea(mouseX, mouseY, x + 72, y + 17, 50, 52, fluidTankRenderer0.getWidth(), fluidTankRenderer0.getHeight())) {
            poseStack.renderTooltip(
                    this.font,
                    fluidTankRenderer0.getTooltip(menu.getFluidStack(0), TooltipFlag.Default.NORMAL),
                    Optional.empty(),
                    mouseX - x,
                    mouseY - y
            );
        }
    }

    @Override     // yeast slot tooltip
    protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            if (this.hoveredSlot.container.getContainerSize() <= 3 && this.hoveredSlot.getSlotIndex() == 0) {
                if (menu.hasYeastWarning()) {
                    ItemStack itemstack = this.hoveredSlot.getItem();
                    graphics.renderTooltip(this.font, YEAST_WARNING, itemstack.getTooltipImage(), ItemStack.EMPTY, mouseX, mouseY);
                }
                else if (menu.hasYeastError()) {
                    graphics.renderTooltip(this.font, YEAST_ERROR, Optional.empty(), ItemStack.EMPTY, mouseX, mouseY);
                }
                else {
                    super.renderTooltip(graphics, mouseX, mouseY);
                }
            }
            else {
                super.renderTooltip(graphics, mouseX, mouseY);
            }
        }
    }

    private boolean isMouseAboveArea(int mouseX, int mouseY, int baseX, int baseY, int offsetX, int offsetY, int width, int height) {
        return (mouseX >= baseX && mouseX <= (baseX + offsetX)) && (mouseY >= baseY && mouseY <= (baseY + offsetY));
    }
}

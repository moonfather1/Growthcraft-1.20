package growthcraft.lib.item;

import net.minecraft.world.item.*;

import java.awt.*;

public class GrowthcraftItem extends Item {

    private int color;
    private DyeColor dye;
    private boolean hasFoil;

    public GrowthcraftItem() {
        this(64, false);
    }

    public GrowthcraftItem(int maxStackSize) {
        this(maxStackSize, false);
    }

    public GrowthcraftItem(int maxStackSize, boolean foil) {
        super(getInitProperties(maxStackSize, foil));
        this.color = 0x0;
        this.hasFoil = foil;
    }

    public GrowthcraftItem(int maxStackSize, Color color) {
        this(maxStackSize, false);
        this.color = color.getRGB();
    }
    
    public GrowthcraftItem(DyeColor dye) {
        this(64, false);
        this.dye = dye;
    }

    private static Properties getInitProperties(int maxStackSize, boolean isFoil) {
        Properties properties = new Properties();
        properties.stacksTo(maxStackSize);
        if(isFoil) properties.rarity(Rarity.UNCOMMON);
        return properties;
    }

    public int getColor(ItemStack stack, int layer) {
        return this.getColor(layer);
    }

    public int getColor(int layer) {
        return layer == 0 ? this.color : 0xFFFFFF;
    }
    
    public DyeColor getDyeColor() {
    	return this.dye;
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return this.hasFoil;
    }
}

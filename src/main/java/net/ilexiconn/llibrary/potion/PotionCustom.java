package net.ilexiconn.llibrary.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionCustom extends Potion
{
    public final ResourceLocation texture;

    public PotionCustom(int id, boolean isEffectBad, int liquidColor, ResourceLocation texture, int iconIndexX, int iconIndexY)
    {
        super(id, texture, isEffectBad, liquidColor);
        this.texture = texture;
        this.setIconIndex(iconIndexX, iconIndexY);
    }

    @SideOnly(Side.CLIENT)
    public int getStatusIconIndex()
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("llibrary", "textures/potion/test.png"));
        return super.getStatusIconIndex();
    }
}
package net.ilexiconn.llibrary.client;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.message.message.MessageRequestTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

/**
 * Player skin tools.
 *
 * @author iLexiconn
 */
@SideOnly(Side.CLIENT)
public class TextureHelper
{
    private static ResourceLocation steveTexture = new ResourceLocation("textures/entity/steve.png");
    public static final Map<String, GameProfile> profiles = Maps.newHashMap();
    private static Minecraft mc = Minecraft.getMinecraft();

    public static ResourceLocation getPlayerSkin(GameProfile profile)
    {
        return getPlayerImage(profile, MinecraftProfileTexture.Type.SKIN);
    }

    public static ResourceLocation getPlayerCape(GameProfile profile)
    {
        return getPlayerImage(profile, MinecraftProfileTexture.Type.CAPE);
    }

    private static ResourceLocation getPlayerImage(GameProfile profile, MinecraftProfileTexture.Type type)
    {
        if (profile != null)
        {
            if (profile.getName().equals(mc.thePlayer.getGameProfile().getName())) return type == Type.CAPE ? mc.thePlayer.getLocationCape() : mc.thePlayer.getLocationSkin();

            if (profiles.containsKey(profile.getName()))
            {
                Map<?, ?> map = mc.getSkinManager().loadSkinFromCache(profiles.get(profile.getName()));
                if (map.containsKey(type)) return mc.getSkinManager().loadSkin((MinecraftProfileTexture) map.get(type), type);
            }
            else
            {
                profiles.put(profile.getName(), profile);
                LLibrary.networkWrapper.sendToServer(new MessageRequestTexture(profile.getName()));
            }
        }

        return type == Type.CAPE ? null : steveTexture;
    }

    public static void setPlayerSkin(AbstractClientPlayer entityPlayer, BufferedImage skin)
    {
        if (!hasBackup(entityPlayer)) backupPlayerSkin(entityPlayer);
        uploadPlayerSkin(entityPlayer, skin);
    }

    public static void resetPlayerSkin(AbstractClientPlayer entityPlayer)
    {
        BufferedImage image = getOriginalPlayerSkin(entityPlayer);
        if (image != null) uploadPlayerSkin(entityPlayer, image);
    }

    public static boolean hasBackup(AbstractClientPlayer player)
    {
        return new File("llibrary" + File.separator + "skin-backups" + File.separator + player.getDisplayNameString() + ".png").exists();
    }

    private static void backupPlayerSkin(AbstractClientPlayer entityPlayer)
    {
        ResourceLocation resource = getPlayerSkin(entityPlayer.getGameProfile());

        File file = new File("llibrary" + File.separator + "skin-backups");
        file.mkdir();
        File skinFile = new File(file, entityPlayer.getDisplayNameString() + ".png");
        try
        {
            skinFile.createNewFile();
            if (resource != null) ImageIO.write(ImageIO.read(mc.getResourceManager().getResource(resource).getInputStream()), "PNG", skinFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void uploadPlayerSkin(AbstractClientPlayer player, BufferedImage bufferedImage)
    {
        ITextureObject textureObject = Minecraft.getMinecraft().renderEngine.getTexture(player.getLocationSkin());

        if (textureObject == null)
        {
            textureObject = new ThreadDownloadImageData(null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", StringUtils.stripControlCodes(player.getDisplayNameString())), steveTexture, new ImageBufferDownload());
            Minecraft.getMinecraft().renderEngine.loadTexture(player.getLocationSkin(), textureObject);
        }

        uploadTexture(textureObject, bufferedImage);
    }

    private static BufferedImage getOriginalPlayerSkin(AbstractClientPlayer entityPlayer)
    {
        File file = new File("llibrary" + File.separator + "skin-backups" + File.separator + entityPlayer.getDisplayNameString() + ".png");
        BufferedImage bufferedImage = null;

        try
        {
            if (file.exists()) bufferedImage = ImageIO.read(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return bufferedImage;
    }

    private static void uploadTexture(ITextureObject textureObject, BufferedImage bufferedImage)
    {
        TextureUtil.uploadTextureImage(textureObject.getGlTextureId(), bufferedImage);
    }
}

package net.ilexiconn.llibrary.proxy;

import net.ilexiconn.llibrary.client.ClientEventHandler;
import net.ilexiconn.llibrary.client.gui.GuiChangelog;
import net.ilexiconn.llibrary.client.render.entity.RenderLLibraryPlayer;
import net.ilexiconn.llibrary.update.ChangelogHandler;
import net.ilexiconn.llibrary.update.ModUpdateContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy
{
    public void preInit()
    {
        super.preInit();

        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        FMLCommonHandler.instance().bus().register(new ClientEventHandler());
    }

    public void postInit()
    {
        super.postInit();

        Minecraft.getMinecraft().getRenderManager().entityRenderMap.put(EntityPlayer.class, new RenderLLibraryPlayer());
    }

    public void openChangelogGui(ModUpdateContainer mod, String version)
    {
        String[] changelog = null;

        try
        {
            changelog = ChangelogHandler.getChangelog(mod, version);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Minecraft.getMinecraft().displayGuiScreen(new GuiChangelog(mod, version, changelog));
    }
}

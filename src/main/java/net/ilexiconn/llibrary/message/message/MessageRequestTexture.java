package net.ilexiconn.llibrary.message.message;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.message.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessageRequestTexture extends AbstractMessage<MessageRequestTexture>
{
    public String name;

    public MessageRequestTexture()
    {

    }

    public MessageRequestTexture(String n)
    {
        name = n;
    }

    public void handleClientMessage(MessageRequestTexture message, EntityPlayer player)
    {

    }

    public void handleServerMessage(MessageRequestTexture message, final EntityPlayer player)
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                GameProfile profile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(name);

                if (profile != null)
                {
                    Property property = Iterables.getFirst(profile.getProperties().get("textures"), null);
                    if (property == null)  profile = MinecraftServer.getServer().getMinecraftSessionService().fillProfileProperties(profile, true);
                    LLibrary.networkWrapper.sendTo(new MessageReturnTexture(profile), (EntityPlayerMP) player);
                }
            }

        }, name).start();
    }

    public void fromBytes(ByteBuf buf)
    {
        name = ByteBufUtils.readUTF8String(buf);
    }

    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, name);
    }
}

package net.ilexiconn.llibrary.message.message;

import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.client.TextureHelper;
import net.ilexiconn.llibrary.message.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessageReturnTexture extends AbstractMessage<MessageReturnTexture>
{
    public GameProfile profile;

    public MessageReturnTexture()
    {

    }

    public MessageReturnTexture(GameProfile p)
    {
        profile = p;
    }

    public void handleClientMessage(MessageReturnTexture message, EntityPlayer player)
    {
        TextureHelper.profiles.put(profile.getName(), profile);
    }

    public void handleServerMessage(MessageReturnTexture message, EntityPlayer player)
    {

    }

    public void fromBytes(ByteBuf buf)
    {
        NBTTagCompound nbt = ByteBufUtils.readTag(buf);
        profile = NBTUtil.readGameProfileFromNBT(nbt);
    }

    public void toBytes(ByteBuf buf)
    {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTUtil.writeGameProfile(nbt, profile);
        ByteBufUtils.writeTag(buf, nbt);
    }
}

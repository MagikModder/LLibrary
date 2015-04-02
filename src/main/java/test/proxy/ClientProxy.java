package test.proxy;

import net.ilexiconn.llibrary.client.render.RenderHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import test.render.TestModelExtension;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy
{
    public void init()
    {
        RenderHelper.registerModelExtension(new TestModelExtension());
    }
}

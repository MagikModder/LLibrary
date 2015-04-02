package net.ilexiconn.llibrary.command;

import com.google.common.collect.Lists;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.color.EnumJsonChatColor;
import net.ilexiconn.llibrary.update.ChangelogHandler;
import net.ilexiconn.llibrary.update.ModUpdateContainer;
import net.ilexiconn.llibrary.update.UpdateHelper;
import net.ilexiconn.llibrary.update.VersionHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CommandLLibrary extends CommandBase
{
    public String getName()
    {
        return "llibrary";
    }

    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/llibrary list OR /llibrary update <modid> OR /llibrary changelog <modid> <version>";
    }

    public void execute(ICommandSender sender, String[] args) throws CommandException
    {
        String title = "[LLibHelper]" + EnumChatFormatting.YELLOW + " ";
        List<ModUpdateContainer> outdatedMods = VersionHandler.getOutdatedMods();

        if (args.length >= 1)
        {
            if (args[0].equalsIgnoreCase("list"))
            {
                if (args.length > 1)
                {
                    throw new WrongUsageException("/llibrary list");
                }

                ChatHelper.chatTo(sender, new ChatMessage("--- Showing a list of outdated mods ---", EnumJsonChatColor.DARK_GREEN));

                for (ModUpdateContainer mod : outdatedMods)
                {
                    ChatHelper.chatTo(sender, new ChatMessage("(" + mod.modid + ") ", EnumJsonChatColor.BLUE), new ChatMessage(mod.name + " version " + mod.version + " - Latest version: " + mod.latestVersion, EnumJsonChatColor.WHITE));
                }

                ChatHelper.chatTo(sender, new ChatMessage("Use ", EnumJsonChatColor.GREEN), new ChatMessage("/llibrary update <modid>", EnumJsonChatColor.YELLOW), new ChatMessage(" to update the desired mod, ", EnumJsonChatColor.GREEN), new ChatMessage("or", EnumJsonChatColor.RED));
                ChatHelper.chatTo(sender, new ChatMessage("Use ", EnumJsonChatColor.GREEN), new ChatMessage("/llibrary changelog <modid> <version>", EnumJsonChatColor.YELLOW), new ChatMessage(" to see its version changelog.", EnumJsonChatColor.GREEN));

                return;
            }

            if (args[0].equalsIgnoreCase("update"))
            {
                if (args.length != 2)
                {
                    throw new WrongUsageException("/llibrary update <modid>");
                }

                for (ModUpdateContainer mod : outdatedMods)
                {
                    if (args[1].equalsIgnoreCase(mod.modid))
                    {
                        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;

                        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
                        {
                            try
                            {
                                desktop.browse(mod.website.toURI());
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                return;
            }

            if (args[0].equalsIgnoreCase("changelog"))
            {
                if (args.length != 3)
                {
                    throw new WrongUsageException("/llibrary changelog <modid> <version>");
                }

                for (int i = 0; i < UpdateHelper.modList.size(); ++i)
                {
                    ModUpdateContainer mod = UpdateHelper.modList.get(i);

                    if (args[1].equalsIgnoreCase(mod.modid))
                    {
                        boolean hasChangelogForVersion = false;

                        try
                        {
                            hasChangelogForVersion = ChangelogHandler.hasModGotChangelogForVersion(mod, args[2]);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        if (hasChangelogForVersion)
                        {
                            LLibrary.proxy.openChangelogGui(mod, args[2]);
                        }
                        else
                        {
                            ChatHelper.chatTo(sender, new ChatMessage("There is no changelog for mod '" + mod.modid + "' version " + args[2] + "!", EnumJsonChatColor.RED));
                        }
                    }
                }

                return;
            }
        }
        throw new WrongUsageException(getCommandUsage(sender));
    }

    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, "list", "update", "changelog");
        }
        else
        {
            if (args[0].equalsIgnoreCase("update") && args.length == 2)
            {
                return func_175762_a(args, getAllModIDs(VersionHandler.getOutdatedMods()));
            }
            if (args[0].equalsIgnoreCase("changelog") && args.length == 2)
            {
                return func_175762_a(args, getAllModIDs(UpdateHelper.modList));
            }
            if (args[0].equalsIgnoreCase("changelog") && args.length == 3)
            {
                return func_175762_a(args, getAllModChangelogs(UpdateHelper.getModContainerById(args[1])));
            }
        }
        return null;
    }

    protected List getAllModIDs(List list)
    {
        ArrayList arraylist = Lists.newArrayList();

        for (Object aCollection : list)
        {
            ModUpdateContainer mod = (ModUpdateContainer) aCollection;
            arraylist.add(mod.modid);
        }

        return arraylist;
    }

    protected List getAllModChangelogs(ModUpdateContainer mod)
    {
        ArrayList arraylist = Lists.newArrayList();

        for (String string : mod.updateFile)
        {
            String s = mod.modid + "Log|";

            if (string.startsWith(s))
            {
                String s1 = string.substring(s.length()).split(":")[0];
                arraylist.add(s1);
            }
        }

        return arraylist;
    }
}
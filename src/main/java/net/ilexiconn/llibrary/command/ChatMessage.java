package net.ilexiconn.llibrary.command;

import net.ilexiconn.llibrary.color.EnumJsonChatColor;

/**
 * @author FiskFille
 */
public class ChatMessage
{
    public String message;
    public EnumJsonChatColor color;

    public ChatMessage(String m, EnumJsonChatColor c)
    {
        message = m;
        color = c;
    }
}
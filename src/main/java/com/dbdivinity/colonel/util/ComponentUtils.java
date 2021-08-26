package com.dbdivinity.colonel.util;

import com.mojang.brigadier.Message;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class ComponentUtils {
    public static IChatComponent fromMessage(Message message) {
        return (IChatComponent) (message instanceof IChatComponent ? (IChatComponent) message : new ChatComponentText(message.getString()));
    }

    public static class TranslatedComponent extends ChatComponentTranslation implements Message {

        public TranslatedComponent(String message, Object... args) {
            super(message, args);
        }

        @Override
        public String getString() {
            return this.getUnformattedTextForChat();
        }
    }
}

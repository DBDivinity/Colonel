package com.dbdivinity.colonel.command.builtin;

import com.dbdivinity.colonel.command.Commands;
import com.dbdivinity.colonel.util.ComponentUtils;
import com.dbdivinity.colonel.util.ModInfo;
import com.google.common.collect.Iterables;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.Iterator;
import java.util.Map;

public class HelpCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(new ComponentUtils.TranslatedComponent("commands.help.failed"));

    public HelpCommand() {
    }

    public static void register(CommandDispatcher<ICommandSender> dispatch) {
        dispatch.register((Commands.literal("help").executes((p_137794_) -> {
            Map<CommandNode<ICommandSender>, String> var2 = dispatch.getSmartUsage(dispatch.getRoot(), p_137794_.getSource());
            Iterator var3 = var2.values().iterator();

            while (var3.hasNext()) {
                String var4 = (String) var3.next();
                p_137794_.getSource().addChatMessage(new ChatComponentText("/" + var4));
            }

            return var2.size();
        })).then(Commands.argument("command", StringArgumentType.greedyString()).executes((sender) -> {
            ParseResults<ICommandSender> var2 = dispatch.parse(StringArgumentType.getString(sender, "command"), sender.getSource());
            if (var2.getContext().getNodes().isEmpty()) {
                throw ERROR_FAILED.create();
            } else {
                Map nodeMap = dispatch.getSmartUsage((Iterables.getLast(var2.getContext().getNodes())).getNode(), sender.getSource());
                Iterator<String> commands = nodeMap.values().iterator();

                while (commands.hasNext()) {
                    String var5 = commands.next();
                    String var10003 = var2.getReader().getString();
                    sender.getSource().addChatMessage(new ChatComponentText("/" + ModInfo.baseCommand + " " + var10003 + " " + var5));
                }

                return nodeMap.size();
            }
        })));
    }
}
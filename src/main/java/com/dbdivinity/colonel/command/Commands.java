package com.dbdivinity.colonel.command;

import com.dbdivinity.colonel.command.builtin.HelpCommand;
import com.dbdivinity.colonel.event.BrigCommandEvent;
import com.dbdivinity.colonel.event.RegisterCommandsEvent;
import com.dbdivinity.colonel.util.ComponentUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;

public class Commands {
    CommandDispatcher<ICommandSender> dispatcher;

    public Commands(Commands.CommandSelection selection) {
        this.dispatcher = new CommandDispatcher<>();
        HelpCommand.register(this.dispatcher);
        onCommandRegister(this.dispatcher, selection);
    }

    private static void onCommandRegister(CommandDispatcher<ICommandSender> dispatcher, Commands.CommandSelection environment) {
        RegisterCommandsEvent event = new RegisterCommandsEvent(dispatcher, environment);
        MinecraftForge.EVENT_BUS.post(event);
    }

    public static LiteralArgumentBuilder<ICommandSender> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    public static <T> RequiredArgumentBuilder<ICommandSender, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    @Nullable
    public static <S> CommandSyntaxException getParseException(ParseResults<S> result) {
        if (!result.getReader().canRead()) {
            return null;
        } else if (result.getExceptions().size() == 1) {
            return result.getExceptions().values().iterator().next();
        } else {
            return result.getContext().getRange().isEmpty() ? CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(result.getReader()) : CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(result.getReader());
        }
    }

    CommandDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public int performCommand(ICommandSender source, String command) {
        StringReader stringreader = new StringReader(command);
        if (stringreader.canRead() && stringreader.peek() == '/') {
            stringreader.skip();
        }

        try {
            try {
                ParseResults<ICommandSender> parse = this.dispatcher.parse(stringreader, source);
                BrigCommandEvent event = new BrigCommandEvent(parse);
                if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) {
                    if (event.getException() != null) {
                        com.google.common.base.Throwables.propagateIfPossible(event.getException());
                    }
                    return 1;
                }
                return this.dispatcher.execute(event.getParseResults());
            } catch (CommandSyntaxException commandsyntaxexception) {
                source.addChatMessage(ComponentUtils.fromMessage(commandsyntaxexception.getRawMessage()));
            }
        } finally {
            // Logging?
            return 0;
        }
    }

    public enum CommandSelection {
        ALL(true, true),
        DEDICATED(false, true),
        INTEGRATED(true, false);

        final boolean includeIntegrated;
        final boolean includeDedicated;

        private CommandSelection(boolean integrated, boolean dedicated) {
            this.includeIntegrated = integrated;
            this.includeDedicated = dedicated;
        }
    }
}

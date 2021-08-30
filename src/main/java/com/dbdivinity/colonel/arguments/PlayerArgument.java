package com.dbdivinity.colonel.arguments;

import com.dbdivinity.colonel.arguments.selectors.EntitySelector;
import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PlayerArgument implements ArgumentType<EntitySelector> {
    public static final SimpleCommandExceptionType INVALID_PLAYER_NAME_OR_UUID =
            new SimpleCommandExceptionType(
                    new LiteralMessage("Could not find a player with the specified UUID/Name"));
    public static final SimpleCommandExceptionType UUID_NOT_SUPPORTED =
            new SimpleCommandExceptionType(
                    new LiteralMessage("UUIDs are not supported currently"));
    private int type;
    private List<String> names = new ArrayList<>();
    private List<UUID> uuids = new ArrayList<>();

    private PlayerArgument(int type) {
        this.type = type;
    }

    public static PlayerArgument player() {
        return new PlayerArgument(0);
    }

    public static PlayerArgument players() {
        return new PlayerArgument(1);
    }

    @Override
    public EntitySelector parse(StringReader reader) throws CommandSyntaxException {
        names = new ArrayList<>();
        uuids = new ArrayList<>();
        String name = reader.readString();
        switch (this.type) {
            case 0:
                uuid_or_name(name, reader);
                break;
            case 1:
                while (reader.canRead()) {
                    name = reader.readUnquotedString();
                    uuid_or_name(name, reader);
                }
                break;
        }
        EntitySelector selector = new EntitySelector(this.names, this.uuids);
        selector.process();
        return selector;
    }

    private void uuid_or_name(String name, ImmutableStringReader reader)
            throws CommandSyntaxException {
        try {
            UUID.fromString(name);
            // If this is a UUID throw a response saying its unsupported currently.
            throw UUID_NOT_SUPPORTED.createWithContext(reader);
        } catch (IllegalArgumentException e) {
            names.add(name);
            // Otherwise, we add this to the names list.
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(
            CommandContext<S> context, SuggestionsBuilder builder) {
        String[] usernames = MinecraftServer.getServer().getConfigurationManager().getAllUsernames();
        for (String name : usernames) {
            if (name.toLowerCase().startsWith(builder.getRemainingLowerCase())) {
                builder.suggest(name);
            }
        }
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return Arrays.asList(MinecraftServer.getServer().getConfigurationManager().getAllUsernames());
    }
}

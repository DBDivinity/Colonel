package com.dbdivinity.colonel.arguments.selectors;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntitySelector {
    private List<Entity> entities;
    private List<String> names;
    private List<UUID> uuids;

    public EntitySelector(List<String> names, List<UUID> uuids) {
        this.names = names;
        this.uuids = uuids;
        this.entities = new ArrayList<>();
    }

    public void process() throws CommandSyntaxException {
        for (String name : names) {
            EntityPlayerMP player = getPlayerByName(name);
            if (player != null) {
                entities.add(player);
            } else {
                throw new SimpleCommandExceptionType(
                    new LiteralMessage("Could not find a player with the specified UUID/Name: " + name))
                    .create();
            }
        }
    }

    public EntityPlayerMP getOnePlayer() {
        for (Entity entity : entities) {
            if (entity instanceof EntityPlayerMP) {
                return (EntityPlayerMP) entity;
            }
        }
        // This should never happen
        return null;
    }

    public List<EntityPlayerMP> getAllPlayers() {
        List<EntityPlayerMP> players = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity instanceof EntityPlayerMP) {
                players.add((EntityPlayerMP) entity);
            }
        }
        return players;
    }

    private EntityPlayerMP getPlayerByName(String name){
        ServerConfigurationManager server = MinecraftServer.getServer().getConfigurationManager();
        return server.func_152612_a(name);
    }
}

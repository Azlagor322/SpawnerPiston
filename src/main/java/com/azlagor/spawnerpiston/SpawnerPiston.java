package com.azlagor.spawnerpiston;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.type.Piston;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.java.JavaPlugin;


public final class SpawnerPiston extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPistonExtend(BlockRedstoneEvent event) {
        if(event.getBlock().getType() != Material.LEVER) return;
        if(event.getBlock().getBlockPower() == 15) return;
        BlockFace[] minList = {BlockFace.DOWN, BlockFace.UP, BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH};
        Block block = event.getBlock();
        for (BlockFace face : minList) {
            Block connectedBlock = block.getRelative(face);
            if(!(connectedBlock.getType() == Material.STICKY_PISTON || connectedBlock.getType() == Material.PISTON)) continue;
            Piston p = (Piston) connectedBlock.getBlockData();
            Block pushBlock = connectedBlock.getRelative(p.getFacing());
            if(pushBlock.getType() != Material.SPAWNER) return;
            Block toPushBlock = connectedBlock.getRelative(p.getFacing(),2);
            if(toPushBlock.getType() != Material.AIR) return;
            CreatureSpawner spawner = (CreatureSpawner) pushBlock.getState();
            EntityType entityType = spawner.getSpawnedType();
            int delay = spawner.getDelay();
            int minSpawnDelay = spawner.getMinSpawnDelay();
            int maxSpawnDelay = spawner.getMaxSpawnDelay();
            int spawnCount = spawner.getSpawnCount();
            int maxNearbyEntities = spawner.getMaxNearbyEntities();
            int requiredPlayerRange = spawner.getRequiredPlayerRange();
            int spawnRange = spawner.getSpawnRange();

            Block newBlock = toPushBlock;
            newBlock.setType(pushBlock.getType());
            CreatureSpawner newSpawner = (CreatureSpawner) newBlock.getState();
            newSpawner.setBlockData(pushBlock.getBlockData());
            newSpawner.setSpawnedType(entityType);
            newSpawner.setDelay(delay);
            newSpawner.setMinSpawnDelay(minSpawnDelay);
            newSpawner.setMaxSpawnDelay(maxSpawnDelay);
            newSpawner.setSpawnCount(spawnCount);
            newSpawner.setMaxNearbyEntities(maxNearbyEntities);
            newSpawner.setRequiredPlayerRange(requiredPlayerRange);
            newSpawner.setSpawnRange(spawnRange);
            newSpawner.update();
            pushBlock.setType(Material.AIR);
        }


    }
}

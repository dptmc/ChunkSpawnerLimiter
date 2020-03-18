package com.cyprias.chunkspawnerlimiter.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.cyprias.chunkspawnerlimiter.Common;
import com.cyprias.chunkspawnerlimiter.tasks.InspectTask;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.cyprias.chunkspawnerlimiter.Config;
import com.cyprias.chunkspawnerlimiter.ChunkSpawnerLimiter;
import com.cyprias.chunkspawnerlimiter.compare.MobGroupCompare;
import org.bukkit.scheduler.BukkitTask;

public class WorldListener implements Listener {
    private ChunkSpawnerLimiter plugin;
    private HashMap<Chunk, Integer> chunkTasks = new HashMap<>();

    public WorldListener(final ChunkSpawnerLimiter plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChunkLoadEvent(ChunkLoadEvent e) {
        Common.debug("ChunkLoadEvent " + e.getChunk().getX() + " " + e.getChunk().getZ());
        if (Config.Properties.ACTIVE_INSPECTIONS) {
            InspectTask inspectTask = new InspectTask(e.getChunk());
            long delay = Config.Properties.INSPECTION_FREQUENCY * 20L;
            BukkitTask task = inspectTask.runTaskTimer(plugin, delay, delay);
            inspectTask.setId(task.getTaskId());
            chunkTasks.put(e.getChunk(), task.getTaskId());
        }

        if (Config.Properties.CHECK_CHUNK_LOAD)
            checkChunk(e.getChunk());
    }

    @EventHandler
    public void onChunkUnloadEvent(ChunkUnloadEvent e) {
        Common.debug("ChunkUnloadEvent " + e.getChunk().getX() + " " + e.getChunk().getZ());

        if (chunkTasks.containsKey(e.getChunk())) {
            Bukkit.getServer().getScheduler().cancelTask(chunkTasks.get(e.getChunk()));
            chunkTasks.remove(e.getChunk());
        }

        if (Config.Properties.CHECK_CHUNK_UNLOAD)
            checkChunk(e.getChunk());
    }

    /**
     * Checks the chunk for entities, removes entities if over the limit.
     *
     * @param chunk Chunk
     */
    public static void checkChunk(Chunk chunk) {
        if (Config.getStringList("excluded-worlds").contains(chunk.getWorld().getName())) {
            return;
        }

        Entity[] entities = chunk.getEntities();
        HashMap<String, ArrayList<Entity>> types = addEntitiesByConfig(entities);

        for (Entry<String, ArrayList<Entity>> entry : types.entrySet()) {
            String entityType = entry.getKey();
            int limit = Config.getEntityLimit(entityType);

            if (entry.getValue().size() > limit) {
                Common.debug("Removing " + (entry.getValue().size() - limit) + " " + entityType + " @ " + chunk.getX() + " " + chunk.getZ());
                if (Config.getBoolean("properties.notify-players")) {
                    notifyPlayers(entry, entities, limit, entityType);
                }
                removeEntities(entry, limit);
            }
        }
    }

    private static boolean hasCustomName(Entity entity) {
        if (Config.Properties.PRESERVE_NAMED_ENTITIES)
            return entity.getCustomName()!=null;
        return false;
    }

    private static boolean hasMetaData(Entity entity) {
        for (String metadata : Config.getStringList("properties.ignore-metadata")) {
            if (entity.hasMetadata(metadata)) {
                return true;
            }
        }
        return false;
    }

    private static void removeEntities(Entry<String, ArrayList<Entity>> entry, int limit) {
        for (int i = entry.getValue().size() - 1; i >= limit; i--) {
            if (hasMetaData(entry.getValue().get(i)) || hasCustomName(entry.getValue().get(i)))
                continue;
            entry.getValue().get(i).remove();
        }
    }

    private static HashMap<String, ArrayList<Entity>> addEntitiesByConfig(Entity[] entities) {
        HashMap<String, ArrayList<Entity>> modifiedTypes = new HashMap<>();
        for (int i = entities.length - 1; i >= 0; i--) {
            EntityType type = entities[i].getType();

            String entityType = type.name();
            String eGroup = MobGroupCompare.getMobGroup(entities[i]);

            if (Config.contains("entities." + entityType)) {
                if (!modifiedTypes.containsKey(entityType))
                    modifiedTypes.put(entityType, new ArrayList<>());
                modifiedTypes.get(entityType).add(entities[i]);
            }

            if (Config.contains("entities." + eGroup)) {
                if (!modifiedTypes.containsKey(eGroup))
                    modifiedTypes.put(eGroup, new ArrayList<>());
                modifiedTypes.get(eGroup).add(entities[i]);
            }
        }
        return modifiedTypes;
    }


    private static void notifyPlayers(Entry<String, ArrayList<Entity>> entry, Entity[] entities, int limit, String entityType) {
        for (int i = entities.length - 1; i >= 0; i--) {
            if (entities[i] instanceof Player) {
                final Player p = (Player) entities[i];
                Common.tell(p, Config.getString("messages.removedEntities", entry.getValue().size() - limit, entityType));
            }
        }
    }
}
package com.cyprias.chunkspawnerlimiter.utils

import com.cyprias.chunkspawnerlimiter.ChunkSpawnerLimiter
import com.cyprias.chunkspawnerlimiter.compare.MobGroupCompare
import com.cyprias.chunkspawnerlimiter.configs.CslConfig
import com.cyprias.chunkspawnerlimiter.messages.Debug
import org.bukkit.Chunk
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Raider


object ChunkUtil {
    lateinit var plugin: ChunkSpawnerLimiter
    private lateinit var config: CslConfig

    fun init(plugin: ChunkSpawnerLimiter) {
        this.plugin = plugin
        this.config = plugin.cslConfig
    }

    fun checkChunk(chunk: Chunk) {
        if (config.excludedWorlds?.contains(chunk.world.name) == true) {
            return
        }

        val entities = chunk.entities
        val types = addEntitiesByConfig(entities)

        for (entry in types.entries) {
            val entityType = entry.key
            val limit: Int = config.getEntityLimit(entityType)

            if (entry.value.size > limit) {
                ChatUtil.debug(
                    Debug.REMOVING_ENTITY_AT,
                    entry.value.size - limit,
                    entityType,
                    chunk.x,
                    chunk.z
                )
                if (config.notifyPlayers) {
                    notifyPlayers(entry, entities, limit, entityType)
                }
                removeEntities(entry, limit)
            }
        }
    }

    private fun hasCustomName(entity: Entity): Boolean {
        if (config.preserveNamedEntities) return entity.customName != null
        return false
    }

    private fun hasMetaData(entity: Entity): Boolean {
        for (metadata in config.ignoreMetadata!!) {
            if (entity.hasMetadata(metadata)) {
                return true
            }
        }
        return false
    }

    private fun isPartOfRaid(entity: Entity): Boolean {
        if (!config.preserveRaidEntities) return false

        if (entity is Raider) {
            for (raid in entity.world.raids) {
                val potentialMatch = raid.raiders.stream().anyMatch { r: Raider -> r == entity }
                if (!potentialMatch) continue
                return true
            }
        }
        return false
    }

    private fun removeEntities(entry: Map.Entry<String, ArrayList<Entity>>, limit: Int) {
        for (i in entry.value.size - 1 downTo limit) {
            val entity = entry.value[i]
            if (hasMetaData(entity) || hasCustomName(entity) || (entity is Player) || isPartOfRaid(entity)) continue
            entity.remove()
        }
    }

    private fun addEntitiesByConfig(entities: Array<Entity>): HashMap<String, ArrayList<Entity>> {
        val modifiedTypes = HashMap<String, ArrayList<Entity>>()
        for (i in entities.indices.reversed()) {
            val type = entities[i].type

            val entityType = type.name
            val entityMobGroup: String = MobGroupCompare.getMobGroup(
                entities[i]
            )

            if (config.contains("entities.$entityType") == true) {
                modifiedTypes.putIfAbsent(entityType, ArrayList())
                modifiedTypes[entityType]!!.add(entities[i])
            }

            if (config.contains("entities.$entityMobGroup") == true) {
                modifiedTypes.putIfAbsent(entityMobGroup, ArrayList())
                modifiedTypes[entityMobGroup]!!.add(entities[i])
            }
        }
        return modifiedTypes
    }


    private fun notifyPlayers(
        entry: Map.Entry<String, ArrayList<Entity>>,
        entities: Array<Entity>,
        limit: Int,
        entityType: String
    ) {
        for (i in entities.indices.reversed()) {
            if (entities[i] is Player) {
                val p = entities[i] as Player
                config.removedEntities?.let {
                    p.message(
                        it,
                        entry.value.size - limit,
                        entityType
                    )
                }
            }
        }
    }
}
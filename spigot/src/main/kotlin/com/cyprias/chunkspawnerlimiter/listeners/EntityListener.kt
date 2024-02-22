package com.cyprias.chunkspawnerlimiter.listeners

import com.cyprias.chunkspawnerlimiter.configs.CslConfig
import com.cyprias.chunkspawnerlimiter.messages.Debug
import org.bukkit.Chunk
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.vehicle.VehicleCreateEvent
import com.cyprias.chunkspawnerlimiter.utils.ChatUtil
import com.cyprias.chunkspawnerlimiter.utils.ChunkUtil

class EntityListener(
    private var config: CslConfig
): Listener {
    @EventHandler
    fun onCreatureSpawnEvent(event: CreatureSpawnEvent) {
        if (event.isCancelled || !config.watchCreatureSpawns) return

        val reason = event.spawnReason.toString()

        if (!config.isSpawnReason(reason)) {
            ChatUtil.debug(
                Debug.IGNORE_ENTITY,
                event.entity.type,
                reason
            )
            return
        }

        val chunk = event.location.chunk
        ChunkUtil.checkChunk(chunk)
        checkSurroundings(chunk)
    }


    @EventHandler
    fun onVehicleCreateEvent(event: VehicleCreateEvent) {
        if (event.isCancelled || !config.watchVehicleCreate) return

        val chunk = event.vehicle.location.chunk
        ChunkUtil.checkChunk(chunk)
        checkSurroundings(chunk)
    }


    private fun checkSurroundings(chunk: Chunk) {
        val surrounding: Int = config.checkSurroundingChunks
        if (surrounding > 0) {
            for (x in chunk.x + surrounding downTo (chunk.x - surrounding)) {
                for (z in chunk.z + surrounding downTo (chunk.z - surrounding)) {
                    ChunkUtil.checkChunk(chunk.world.getChunkAt(x, z))
                }
            }
        }
    }
}
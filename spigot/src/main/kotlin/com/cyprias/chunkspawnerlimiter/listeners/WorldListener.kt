package com.cyprias.chunkspawnerlimiter.listeners

import com.cyprias.chunkspawnerlimiter.ChunkSpawnerLimiter
import com.cyprias.chunkspawnerlimiter.messages.Debug
import com.cyprias.chunkspawnerlimiter.tasks.InspectTask
import com.cyprias.chunkspawnerlimiter.utils.ChatUtil
import com.cyprias.chunkspawnerlimiter.utils.ChunkUtil
import org.bukkit.Chunk
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.ChunkLoadEvent
import org.bukkit.event.world.ChunkUnloadEvent
import org.bukkit.scheduler.BukkitTask


class WorldListener(private val plugin: ChunkSpawnerLimiter) : Listener {
    private val chunkTasks: MutableMap<Chunk, Int> = HashMap()

    @EventHandler
    fun onChunkLoadEvent(event: ChunkLoadEvent) {
        ChatUtil.debug(Debug.CHUNK_LOAD_EVENT, event.chunk.x, event.chunk.z)
        val config = plugin.cslConfig
        if (config.activeInspections) {
            val inspectTask = InspectTask(plugin, event.chunk)
            val delay: Long = config.inspectionFrequency * 20L
            val task: BukkitTask = inspectTask.runTaskTimer(plugin, delay, delay)
            inspectTask.id = task.taskId

            chunkTasks[event.chunk] = task.taskId
        }

        if (config.checkChunkLoad) ChunkUtil.checkChunk(event.chunk)
    }

    @EventHandler
    fun onChunkUnloadEvent(event: ChunkUnloadEvent) {
        ChatUtil.debug(
            Debug.CHUNK_UNLOAD_EVENT,
            event.chunk.x,
            event.chunk.z
        )

        if (chunkTasks.containsKey(event.chunk)) {
            chunkTasks[event.chunk]?.let { plugin.server.scheduler.cancelTask(it) }
            chunkTasks.remove(event.chunk)
        }

        if (plugin.cslConfig.checkChunkUnload) ChunkUtil.checkChunk(event.chunk)
    }


}
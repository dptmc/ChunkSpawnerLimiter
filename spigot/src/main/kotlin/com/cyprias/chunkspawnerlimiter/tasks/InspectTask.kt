package com.cyprias.chunkspawnerlimiter.tasks

import com.cyprias.chunkspawnerlimiter.ChunkSpawnerLimiter
import com.cyprias.chunkspawnerlimiter.listeners.WorldListener
import com.cyprias.chunkspawnerlimiter.messages.Debug
import org.bukkit.Chunk
import org.bukkit.scheduler.BukkitRunnable
import com.cyprias.chunkspawnerlimiter.utils.ChatUtil
import com.cyprias.chunkspawnerlimiter.utils.ChunkUtil
import kotlin.properties.Delegates

class InspectTask(
    private val plugin: ChunkSpawnerLimiter,
    private var chunk: Chunk
): BukkitRunnable() {
    var id by Delegates.notNull<Int>()

    override fun run() {
        ChatUtil.debug(Debug.ACTIVE_CHECK, chunk.x, chunk.z)
        if (!chunk.isLoaded) {
            plugin.cancelTask(id)
            return
        }
        ChunkUtil.checkChunk(chunk)
    }

}
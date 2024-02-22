package com.cyprias.chunkspawnerlimiter.listeners

import com.cyprias.chunkspawnerlimiter.ChunkSpawnerLimiter
import com.cyprias.chunkspawnerlimiter.messages.Debug
import org.bukkit.ChunkSnapshot
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import com.cyprias.chunkspawnerlimiter.utils.ChatUtil
import com.cyprias.chunkspawnerlimiter.utils.message
import com.cyprias.chunkspawnerlimiter.utils.replacePlaceholders
import com.cyprias.chunkspawnerlimiter.utils.title

class PlaceBlockListener(private val plugin: ChunkSpawnerLimiter) : Listener {

    @EventHandler
    fun onPlace(event: BlockPlaceEvent) {
        if (event.isCancelled || !plugin.blocksConfig.enabled) return

        if (plugin.cslConfig.excludedWorlds?.contains(event.block.chunk.world.name) == true) return

        val placedType = event.block.type
        if (plugin.blocksConfig.hasLimit(placedType)) {
            val limit: Int? = plugin.blocksConfig.getLimit(placedType)
            val minY = getMinY(event.block.world)
            val maxY = getMaxY(event.block.world)
            val amountInChunk = countBlocksInChunk(event.block.chunk.chunkSnapshot, placedType, minY, maxY)
            if (amountInChunk > limit!!) {
                event.isCancelled = true

                if (plugin.blocksConfig.notifyMessage) {
                    event.player.message(plugin.cslConfig.maxAmountBlocks.replacePlaceholders(placedType.name, limit))
                }
                if (plugin.blocksConfig.notifyTitle) {

                    event.player.title(
                        plugin.cslConfig.maxAmountBlocksTitle,
                        plugin.cslConfig.maxAmountBlocksSubtitle,
                        placedType.name,
                        limit
                    )

                }
            }
            ChatUtil.debug(Debug.BLOCK_PLACE_CHECK, placedType, amountInChunk, limit)
        }
    }

    private fun getMinY(world: World): Int {
        if (plugin.blocksConfig.hasWorld(world.name)) {
            return plugin.blocksConfig.getMinY(world.name)
        }
        return when (world.environment) {
            World.Environment.NORMAL -> plugin.blocksConfig.minY
            World.Environment.NETHER, World.Environment.THE_END -> 0
            else -> 0
        }
    }

    private fun getMaxY(world: World): Int {
        if (plugin.blocksConfig.hasWorld(world.name)) {
            return plugin.blocksConfig.getMaxY(world.name)
        }
        return plugin.blocksConfig.maxY
    }


    private fun countBlocksInChunk(chunkSnapshot: ChunkSnapshot, material: Material, minY: Int, maxY: Int): Int {
        var count = 0
        for (x in 0..15) {
            for (y in minY until maxY) {
                for (z in 0..15) {
                    if (chunkSnapshot.getBlockType(x, y, z) == material) count++
                }
            }
        }
        return count
    }

}
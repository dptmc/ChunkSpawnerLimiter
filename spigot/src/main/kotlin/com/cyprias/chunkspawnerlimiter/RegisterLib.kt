package com.cyprias.chunkspawnerlimiter

import co.aikar.commands.PaperCommandManager
import com.cyprias.chunkspawnerlimiter.commands.CslCommand
import org.bstats.bukkit.Metrics


object RegisterLib {
    lateinit var plugin: ChunkSpawnerLimiter

    fun init(plugin: ChunkSpawnerLimiter) {
        this.plugin = plugin
    }

    fun registerCommandsAndMetrics() {
        val paperCommandManager = PaperCommandManager(plugin)
        paperCommandManager.registerCommand(CslCommand(plugin))
        Metrics(plugin, 4195)
    }
}
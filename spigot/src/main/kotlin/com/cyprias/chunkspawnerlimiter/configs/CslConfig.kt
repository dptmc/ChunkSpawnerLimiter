package com.cyprias.chunkspawnerlimiter.configs

import com.cyprias.chunkspawnerlimiter.ChunkSpawnerLimiter
import org.bukkit.configuration.ConfigurationSection
import kotlin.properties.Delegates


class CslConfig(plugin: ChunkSpawnerLimiter): ConfigFile<ChunkSpawnerLimiter>(
    plugin,
    "config.yml",
    "",
    ""
) {
    /* Properties */
    var debugMessages = false
    var checkChunkLoad = false
    var checkChunkUnload = false
    var activeInspections = false
    var watchCreatureSpawns = false
    var watchVehicleCreate = false
    var checkSurroundingChunks = 0
    var inspectionFrequency = 0
    var notifyPlayers = false
    var preserveNamedEntities = false
    var preserveRaidEntities = false
    var ignoreMetadata: List<String>? = null

    /* Messages */
    var removedEntities: String? = null
    var reloadedConfig: String? = null

    lateinit var maxAmountBlocks: String
    lateinit var maxAmountBlocksTitle: String
    lateinit var maxAmountBlocksSubtitle: String

    var excludedWorlds: List<String>? = null

    var metrics by Delegates.notNull<Boolean>()

    override fun initValues() {
        this.excludedWorlds = config!!.getStringList("excluded-worlds")
        val propertiesPath = "properties."
        this.debugMessages = config!!.getBoolean(propertiesPath + "debug-messages")
        this.checkChunkLoad = config!!.getBoolean(propertiesPath + "check-chunk-load")
        this.checkChunkUnload = config!!.getBoolean(propertiesPath + "check-chunk-unload")
        this.activeInspections = config!!.getBoolean(propertiesPath + "active-inspections")
        this.watchCreatureSpawns = config!!.getBoolean(propertiesPath + "watch-creature-spawns")
        this.watchVehicleCreate = config!!.getBoolean(propertiesPath + "watch-vehicle-spawns")
        this.checkSurroundingChunks = config!!.getInt(propertiesPath + "check-surrounding-chunks")
        this.inspectionFrequency = config!!.getInt(propertiesPath + "inspection-frequency", 300)
        this.notifyPlayers = config!!.getBoolean(propertiesPath + "notify-players", false)
        this.preserveNamedEntities = config!!.getBoolean(propertiesPath + "preserve-named-entities", true)
        this.preserveRaidEntities = config!!.getBoolean(propertiesPath + "preserve-raid-entities", true)
        this.ignoreMetadata = config!!.getStringList(propertiesPath + "ignore-metadata")
        val messagesPath = "messages."
        this.removedEntities = config!!.getString(messagesPath + "removedEntities")
        this.reloadedConfig = config!!.getString(messagesPath + "reloadedConfig", "&cReloaded csl config.")
        this.maxAmountBlocks = config!!.getString(
            messagesPath + "maxAmountBlocks",
            "&6Cannot place more &4{material}&6. Max amount per chunk &2{amount}."
        ).toString()
        this.maxAmountBlocksTitle =
            config!!.getString(messagesPath + "maxAmountBlocksTitle", "&6Cannot place more &4{material}&6.").toString()
        this.maxAmountBlocksSubtitle =
            config!!.getString(messagesPath + "maxAmountBlocksSubtitle", "&6Max amount per chunk &2{amount}.").toString()

        this.metrics = config!!.getBoolean("metrics")
    }

    fun getEntityLimit(entityType: String): Int {
        return config!!.getInt("entities.$entityType")
    }

    fun isSpawnReason(reason: String): Boolean {
        return config!!.getBoolean("spawn-reasons.$reason")
    }

    fun contains(property: String) = config?.contains(property)

    fun spawnReasons(): ConfigurationSection {
        return config!!.getConfigurationSection("spawn-reasons")!!
    }

    fun entityLimits(): ConfigurationSection {
        return config!!.getConfigurationSection("entities")!!
    }
}
package com.cyprias.chunkspawnerlimiter.configs

import com.cyprias.chunkspawnerlimiter.ChunkSpawnerLimiter
import org.bukkit.Material
import org.jetbrains.annotations.Contract
import java.util.*

class BlocksConfig(plugin: ChunkSpawnerLimiter): ConfigFile<ChunkSpawnerLimiter>(
    plugin,
    "blocks.yml",
    "",
    ""
) {
    var enabled = false
    private lateinit var materialLimits: Map<Material, Int>
    var notifyMessage = false
    var notifyTitle = false

    var minY = 0
    var maxY = 0

    override fun initValues() {
        this.enabled = config!!.getBoolean("enabled", false)
        this.materialLimits = convertToMaterialLimits(config!!.getConfigurationSection("blocks")!!.getValues(false))
        this.notifyMessage = config!!.getBoolean("notify.message", false)
        this.notifyTitle = config!!.getBoolean("notify.title", true)
        this.minY = config!!.getInt("count.min-y", -64)
        this.maxY = config!!.getInt("count.max-y", 256)
    }

    private fun convertToMaterialLimits(map: Map<String, Any>): Map<Material, Int> {
        val materialIntegerEnumMap: MutableMap<Material, Int> = EnumMap(
            Material::class.java
        )

        for ((key, value) in map) {
            val material = Material.getMaterial(key)

            if (material == null) {
                plugin.logger.warning("Incorrect material name, check your blocks.yml and make sure it's set exactly.")
                plugin.logger.warning("Skipping entry (material=$key)")
                continue
            }

            val limit = value as Int?
            if (limit == null) {
                plugin.logger.warning("Missing limit value for material = " + material.name + ", skipping entry.")
                continue
            }
            materialIntegerEnumMap[material] = limit
        }

        return materialIntegerEnumMap
    }

    fun getLimit(material: Material): Int? {
        return materialLimits[material]
    }

    fun hasLimit(material: Material): Boolean {
        return materialLimits.containsKey(material)
    }

    fun getMinY(worldName: String): Int {
        return config!!.getInt(getWorldPath(worldName) + ".min-y", minY)
    }

    fun getMaxY(worldName: String): Int {
        return config!!.getInt(getWorldPath(worldName) + ".max-y", maxY)
    }

    fun hasWorld(worldName: String): Boolean {
        return config!!.contains(getWorldPath(worldName))
    }

    @Contract(pure = true)
    private fun getWorldPath(worldName: String): String {
        return "count.$worldName"
    }
}
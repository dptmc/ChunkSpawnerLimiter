package com.cyprias.chunkspawnerlimiter.configs

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

abstract class ConfigFile<T: JavaPlugin>(
    protected val plugin: T,
    private val fileName: String,
    private val resourcePath: String,
    folderName: String
)  {
    private var folder: File = File(plugin.dataFolder.path + File.separator + folderName)
    private var file: File? = null
    protected var config: FileConfiguration? = null
        get() {
            if (field == null) {
                reloadConfig()
            }
            return field!!
        }


    fun saveDefaultConfig() {
        if (this.file == null) {
            this.file = File(folder, fileName)
        }

        if (!this.file!!.exists()) {
            plugin.saveResource(resourcePath + fileName, false)
        }

        reloadConfig()
    }

    fun reloadConfig() {
        if (file == null) {
            file = File(folder, fileName)
        }

        this.config = YamlConfiguration.loadConfiguration(file!!)
        initValues()
    }


    abstract fun initValues()
}


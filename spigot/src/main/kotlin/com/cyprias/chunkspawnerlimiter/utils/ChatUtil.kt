package com.cyprias.chunkspawnerlimiter.utils

import com.cyprias.chunkspawnerlimiter.ChunkSpawnerLimiter
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


object ChatUtil {
    private lateinit var plugin: ChunkSpawnerLimiter

    fun init(plugin: ChunkSpawnerLimiter) {
        this.plugin = plugin
    }

    fun debug(message: String) {
        if (plugin.cslConfig.debugMessages) {
            plugin.logger.info("DEBUG $message")
        }
    }

    fun debug(message: String, vararg args: Any?) {
        debug(message.format(args))
    }

}

fun String.color() = ChatColor.translateAlternateColorCodes('&', this)

fun String.replacePlaceholders(material: String, amount: Int) = this.replace("{material}", material).replace("{amount}", amount.toString())

fun CommandSender.message(message: String) = this.sendMessage(message.color())

fun CommandSender.message(message: String, vararg args: Any?) = this.sendMessage(message.color().format(args))

fun Player.title(title: String, subtitle: String, material: String, amount: Int) {
    this.sendTitle(
        title.color().replacePlaceholders(material, amount),
        subtitle.color().replacePlaceholders(material, amount),
        10,
        70,
        20
    )
}

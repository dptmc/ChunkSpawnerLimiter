package com.cyprias.chunkspawnerlimiter.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.HelpCommand
import co.aikar.commands.annotation.Subcommand
import com.cyprias.chunkspawnerlimiter.ChunkSpawnerLimiter
import com.cyprias.chunkspawnerlimiter.messages.Command
import org.bukkit.command.CommandSender
import org.bukkit.configuration.ConfigurationSection
import com.cyprias.chunkspawnerlimiter.utils.message

@CommandAlias("csl")
class CslCommand(private val plugin: ChunkSpawnerLimiter) : BaseCommand() {
    private val config = plugin.cslConfig

    @Subcommand(Command.Reload.COMMAND)
    @CommandAlias(Command.Reload.ALIAS)
    @CommandPermission(Command.Reload.PERMISSION)
    @Description(Command.Reload.DESCRIPTION)
    fun onReload(sender: CommandSender) {
        plugin.reloadConfigs()
        config.reloadedConfig?.let { sender.message(it) }
    }


    @Subcommand(Command.Settings.COMMAND)
    @CommandAlias(Command.Settings.ALIAS)
    @CommandPermission(Command.Settings.PERMISSION)
    @Description(Command.Settings.DESCRIPTION)
    fun onSettings(sender: CommandSender) {
        //todo convert to $
        sender.message("&2&l-- ChunkSpawnerLimiter v%s --", plugin.description.version)
        sender.message("&2&l-- Properties --")
        sender.message("Debug Message: %s", config.debugMessages)
        sender.message("Check Chunk Load: %s", config.checkChunkLoad)
        sender.message("Check Chunk Unload: %s", config.checkChunkUnload)
        sender.message("Active Inspection: %s", config.activeInspections)
        sender.message("Watch Creature Spawns: %s", config.watchCreatureSpawns)
        sender.message("Check Surrounding Chunks: %s", config.checkSurroundingChunks)
        sender.message("Inspection Frequency: %d", config.inspectionFrequency)
        sender.message("Notify Players: %s", config.notifyPlayers)
        sender.message("Preserve Named Entities: %s", config.preserveNamedEntities)
        sender.message("Ignore Metadata: %s", config.ignoreMetadata.toString())
        sender.message("Excluded Worlds: %s", config.excludedWorlds)
        sender.message("&2&l-- Messages --")
        sender.message("Reloaded Config: %s", config.reloadedConfig)
        sender.message("Removed Entities: %s", config.removedEntities)
    }

    @Subcommand(Command.Info.COMMAND)
    @CommandAlias(Command.Info.ALIAS)
    @CommandPermission(Command.Info.PERMISSION)
    @Description(Command.Info.DESCRIPTION)
    fun onInfo(sender: CommandSender) {
        sender.message("&2&l-- ChunkSpawnerLimiter v%s --", plugin.description.version)
        sender.message("&2&l-- Reasons to cull on: --")
        sendConfigurationSection(sender, config.spawnReasons())
        sender.message("&2&l-- Entity Limits: --")
        sendConfigurationSection(sender, config.entityLimits())
    }

    @HelpCommand
    fun onHelp(help: CommandHelp) = help.showHelp()

    private fun sendConfigurationSection(sender: CommandSender, section: ConfigurationSection) {
        for ((key, value) in section.getValues(false)) {
            sender.message("%s: %s", key, value.toString())
        }
    }
}
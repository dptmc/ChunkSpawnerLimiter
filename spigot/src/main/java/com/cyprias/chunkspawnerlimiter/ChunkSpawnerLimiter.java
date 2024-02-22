package com.cyprias.chunkspawnerlimiter;


import com.alessiodp.libby.BukkitLibraryManager;
import com.alessiodp.libby.Library;
import com.cyprias.chunkspawnerlimiter.configs.BlocksConfig;
import com.cyprias.chunkspawnerlimiter.configs.CslConfig;
import com.cyprias.chunkspawnerlimiter.listeners.EntityListener;
import com.cyprias.chunkspawnerlimiter.listeners.PlaceBlockListener;
import com.cyprias.chunkspawnerlimiter.listeners.WorldListener;
import com.cyprias.chunkspawnerlimiter.messages.Debug;
import com.cyprias.chunkspawnerlimiter.utils.ChatUtil;
import com.cyprias.chunkspawnerlimiter.utils.ChunkUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ChunkSpawnerLimiter extends JavaPlugin {
    CslConfig cslConfig;
    BlocksConfig blocksConfig;

    @Override
    public void onLoad() {
        loadLibraries();
    }

    @Override
    public void onEnable() {
        initConfigs();

        ChatUtil.INSTANCE.init(this);
        ChunkUtil.INSTANCE.init(this);

        registerListeners();

        RegisterLib.INSTANCE.init(this);
        RegisterLib.INSTANCE.registerCommandsAndMetrics();
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }

    private void loadLibraries() {
        BukkitLibraryManager libraryManager = new BukkitLibraryManager(this);
        libraryManager.addMavenCentral();
        libraryManager.addRepository("https://repo.codemc.org/repository/maven-public");
        libraryManager.addRepository("https://repo.aikar.co/content/groups/aikar/");

        Library kotlinLibrary = Library.builder()
                .groupId("org.jetbrains.kotlin")
                .artifactId("kotlin-stdlib-jdk8")
                .version("1.9.22")
                .isolatedLoad(true)
                .build();

        Library acfLibrary = Library.builder()
                .groupId("co.aikar")
                .artifactId("acf-paper")
                .version("0.5.1-SNAPSHOT")
                .relocate("co.aikar","com{}cyprias{}chunkspawnerlimiter{}libs")
                .isolatedLoad(true)
                .build();

        Library bstatsLibrary = Library.builder()
                .groupId("org.bstats")
                .artifactId("bstats-bukkit")
                .version("3.0.2")
                .relocate("org{}bstats","com{}cyprias{}chunkspawnerlimiter{}libs")
                .isolatedLoad(true)
                .build();

        libraryManager.loadLibraries(kotlinLibrary, acfLibrary, bstatsLibrary);
    }

    private void initConfigs() {
        cslConfig = new CslConfig(this);
        cslConfig.saveDefaultConfig();
        blocksConfig = new BlocksConfig(this);
        blocksConfig.saveDefaultConfig();
    }

    public void reloadConfigs() {
        cslConfig.reloadConfig();
        blocksConfig.reloadConfig();
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new EntityListener(cslConfig), this);
        pm.registerEvents(new WorldListener(this), this);
        pm.registerEvents(new PlaceBlockListener(this), this);
        ChatUtil.INSTANCE.debug(Debug.REGISTER_LISTENERS);
    }

    public void cancelTask(int taskID) {
        Bukkit.getServer().getScheduler().cancelTask(taskID);
    }

    public CslConfig getCslConfig() {
        return cslConfig;
    }

    public BlocksConfig getBlocksConfig() {
        return blocksConfig;
    }
}


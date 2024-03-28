package zefir.mangelogs.config;

import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.comments.format.YamlCommentFormat;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.fabricmc.loader.api.FabricLoader;

public class ConfigManager {

    public static final Path MANGELOGS_DIR = FabricLoader.getInstance().getConfigDir().resolve("MangeLogs");
    private static String configReloadPermission;
    private static YamlFile yamlFile;

    public static void registerConfigs() {
        createAndLoadConfig();
    }

    private static void createAndLoadConfig() {
        if (!MANGELOGS_DIR.toFile().exists()) {
            MANGELOGS_DIR.toFile().mkdir();
        }

        Path configFile = MANGELOGS_DIR.resolve("config.yml");
        yamlFile = new YamlFile(configFile.toAbsolutePath().toString());

        try {
            yamlFile.createOrLoadWithComments();
            initializeConfigDefaults(yamlFile);
            yamlFile.loadWithComments();

            configReloadPermission = yamlFile.getString("permissions.reloadConfig");

        } catch (IOException e) {
            throw new RuntimeException("Failed to create or load configuration file", e);
        }
    }

    private static void initializeConfigDefaults(YamlFile yamlFile) {
        yamlFile.setCommentFormat(YamlCommentFormat.PRETTY);
        yamlFile.options().headerFormatter()
                .prefixFirst("######################")
                .commentPrefix("## ")
                .commentSuffix(" ##")
                .suffixLast("######################");

        yamlFile.setHeader("MangeLogs Config File");

        yamlFile.addDefault("permissions.reloadConfig", "mangelogs.reload");
        yamlFile.path("permissions").comment("Permission required to reload the config");

        yamlFile.path("logging").addDefault("BlockBreak", true);
        yamlFile.path("logging").addDefault("AttackBlock", true);
        yamlFile.path("logging").addDefault("AttackEntity", true);
        yamlFile.path("logging").addDefault("UseItem", true);
        yamlFile.path("logging").addDefault("EggsThrown", true);
        yamlFile.path("logging").addDefault("UseBlock", true);
        yamlFile.path("logging").addDefault("UseEntity", true);
        yamlFile.path("logging").addDefault("ItemPickup", true);
        yamlFile.path("logging").addDefault("ItemDropped", true);
        yamlFile.path("logging").addDefault("UIClick", true);
        yamlFile.path("logging").addDefault("ChatMessage", true);
        yamlFile.path("logging").addDefault("CommandExecution", true);
        yamlFile.path("logging").addDefault("ItemPlacedInFrame", true);
        yamlFile.path("logging").addDefault("ItemFrameItemRemoved", true);
        yamlFile.path("logging").addDefault("ItemBreak", true);
        yamlFile.path("logging").addDefault("ItemPickup", true);

        yamlFile.path("logging").addDefault("MinecartDestroyed", true);
        yamlFile.path("logging").addDefault("ItemDespawn", true);
        yamlFile.path("logging").addDefault("ItemDestroyed", true);
        yamlFile.path("logging").addDefault("MobPickupItem", true);

        loadEventLoggingConfig();
        try {
            yamlFile.save();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save default YAML configuration", e);
        }
    }

    private static final Map<String, Boolean> eventLoggingEnabled = new HashMap<>();

    public static boolean isLogEventEnabled(String eventName) {
        if (eventLoggingEnabled.isEmpty()) {
            loadEventLoggingConfig();
        }
        return eventLoggingEnabled.getOrDefault(eventName, true);
    }

    private static void loadEventLoggingConfig() {
        ConfigurationSection loggingSection = yamlFile.getConfigurationSection("logging");
        if (loggingSection != null) {
            for (String key : loggingSection.getKeys(false)) {
                eventLoggingEnabled.put(key, loggingSection.getBoolean(key));
            }
        }
    }
    public static String getConfigReloadPermission() {
        return configReloadPermission;
    }
}

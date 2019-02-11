package com.exonum.osgi.prototype;

import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;

public class DynamicModulesMain {

  // /home/dmitry/Documents/dynamic-services-makarov-proto/service-address/target/service-address-1.0-SNAPSHOT-plugin.jar /home/dmitry/Documents/dynamic-services-makarov-proto/service-user/target/service-user-1.0-SNAPSHOT-plugin.jar
  public static void main(String[] args) {
    List<Path> pluginPaths = Arrays.stream(args)
        .map(path -> Path.of(path))
        .collect(toList());

    PluginManager pluginManager = new DefaultPluginManager();

    List<String> loadedPlugins = loadPlugins(pluginManager, pluginPaths);

    startPlugins(pluginManager, loadedPlugins);

    stopPlugins(pluginManager, loadedPlugins);

    unloadPlugins(pluginManager, loadedPlugins);
  }

  /**
   * Loads the plugins by paths to their JAR-files.
   * @param pluginJars a list of plugins to load
   * @return a list of pluginIds of loaded plugins
   * @throws IllegalArgumentException if some plugins could not be loaded
   */
  private static List<String> loadPlugins(PluginManager pluginManager, List<Path> pluginJars) {
    List<String> loadedPlugins = new ArrayList<>(pluginJars.size());
    for (Path pluginJar : pluginJars) {
      System.out.println("\uD83D\uDE80 Loading " + pluginJar);

      String pluginId = pluginManager.loadPlugin(pluginJar);
      if (pluginId == null) {
        // Why don't PluginManager#loadPlugin throw itself? How could clients possibly know
        // why it failed to load a plugin?
        throw new IllegalArgumentException("Could not load plugin " + pluginJar);
      }

      System.out.println("\uD83D\uDE80 Loaded " + pluginId + " (" + pluginJar + ")");
      loadedPlugins.add(pluginId);
    }
    return loadedPlugins;
  }

  private static void startPlugins(PluginManager pluginManager, List<String> plugins) {
    System.out.println("Starting plugins: " + plugins);

    plugins.forEach(pluginManager::startPlugin);

    System.out.println("Started plugins: " + pluginManager.getStartedPlugins());
  }

  private static void stopPlugins(PluginManager pluginManager, List<String> plugins) {
    System.out.println("Stopping plugins " + plugins);

    plugins.stream()
        .peek(pluginId -> System.out.println("Stopping " + pluginId))
        .forEach(pluginManager::stopPlugin);
  }

  private static void unloadPlugins(PluginManager pluginManager, List<String> loadedPlugins) {
    System.out.println("Unloading plugins " + loadedPlugins);

    loadedPlugins.forEach(pluginManager::unloadPlugin);
  }

}
package com.exonum.osgi.prototype;

import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;

public class DynamicModulesMain {

  // /home/dmitry/Documents/dynamic-services-makarov-proto/service-address/target/service-address-1.0-SNAPSHOT-plugin.jar /home/dmitry/Documents/dynamic-services-makarov-proto/service-user/target/service-user-1.0-SNAPSHOT-plugin.jar
  public static void main(String[] args) throws InterruptedException {
    int numPlugins = 128;
    String pathTemplate = "/home/dmitry/Documents/dynamic-services-makarov-proto/service-address-plugin-repo/address-service-%s-0.1.0-plugin.jar";

    // Sleep to give some time to connect from JFR
    System.out.println("Connect now!");
    Thread.sleep(30 * 1000);

    var pluginPaths = IntStream.rangeClosed(1, numPlugins)
        .mapToObj(i -> String.format(pathTemplate, i))
        .map(path -> Path.of(path))
        .collect(toList());

    // Sleep some more to see class loading in more detail
    Thread.sleep(5 * 1000);

    PluginManager pluginManager = new DefaultPluginManager();

    for (int i = 0; ; i++) {
      // Load and start plugins one by one
      System.out.println("\uD83D\uDE80 Loading plugins i=" + i);

      var loadedPlugins = new ArrayList<String>(numPlugins);
      for (var pluginPath : pluginPaths) {
        // Load a plugin
        System.out.println("\uD83D\uDE80 Loading " + pluginPath);

        String pluginId = pluginManager.loadPlugin(pluginPath);
        if (pluginId == null) {
          // Why don't PluginManager#loadPlugin throw itself? How could clients possibly know
          // why it failed to load a plugin?
          throw new IllegalArgumentException("Could not load plugin " + pluginPath);
        }

        System.out.println("\uD83D\uDE80 Loaded " + pluginId + " (" + pluginPath + ")");

        // Register it
        loadedPlugins.add(pluginId);

        // Start it
        System.out.println("\uD83D\uDE80 Starting " + pluginId);
        pluginManager.startPlugin(pluginId);

        // Sleep for a while
        Thread.sleep(100);
      }

      stopPlugins(pluginManager, loadedPlugins);

      unloadPlugins(pluginManager, loadedPlugins);
      System.out.println("\uD83D\uDE80 Unloaded plugins i=" + i);
      Thread.sleep(500);
    }
  }

  private static void stopPlugins(PluginManager pluginManager, List<String> plugins) {
    System.out.println("\uD83D\uDE80 Stopping plugins " + plugins);

    plugins.stream()
        .peek(pluginId -> System.out.println("\uD83D\uDE80 Stopping " + pluginId))
        .forEach(pluginManager::stopPlugin);
  }

  private static void unloadPlugins(PluginManager pluginManager, List<String> loadedPlugins)
      throws InterruptedException {
    System.out.println("\uD83D\uDE80 Unloading plugins " + loadedPlugins);

    for (String pluginId : loadedPlugins) {
      System.out.println("\uD83D\uDE80 Stopping " + pluginId);
      pluginManager.unloadPlugin(pluginId);
      // No need to sleep as no immediate GC will happen
//      Thread.sleep(100);
    }
  }

}
package com.exonum.osgi.prototype;

import static java.util.stream.Collectors.toList;

import com.exonum.osgi.prototype.service.AddressService;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;

public class DynamicModulesMain {

  /**
   * Launch with two address-services.
   */
  public static void main(String[] args) throws InterruptedException {

    Thread.sleep(20 * 1000);

    List<Path> pluginPaths = Arrays.stream(args)
        .map(path -> Path.of(path))
        .collect(toList());

    PluginManager pluginManager = new DefaultPluginManager();

    List<String> loadedPlugins = loadPlugins(pluginManager, pluginPaths);

    startPlugins(pluginManager, loadedPlugins);

    // All plugins are assumed to be address-service
    var firstId = loadedPlugins.get(0);
    var secondId = loadedPlugins.get(1);

    var firstService = getRequiredExtension(pluginManager, firstId);
    var secondService = getRequiredExtension(pluginManager, secondId);

    checkAllEqual(firstService.getSystemClass(), secondService.getSystemClass(), System.class);

    stopPlugins(pluginManager, loadedPlugins);

    unloadPlugins(pluginManager, loadedPlugins);

for (int i = 0; i < 8; i++) {    System.gc(); Thread.sleep(50); }
    Thread.sleep(20 * 1000);
  }

  private static AddressService getRequiredExtension(PluginManager pluginManager, String pluginId) {
    return pluginManager.getExtensions(AddressService.class, pluginId).get(0);
  }

  /**
   * Check that all classes are the same (i.e., there is no reported by JMC 'multiple'
   * loaded classes).
   *
   * Even for such simple scenario, JMC reports the following:
   *
   * Top 5 loaded classes:
   * java.lang.Object (8)
   * java.lang.System (7) <-- ?
   * java.lang.Class (6)
   * java.util.Map (6)
   * java.util.HashMap (6)
   */
  @SafeVarargs
  private static void checkAllEqual(Class<? extends System>... classes) {
    Class<? extends System> first = classes[0];
    for (var nextClass : classes) {
      if (first != nextClass) {
        throw new AssertionError(first + " (cl = " + first.getClassLoader() + ") != " + nextClass
         + "(cl = " + nextClass.getClassLoader() + ")");
      }
    }
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
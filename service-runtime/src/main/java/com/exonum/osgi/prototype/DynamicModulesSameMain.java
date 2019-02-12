package com.exonum.osgi.prototype;

import java.nio.file.Path;
import org.pf4j.DefaultPluginManager;
import org.pf4j.JarPluginLoader;
import org.pf4j.ManifestPluginDescriptorFinder;
import org.pf4j.PluginDescriptorFinder;
import org.pf4j.PluginLoader;
import org.pf4j.PluginManager;

/**
 * Loads the same module (plugin) over and over again to see if any leaks occur in such a scenario.
 *
 * <p>Launch with a strict memory limit (e.g., {@code -Xmx40m} to limit the heap to 40 megabytes).
 */
public class DynamicModulesSameMain {

  public static void main(String[] args) throws InterruptedException {
    // Give us some time to connect interactively
    Thread.sleep(20 * 1000);

    var modulePath = Path.of(args[0]);

    var pluginManager = createJarPluginManager();

    for (int i = 1; ; i++) {
      System.out.println(String.format("\uD83D\uDE80 Loading plugin i=%d, path=%s", i, modulePath));

      String pluginId = loadPluginChecked(modulePath, pluginManager);

      pluginManager.startPlugin(pluginId);

      pluginManager.stopPlugin(pluginId);

      pluginManager.unloadPlugin(pluginId);

      System.out.println(String.format("\uD83D\uDE80 Unloaded plugin i=%d, id=%s", i, pluginId));

      Thread.sleep(25);
    }
  }

  private static PluginManager createJarPluginManager() {
    return new DefaultPluginManager() {

      @Override
      protected PluginLoader createPluginLoader() {
        // load only jar plugins
        return new JarPluginLoader(this);
      }

      @Override
      protected PluginDescriptorFinder createPluginDescriptorFinder() {
        // read plugin descriptor from jar's manifest
        return new ManifestPluginDescriptorFinder();
      }
    };
  }

  private static String loadPluginChecked(Path modulePath, PluginManager pluginManager) {
    String pluginId = pluginManager.loadPlugin(modulePath);
    if (pluginId == null) {
      throw new IllegalArgumentException("Can't load plugin " + modulePath
          + ", active=" + pluginManager.getPlugins());
    }
    return pluginId;
  }
}

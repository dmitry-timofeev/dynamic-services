package com.exonum.osgi.prototype;

import com.exonum.osgi.prototype.service.AddressService;
import com.exonum.osgi.prototype.service.ExtensionModule;
import com.exonum.osgi.prototype.service.SomeFrameworkApi;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import java.nio.file.Path;
import org.pf4j.DefaultPluginManager;

public class DynamicGuiceModulesMain {

  /**
   * Loads/unloads the same plugin in a loop to check for possible leaks when Guice is involved.
   *
   * @param args a path to the plugin JAR to load
   */
  public static void main(String[] args) throws InterruptedException {
    Thread.sleep(5 * 1000);
    // Create the framework injector
    Injector frameworkInjector = Guice.createInjector(new FrameworkModule());
    // Test it
    var foo = frameworkInjector.getInstance(SomeFrameworkApi.class);
    foo.foo();

    Path pluginPath = Path.of(args[0]);

    var pluginManager = new DefaultPluginManager();

    for (int i = 0; ; i++) {
      // Load and start the plugin
      String pluginId = pluginManager.loadPlugin(pluginPath);
      pluginManager.startPlugin(pluginId);

      // Get its module
      var allPluginModules = pluginManager.getExtensions(ExtensionModule.class, pluginId);
      var pluginModule = allPluginModules.get(0);

      // Create the module injector
      Injector moduleInjector = frameworkInjector.createChildInjector(pluginModule);

      // Check it resolves both in-module and framework services, required by AddressService
      var addressService = moduleInjector.getInstance(AddressService.class);

      System.out.println("An address of Oleg: " + addressService.getAddress("Oleg"));

      // Unload the plugin
      pluginManager.stopPlugin(pluginId);
      pluginManager.unloadPlugin(pluginId);

      System.out.println("\uD83D\uDE80 Unloaded plugin, i=" + i);
      Thread.sleep(250);
    }
  }

}

class FrameworkModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(SomeFrameworkApi.class).to(SomeFrameworkImpl.class);
  }
}

class SomeFrameworkImpl implements SomeFrameworkApi {

  @Override
  public void foo() {
    System.out.println(this + " foo");
  }
}

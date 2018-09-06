package com.exonum.osgi.prototype;

import com.exonum.osgi.prototype.service.AddressService;
import com.exonum.osgi.prototype.service.UserService;
import java.util.List;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;

public class Pf4JTestMain {
  public static void main(String[] args) {
    // create the plugin manager
    PluginManager pluginManager = new DefaultPluginManager();

    // start and load all plugins of application
    pluginManager.loadPlugins();
    pluginManager.startPlugins();

    // print extensions instances for each started plugin
    //for (PluginWrapper plugin : pluginManager.getPlugins()) {
    //  String pluginId = plugin.getDescriptor().getPluginId();
    //  System.out.println(String.format("Extensions instances added by plugin '%s':", pluginId));
    //
    //  for (Object extension : pluginManager.getExtensions(pluginId)) {
    //    System.out.println("   " + extension);
    //  }
    //}

    // retrieve all extensions for "UserService" extension point
    System.out.println(" Trying to execute extension logic");
    List<UserService> userServices = pluginManager.getExtensions(UserService.class);
    // by default we should iterate over list of extension points, simplified here
    UserService userService = userServices.get(0);

    List<AddressService> addressServices = pluginManager.getExtensions(AddressService.class);
    userService.setAddressService(addressServices.get(0));

    userService.getUserDetails("Denys");
    userService.getUserAddress("Denys");

    // stop and unload all plugins
    pluginManager.stopPlugins();
  }
}

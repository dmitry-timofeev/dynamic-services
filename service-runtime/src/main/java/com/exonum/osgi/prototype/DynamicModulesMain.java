package com.exonum.osgi.prototype;

import com.exonum.osgi.prototype.service.UserService;
import java.io.Console;
import org.jboss.modules.ModuleLoadException;

public class DynamicModulesMain {

  public static void main(String[] args) throws ModuleLoadException {
    String basePath;
    if (args.length != 1) {
      basePath = "";
    } else {
      basePath = args[0];
    }

    UserService userService = null;

    boolean active = true;
    while (active) {
      Console console = System.console();
      String command = console.readLine("command: ");

      String moduleName;
      switch (command) {
        case "load":
          moduleName = console.readLine("module name: ");
          System.out.println("Loading module: " + moduleName);
          //loader.loadModule(moduleName, null);
          continue;
        case "unload":
          moduleName = console.readLine("module name: ");
          System.out.println("UnLoading module: " + moduleName);
          continue;
        case "exec":
          String userName = console.readLine("user name: ");
          System.out.println("Executing logic.");
          userService.getUserDetails(userName);
          continue;
        case "exit":
          System.out.println("Exiting application");
          active = false;
          continue;
        default:
          System.out.println("Unknown command. Use load, unload, exit commands");
      }
    }

  }

}
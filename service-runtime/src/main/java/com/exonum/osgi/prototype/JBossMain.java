package com.exonum.osgi.prototype;

import com.exonum.osgi.prototype.loader.DynamicModuleLoader;
import com.exonum.osgi.prototype.service.UserService;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.jboss.modules.FileSystemClassPathModuleFinder;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleClassLoader;
import org.jboss.modules.ModuleFinder;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.ModuleSpec;
import org.jboss.modules.PathUtils;

public class JBossMain {

  public static void main(String[] args)
      throws ModuleLoadException, ClassNotFoundException, IllegalAccessException,
      InstantiationException {

    UserService userService = null;

    // Trying to use custom module finder
    DynamicModuleLoader loader = new DynamicModuleLoader(Paths.get("baseModulesDir"));
    ModuleSpec moduleSpec = loader.loadModule("service-address", null);
    ModuleSpec moduleSpec1 = loader.loadModule("service-user", null);



    // Prepearing relative path to module
    String name = PathUtils.relativize(PathUtils.canonicalize("service-user"));
    Path jarPath = Paths.get("baseModulesDir").resolve(name + ".jar");

    // Trying to load module with default finder and loader
    ModuleLoader mloader = Module.getBootModuleLoader();
    //mloader.loadModule("service-user");

    FileSystemClassPathModuleFinder fileSystemClassPathModuleFinder = new FileSystemClassPathModuleFinder(
        mloader);
    ModuleSpec module = fileSystemClassPathModuleFinder.findModule("/Users/denysmakarov/Projects/dynamic-services/baseModulesDir/service-user.jar", null);

    ModuleLoader nloader = new ModuleLoader(new ModuleFinder[]{fileSystemClassPathModuleFinder});
    Module module1 = nloader.loadModule(module.getName());

    // Module is loaded but have no classes inside - no idea how to fix  as for now
    ModuleClassLoader classLoader = module1.getClassLoader();

    Class<?> userService1 = classLoader.loadClassLocal("UserServiceImpl");

    Class<? extends UserService> aClass = userService1.asSubclass(UserService.class);

    UserService userService2 = aClass.newInstance();

    userService2.getUserDetails("Denys");

    System.out.println(module.getName());
    System.out.println(module1);
  }

}
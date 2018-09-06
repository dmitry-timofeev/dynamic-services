package com.exonum.osgi.prototype.loader;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import org.jboss.modules.DependencySpec;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleDependencySpecBuilder;
import org.jboss.modules.ModuleFinder;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleSpec;
import org.jboss.modules.PathUtils;
import org.jboss.modules.ResourceLoaderSpec;
import org.jboss.modules.ResourceLoaders;

public class DynamicModuleLoader implements ModuleFinder {
  private final Path basePath;

  public DynamicModuleLoader(Path basePath) {
    if (basePath == null) throw new IllegalArgumentException("null basePath");
    this.basePath = basePath;
  }

  public ModuleSpec loadModule(String name, DynamicModuleLoader delegateLoader)
      throws ModuleLoadException
  {
    // Make sure nobody escapes using a .. in the plugin name
    name = PathUtils.relativize(PathUtils.canonicalize(name));
    Path jarPath = basePath.resolve(name + ".jar");
    if (Files.exists(jarPath)) {
      ModuleSpec.Builder builder = ModuleSpec.build(name);
      // Add all JDK classes
      builder.addDependency(
          DependencySpec.createSystemDependencySpec(
              PathUtils.getPathSet(null)
          )
      );
      //// Add the module's own content
      //builder.addDependency(DependencySpec.OWN_DEPENDENCY);
      //// Add my own module as a dependency
      //final Module myModule = Module.forClass(getClass());
      //builder.addDependency(
      //    new ModuleDependencySpecBuilder()
      //        .setModuleLoader(myModule.getModuleLoader())
      //        .setName(myModule.getName())
      //        .build()
      //);
      // Add the module JAR content
      URI uri = URI.create("jar:" + jarPath.toUri());
      FileSystem fs;
      try {
        fs = FileSystems.newFileSystem(uri, Collections.emptyMap());
      } catch (IOException e) {
        throw new ModuleLoadException(e);
      }
      final Path rootPath = fs.getRootDirectories().iterator().next();
      builder.addResourceRoot(
          ResourceLoaderSpec.createResourceLoaderSpec(
              ResourceLoaders.createPathResourceLoader("root", rootPath)
          )
      );
      ModuleSpec moduleSpec = builder.create();
      return moduleSpec;
    }
    return null;
  }
}

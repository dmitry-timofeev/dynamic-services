Dynamic services examples

1. OSGI
2. JBoss
3. pf4j

Compiled java modules *.jars should be placed to baseModulesDir

Pf4J implementation build and run:

1. Build modules(plugins)
```$sh
$ mvn clean install
```

2. Copy plugins to plugins dir
```$sh
$ cp service-address/target/service-address-1.0-SNAPSHOT-plugin.jar plugins
$ cp service-user/target/service-user-1.0-SNAPSHOT-plugin.jar plugins
```


3. Run main test class
com.exonum.osgi.prototype.Pf4JTestMain
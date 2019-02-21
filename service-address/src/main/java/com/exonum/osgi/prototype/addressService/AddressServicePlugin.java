package com.exonum.osgi.prototype.addressService;

import com.exonum.osgi.prototype.model.Address;
import com.exonum.osgi.prototype.service.AddressService;
import com.exonum.osgi.prototype.service.SomeFrameworkApi;
import com.google.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import org.pf4j.Plugin;
import org.pf4j.PluginException;
import org.pf4j.PluginWrapper;

public class AddressServicePlugin extends Plugin {
  public AddressServicePlugin(PluginWrapper wrapper) {
    super(wrapper);
  }

  @Override
  public void start() throws PluginException {
    System.out.println("AddressService plugin started");
    super.start();
  }

  @Override
  public void stop() throws PluginException {
    System.out.println("AddressService plugin stopped");
    super.stop();
  }

  public static class AddressServiceImpl implements AddressService {
    private static Map<String, Address> database = new HashMap<>();

    static {
      database.put("Denys", new Address("Antonovicha", 158));
      database.put("Oleg", new Address("Bazhana", 34));
      database.put("Ira", new Address("Valilkivska", 45));
    }

    private final SomeInternalPluginApi api;
    private final SomeFrameworkApi frameworkApi;

    @Inject
    public AddressServiceImpl(SomeInternalPluginApi internal, SomeFrameworkApi frameworkApi) {
      this.api = internal;
      this.frameworkApi = frameworkApi;
      internal.bar();
      frameworkApi.foo();
    }

    public Address getAddress(String name) {
      return database.get(name);
    }
  }
}

interface SomeInternalPluginApi {
  void bar();
}

class SomeInternalImpl implements SomeInternalPluginApi {

  @Override
  public void bar() {
    System.out.println(this + " bar");
  }
}

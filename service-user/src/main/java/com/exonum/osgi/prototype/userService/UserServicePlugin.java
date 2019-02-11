package com.exonum.osgi.prototype.userService;

import com.exonum.osgi.prototype.model.User;
import com.exonum.osgi.prototype.service.AddressService;
import com.exonum.osgi.prototype.service.UserService;
import java.util.HashMap;
import java.util.Map;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginException;
import org.pf4j.PluginWrapper;

public class UserServicePlugin extends Plugin {

  public UserServicePlugin(PluginWrapper wrapper) {
    super(wrapper);
  }

  @Override
  public void start() throws PluginException {
    System.out.println("UserService plugin started");
    super.start();
  }

  @Override
  public void stop() throws PluginException {
    System.out.println("UserService plugin stopped");
    super.stop();
  }

  @Extension
  public static class UserServiceImpl implements UserService {
    private AddressService addressService;
    private static Map<String, User> users = new HashMap<>();

    static {
      users.put("Denys", new User("Denys", "Makarov"));
      users.put("Oleg", new User("Oleg", "Petrov"));
      users.put("Ira", new User("Ira", "Ivanova"));
    }

    public void setAddressService(AddressService addressService) {
      this.addressService = addressService;
    }

    public AddressService getAddressService() {
      return addressService;
    }

    public void getUserDetails(String name) {
      System.out.println("User firstName: " + users.get(name).firstName + ", lastName: "
          + users.get(name).lastName);
    }

    public void getUserAddress(String name) {
      com.exonum.osgi.prototype.model.Address address = addressService.getAddress(name);
      System.out.println("User address: " + address.street + ", building num: " + address.buildingNumber);
    }
  }
}

package com.exonum.osgi.prototype.userService;

import com.exonum.osgi.prototype.model.Address;
import com.exonum.osgi.prototype.model.User;
import com.exonum.osgi.prototype.service.AddressService;
import com.exonum.osgi.prototype.service.UserService;
import java.util.Map;

public class UserServiceImpl implements UserService {
  private AddressService addressService;
  private static Map<String, User> users;

  static {
    users.put("Denys", new User("Denys", "Makarov"));
    users.put("Oleg", new User("Oleg", "Petrov"));
    users.put("Ira", new User("Ira", "Ivanova"));
  }

  public void getUserDetails(String name) {
    System.out.println("User firstName: " + users.get(name).firstName + ", lastName: "
        + users.get(name).lastName);
  }

  public Address getUserAddress(String name) {
    return addressService.getAddress(name);
  }
}

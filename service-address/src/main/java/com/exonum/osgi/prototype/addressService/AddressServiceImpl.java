package com.exonum.osgi.prototype.addressService;

import com.exonum.osgi.prototype.model.Address;
import com.exonum.osgi.prototype.service.AddressService;
import java.util.Map;

public class AddressServiceImpl implements AddressService {
  private static Map<String, Address> database;

  static {
    database.put("Denys", new Address("Antonovicha", 158));
    database.put("Oleg", new Address("Bazhana", 34));
    database.put("Ira", new Address("Valilkivska", 45));
  }

  public Address getAddress(String name) {
    return database.get(name);
  }
}

package com.exonum.osgi.prototype.service;

import org.pf4j.ExtensionPoint;

public interface UserService extends ExtensionPoint {
  void getUserDetails(String name);
  void getUserAddress(String name);
  void setAddressService(AddressService addressService);
  AddressService getAddressService();
}

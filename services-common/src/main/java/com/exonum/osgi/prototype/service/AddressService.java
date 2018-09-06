package com.exonum.osgi.prototype.service;

import com.exonum.osgi.prototype.model.Address;
import com.exonum.osgi.prototype.model.User;
import org.pf4j.ExtensionPoint;

public interface AddressService extends ExtensionPoint {
  Address getAddress(String name);
}

package com.exonum.osgi.prototype.service;

import com.exonum.osgi.prototype.model.Address;
import com.exonum.osgi.prototype.model.User;

public interface AddressService {
  Address getAddress(String name);
}

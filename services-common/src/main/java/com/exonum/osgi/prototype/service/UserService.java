package com.exonum.osgi.prototype.service;

import com.exonum.osgi.prototype.model.Address;
import com.exonum.osgi.prototype.model.User;

public interface UserService {
  void getUserDetails(String name);
  Address getUserAddress(String name);
}

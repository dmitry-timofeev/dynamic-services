package com.exonum.osgi.prototype.service;

import com.exonum.osgi.prototype.model.Address;
import org.pf4j.ExtensionPoint;

public interface AddressService extends ExtensionPoint {
  Address getAddress(String name);

  /**
   * Returns the class of System object to verify that system classes are indeed loaded
   * by the bootstrap classloader in different services.
   */
  Class<? extends System> getSystemClass();
}

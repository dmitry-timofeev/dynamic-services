package com.exonum.osgi.prototype.addressService;

import com.exonum.osgi.prototype.addressService.AddressServicePlugin.AddressServiceImpl;
import com.exonum.osgi.prototype.service.AddressService;
import com.exonum.osgi.prototype.service.ExtensionModule;
import com.google.inject.AbstractModule;
import org.pf4j.Extension;

@Extension
public class PluginModule extends AbstractModule implements ExtensionModule {

  @Override
  protected void configure() {
    bind(AddressService.class).to(AddressServiceImpl.class);
    bind(SomeInternalPluginApi.class).to(SomeInternalImpl.class);
  }
}

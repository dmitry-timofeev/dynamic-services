package com.exonum.osgi.prototype.service;

import org.pf4j.ExtensionPoint;

// Unfortunately, an extension *must* implement ExtensionPoint (we can't just 'provide' com.google.inject.Module).
public interface ExtensionModule extends ExtensionPoint, com.google.inject.Module {
}

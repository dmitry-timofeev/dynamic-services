package com.exonum.osgi.prototype.model;

public class Address {
  public String street;
  public int buildingNumber;

  public Address(String street, int buildingNumber) {
    this.street = street;
    this.buildingNumber = buildingNumber;
  }

  @Override
  public String toString() {
    return "Address{" +
        "street='" + street + '\'' +
        ", buildingNumber=" + buildingNumber +
        '}';
  }
}

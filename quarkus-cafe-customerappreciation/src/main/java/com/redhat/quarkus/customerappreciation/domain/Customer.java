package com.redhat.quarkus.customerappreciation.domain;

public class Customer {

    public String name;

    public CustomerStatus customerStatus;

    public Customer(String name, CustomerStatus customerStatus) {
        this.name = name;
        this.customerStatus = customerStatus;
    }

    @Override
    public boolean equals(Object obj) {
        Customer otherCustomer = (Customer) obj;
        return this.name.equals(otherCustomer.name);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Customer[name=")
                .append(name)
                .append(",customerStatus=")
                .append(customerStatus.toString())
                .append("]")
                .toString();
    }
}


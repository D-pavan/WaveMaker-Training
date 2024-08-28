package com.employeeservices.repository;

import com.employeeservices.models.Address;

import java.util.HashMap;
import java.util.Map;

public class AddressInMemoryRepository {
    private static AddressInMemoryRepository addressInMemoryRepository;
    private final Map<Integer, Address> address;

    private AddressInMemoryRepository() {
        this.address = new HashMap<>();
    }

    public static synchronized AddressInMemoryRepository getInstance() {
        if (addressInMemoryRepository == null) {
            addressInMemoryRepository = new AddressInMemoryRepository();
        }
        return addressInMemoryRepository;
    }

    public Map<Integer, Address> getAddress() {
        return this.address;

    }
}

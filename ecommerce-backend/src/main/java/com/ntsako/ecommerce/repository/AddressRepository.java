package com.ntsako.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ntsako.ecommerce.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{

}

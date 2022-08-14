package com.example.bookyourshow.repository;

import com.example.bookyourshow.entity.Buyer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {
        Optional<Buyer> findByPhoneNo(String phoneNo);
}

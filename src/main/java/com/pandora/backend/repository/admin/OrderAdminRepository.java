package com.pandora.backend.repository.admin;

import com.pandora.backend.model.entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderAdminRepository extends PagingAndSortingRepository<Order, Long> {
    List<Order> findTop8ByOrderByDateDesc();

    @Query(value = "SELECT * FROM orders WHERE user_id=?1", nativeQuery = true)
    List<Order> findByUserId(Long userId);
}

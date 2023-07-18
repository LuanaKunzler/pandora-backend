package com.pandora.backend.repository.admin;

import com.pandora.backend.model.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailsAdminRepository extends JpaRepository<OrderDetail, Long> {

}

package com.pandora.backend.repository.admin;

import com.pandora.backend.model.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReportAdminRepository extends JpaRepository<Sale, Long> {
    @Query(value = "SELECT b.id AS book_id, b.title, b.isbn, SUM(od.amount) AS quantity_sold, b.unit_price AS unit_price, ROUND(SUM(od.amount * b.unit_price), 2) AS total_price " +
            "FROM book b " +
            "INNER JOIN order_detail od ON od.book_id = b.id " +
            "INNER JOIN orders o ON o.id = od.order_id " +
            "WHERE o.date >= :?1 AND o.date <= :?2 " +
            "GROUP BY b.id, b.title, b.isbn, b.unit_price", nativeQuery = true)
    List<Sale> findSalesByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}

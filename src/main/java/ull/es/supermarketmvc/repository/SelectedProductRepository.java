package ull.es.supermarketmvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ull.es.supermarketmvc.model.SelectedProduct;

import java.util.List;

public interface SelectedProductRepository extends JpaRepository<SelectedProduct, Long> {

    @Query("SELECT sp, s.name as supermarketName, p.name as productName " +
            "FROM SelectedProduct sp " +
            "JOIN sp.supermarket s " +
            "JOIN sp.product p")
    List<Object[]> getSelectedProductsWithNames();

    @Modifying
    @Transactional
    @Query("DELETE FROM SelectedProduct sp WHERE sp.supermarket.id = :supermarketId")
    void deleteBySupermarketId(@Param("supermarketId") Long supermarketId);
}
package ull.es.supermarketmvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ull.es.supermarketmvc.model.PriceHistory;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {

    List<PriceHistory> findByProductId(Long productId);

    @Query("SELECT ph FROM PriceHistory ph " +
            "WHERE ph.product.id IN :productIds " +
            "AND DATE(ph.readAt) = :specificDate " +
            "AND ph.price = (SELECT MAX(ph2.price) FROM PriceHistory ph2 " +
            "WHERE DATE(ph2.readAt) = :specificDate " +
            "AND ph2.product.id = ph.product.id)")
    List<PriceHistory> findMaxPricesForProductsOnDate(
            @Param("productIds") List<Long> productIds,
            @Param("specificDate") LocalDate specificDate
    );

    @Query("SELECT ph FROM PriceHistory ph " +
            "WHERE ph.product.id IN :productIds " +
            "AND DATE(ph.readAt) = :specificDate " +
            "AND ph.price = (SELECT MIN(ph2.price) FROM PriceHistory ph2 " +
            "WHERE DATE(ph2.readAt) = :specificDate " +
            "AND ph2.product.id = ph.product.id)")
    List<PriceHistory> findMinPricesForProductsOnDate(
            @Param("productIds") List<Long> productIds,
            @Param("specificDate") LocalDate specificDate
    );

//    @Query("SELECT MIN(DATE(ph.readAt)) FROM PriceHistory ph")
//    LocalDate findMinDate();

    @Query("SELECT MIN(ph.readAt) FROM PriceHistory ph")
    LocalDate findMinDate();


//    @Query("SELECT MAX(DATE(ph.readAt)) FROM PriceHistory ph")
//    LocalDate findMaxDate();

    @Query("SELECT MAX(ph.readAt) FROM PriceHistory ph")
    LocalDate findMaxDate();


//    @Query("SELECT MAX(DATE(ph.readAt)) FROM PriceHistory ph " +
//            "WHERE ph.product.id IN :productIds")
//    LocalDate findMaxDateByProductIds(@Param("productIds") List<Long> productIds);

    @Query("SELECT MAX(ph.readAt) FROM PriceHistory ph " +
            "WHERE ph.product.id IN :productIds")
    LocalDate findMaxDateByProductIds(@Param("productIds") List<Long> productIds);


}
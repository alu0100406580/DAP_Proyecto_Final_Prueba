package ull.es.supermarketmvc.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ull.es.supermarketmvc.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
        // Puedes agregar consultas personalizadas aquí si es necesario
        @Query("SELECT p FROM Product p WHERE p.idSupermarket = :idSupermarket")
        List<Product> findProductBySupermarketId(@Param("idSupermarket") int idSupermarket);
        @Query("SELECT p FROM Product p WHERE p.idSupermarket = :idSupermarket")
        List<Product> findProductBySupermarketIdPageable(@Param("idSupermarket") int idSupermarket, Pageable pageable);

        @Query("SELECT p.id FROM Product p WHERE p.idSupermarket = :idSupermarket")
        List<Long> findIdBySupermarket(@Param("idSupermarket") int idSupermarket);
}
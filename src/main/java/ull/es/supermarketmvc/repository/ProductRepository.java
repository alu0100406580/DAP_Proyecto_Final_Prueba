package ull.es.supermarketmvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ull.es.supermarketmvc.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
        // Puedes agregar consultas personalizadas aquí si es necesario
        @Query("SELECT p FROM Product p WHERE p.idSupermarket = :idSupermarket")
        List<Product> findBySupermarketId(@Param("idSupermarket") int idSupermarket);
}
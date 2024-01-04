package ull.es.supermarketmvc.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ull.es.supermarketmvc.model.Product;
import ull.es.supermarketmvc.model.Supermarket;

import java.util.List;

public interface SupermarketRepository extends JpaRepository<Supermarket, Long> {

}
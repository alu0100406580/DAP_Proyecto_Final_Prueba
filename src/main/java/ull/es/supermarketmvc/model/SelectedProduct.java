package ull.es.supermarketmvc.model;

import jakarta.persistence.*;


@Entity
@Table(name = "SelectedProducts")
public class SelectedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_supermarket", nullable = false)
    private Long idSupermarket;

    @Column(name = "id_product", nullable = false)
    private Long idProduct;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;


    public SelectedProduct() {}

    public SelectedProduct(Long idSupermarket, Long idProduct, Integer quantity) {
        this.idSupermarket = idSupermarket;
        this.idProduct = idProduct;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdSupermarket() {
        return idSupermarket;
    }

    public void setIdSupermarket(Long idSupermarket) {
        this.idSupermarket = idSupermarket;
    }

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}


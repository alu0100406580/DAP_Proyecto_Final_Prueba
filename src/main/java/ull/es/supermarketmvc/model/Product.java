package ull.es.supermarketmvc.model;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Products")
public class Product {

    @OneToMany(mappedBy = "product")
    private List<SelectedProduct> selectedProducts;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_by_supermarket")
    private Long idBySupermarket;
    @Column(name = "id_supermarket")
    private Long idSupermarket;
    private String name;
    private Double price;
    private String imageUrl;
    private String urlProduct;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdBySupermarket() {
        return idBySupermarket;
    }

    public void setIdBySupermarket(Long idBySupermarket) {
        this.idBySupermarket = idBySupermarket;
    }

    public Long getIdSupermarket() {
        return idSupermarket;
    }

    public void setIdSupermarket(Long idSupermarket) {
        this.idSupermarket = idSupermarket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrlProduct() {
        return urlProduct;
    }

    public void setUrlProduct(String urlProduct) {
        this.urlProduct = urlProduct;
    }
}
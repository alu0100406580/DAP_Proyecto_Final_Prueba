package ull.es.supermarketmvc.model;

import jakarta.persistence.*;


@Entity
@Table(name = "SelectedProducts")
public class SelectedProduct {

    @ManyToOne
    @JoinColumn(name = "id_supermarket", referencedColumnName = "id")
    private Supermarket supermarket;

    @ManyToOne
    @JoinColumn(name = "id_product", referencedColumnName = "id")
    private Product product;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "id_supermarket", nullable = false)
//    private Long idSupermarket;
//
//    @Column(name = "id_product", nullable = false)
//    private Long idProduct;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public Supermarket getSupermarket() {
        return supermarket;
    }

    public void setSupermarket(Supermarket supermarket) {
        this.supermarket = supermarket;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public SelectedProduct() {
        this.supermarket = new Supermarket();
        this.product = new Product();
    }

    // Constructor
    public SelectedProduct(Long idSupermarket, Long idProduct, Integer quantity) {
        this.quantity = quantity;
        this.supermarket = new Supermarket();
        this.supermarket.setId(idSupermarket);

        this.product = new Product();
        this.product.setId(idProduct);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdSupermarket() {
        return this.supermarket.getId();
    }

    public void setIdSupermarket(Long idSupermarket) {
        this.supermarket.setId(idSupermarket);
    }

    public Long getIdProduct() {
        return this.product.getId();
    }

    public void setIdProduct(Long idProduct) {
        this.product.setId(idProduct);
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}


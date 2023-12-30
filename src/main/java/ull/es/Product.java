package ull.es;

// Clase Product
public class Product {

    private int id;
    private String name;
    private double price;
    private String imageUrl;
    private String urlProduct;

    public Product(String name, Double price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }
    public Product(int id, String name, Double price, String imageUrl, String urlProduct) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.urlProduct = urlProduct;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", urlProduct='" + urlProduct + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUrlProduct() {
        return urlProduct;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setUrlProduct(String urlProduct) {
        this.urlProduct = urlProduct;
    }
}

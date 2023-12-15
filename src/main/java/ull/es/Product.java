package ull.es;

// Clase Product
public class Product {

    private int id;
    private String name;
    private String price;
    private String imageUrl;
    private String urlProduct;

    public Product(String name, String price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }
    public Product(int id, String name, String price, String imageUrl, String urlProduct) {
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
}

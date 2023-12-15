package ull.es;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;


public class MarketDonnaCrawler {
    String url = "https://tienda.mercadona.es/api/categories/";

    List<Product> productList;
    MarketDonnaCrawler() {
            this.productList = new ArrayList<Product>();
            crawlIds();
    }
    public void crawlIds() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(new URL(this.url));
            for (JsonNode category : jsonNode.get("results")) {
                int categoryId = category.get("id").asInt();
                crawlProducts(categoryId);
            }
            System.out.println("g");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void crawlProducts(int idCategory) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(new URL(this.url + "/" + idCategory));
            for (JsonNode category : jsonNode.get("categories")) {
                for(JsonNode product : category.get("products")) {
                    int productId = product.get("id").asInt();
                    String nameProduct = product.get("display_name").asText();
                    String price = product.get("price_instructions").get("unit_price").asText();
                    String urlImage = product.get("thumbnail").asText();
                    String urlProduct = "";
                    if(product.has("share_url")) {
                        urlProduct = product.get("share_url").asText();
                    }
                    Product product1 = new Product(productId, nameProduct, price, urlImage, urlProduct);
                    productList.add(product1);
//                    System.out.println("ID: " + productId + ", Nombre: " + nameProduct + ", precio: " + price);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
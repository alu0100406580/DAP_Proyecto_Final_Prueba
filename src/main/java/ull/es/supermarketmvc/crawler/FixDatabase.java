package ull.es.supermarketmvc.crawler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpResponse;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class FixDatabase {

    void mercadonaFixUrl(List<Product> products) {
        for (Product product : products) {
            String imageUrl = product.getImageUrl();

            // Verificar si la URL de la imagen es válida
            if (! isValidUrl(imageUrl)) {
                int idBySupermarket = product.getId();

                // Construir la URL de la API
                String apiUrl = "https://tienda.mercadona.es/api/products/" + idBySupermarket;

                if(isValidUrl(apiUrl)) {
                    // Realizar la solicitud HTTP a la API
                    fixUrlsMercadona(apiUrl, product);
                    // Procesar la respuesta de la API según tus necesidades
                    System.out.println("API Response for product with id_by_supermarket " + idBySupermarket + ": " + product.getName() + " " +
                            product.getUrlProduct() + " " + product.getImageUrl());
                }
            }
        }
    }

    private boolean isValidUrl(String url) {
        try {
//            // Intentar conectarse a la URL y verificar si obtiene una respuesta 200
//            return Jsoup.connect(url).ignoreHttpErrors(true).execute().statusCode() == 200;
            // Utilizar Apache HttpClient para realizar la solicitud HTTP
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);

            // Verificar si la respuesta tiene un código 200
            return response.getCode() == 200;
        } catch (IOException e) {
            // Manejar cualquier excepción en la verificación de la URL
            e.printStackTrace();
            return false;
        }
    }

    private void fixUrlsMercadona(String url, Product product) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(new URL(url));
            String urlProduct = jsonNode.get("share_url").asText();
            String urlPhoto = jsonNode.get("photos").get(0).get("thumbnail").asText();
            product.setUrlProduct(urlProduct);
            product.setImageUrl(urlPhoto);
        } catch (IOException e) {
            // Manejar cualquier excepción en la solicitud HTTP
            e.printStackTrace();
        }
    }
}

package ull.es;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HiperDinosaurWebCrawler {

    List<Product> productList;


    HiperDinosaurWebCrawler() {
        this.productList = new ArrayList<>();
        this.SideMenuScraper();
        System.out.println("d");
    }

    public void SideMenuScraper() {
        String url = "https://www.hiperdino.es/c9505/";

        try {
            Document document = Jsoup.connect(url).get();

            // Seleccionar todos los elementos con la clase "menu__text dropdown--header"
            Elements categoryGroupHeaders = document.select("div.side-menu-category--container div.category-group__header");

            for (Element categoryGroupHeader : categoryGroupHeaders) {
                // Obtener el nombre de la familia
                String familia = categoryGroupHeader.select("div.menu__text.dropdown--header").text();
                System.out.println("Familia: " + familia);
                if (familia.isEmpty()) {
                    // Saltar a la siguiente iteración si "familia" está vacío
                    continue;
                }
                // Obtener la URL y el nombre del grupo de productos dentro de la categoría
                Elements categoryItems = categoryGroupHeader.nextElementSibling().select("ul.category__list li.sidebar-item--wrapper");

                for (Element categoryItem : categoryItems) {
                    Element anchor = categoryItem.select("a.link--wrapper").first();
                    if (anchor != null) {
                        // Obtener la URL del anchor
                        String urlAnchor = anchor.attr("href");

                        // Obtener el nombre del grupo de productos
                        String grupoProductos = anchor.select("div.sidebar__item.menu--link").text();

//                        System.out.println("URL: " + urlAnchor);
                        System.out.println("Grupo de Productos: " + grupoProductos);
                        crawlProducts(urlAnchor);
                    }
                }

                System.out.println("-----------------------------");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void crawlProducts(String url) {
//        String url = "https://www.lidl.es";
//        String url = "https://www.hiperdino.es/c9505/alimentacion/aceites.html";
//
        try {
            // Conectar a la URL y obtener el contenido HTML
            Document document = Jsoup.connect(url).get();

            // Seleccionar todos los elementos <li> con la clase "product-list-item"
            Elements productItems = document.select("ul.products-list li.product-list-item");
            String imageURL = "";
            // Iterar sobre cada elemento <li>
            for (Element productItem : productItems) {
                // Obtener la información que buscas dentro de cada <li>
                Element imageElement = productItem.select("img").first();
                if(imageElement == null) {
                    System.out.println("d");
                } else {
                    imageURL = imageElement.attr("data-src");
                    // Eliminar la parte "200x200/" del nombre del archivo
                    imageURL  = imageURL.replace("200x200/", "");
                }

                Element nameElement = productItem.select("div.description__text.name").first();
                String productName = "";
                if(nameElement == null) {
                    System.out.println("nE");
                } else {
                    productName = nameElement.text();
                }

                // Obtener el elemento li con la clase "product-list-item"
                Element productListItem = productItem.selectFirst("li.product-list-item");

                // Obtener el valor del atributo data-sort
                int idProduct = Integer.parseInt(productListItem.attr("data-id"));

                // Get the value of the "data-sort" attribute
                String dataSortAttribute = productListItem.attr("data-sort");

                // Parse the "data-sort" attribute value with Jackson
                String productPrice = "";
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(dataSortAttribute);
                    productPrice = jsonNode.path("price_desc").path("value").asText();

                    // Use the extracted value (1.13 in this example)
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(productPrice.isEmpty()) {
                    System.out.println("d");
                }

                // Crear un objeto Product y agregarlo a la lista
//                Product product = new Product(productName, productPrice, imageURL);
                Product product = new Product(idProduct, productName, productPrice, imageURL, url);
                this.productList.add(product);
            }

            // Imprimir la lista de productos (puedes realizar otras operaciones con la lista según tus necesidades)
//            for (Product product : productList) {
//                System.out.println(product);
//            }
//            System.out.println(productList.size());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package ull.es;

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

//    String aceites = "https://www.hiperdino.es/c9505/alimentacion/aceites.html";
//    String aperitivos = "https://www.hiperdino.es/c9505/alimentacion/aperitivo.html";
//    String arroz = "https://www.hiperdino.es/c9505/alimentacion/arroz.html";
//    String azucar = "https://www.hiperdino.es/c9505/alimentacion/azucar-y-edulcorantes.html";
//    String cacaoCafe = "https://www.hiperdino.es/c9505/alimentacion/cacao-y-cafe.html";
//    String cacaoCafe = "https://www.hiperdino.es/c9505/alimentacion/cacao-y-cafe.html";
//    String cacaoCafe = "https://www.hiperdino.es/c9505/alimentacion/cacao-y-cafe.html";
//    String cacaoCafe = "https://www.hiperdino.es/c9505/alimentacion/cacao-y-cafe.html";
//    String cacaoCafe = "https://www.hiperdino.es/c9505/alimentacion/cacao-y-cafe.html";
//    String cacaoCafe = "https://www.hiperdino.es/c9505/alimentacion/cacao-y-cafe.html";
//    String cacaoCafe = "https://www.hiperdino.es/c9505/alimentacion/cacao-y-cafe.html";


    HiperDinosaurWebCrawler() {
        this.SideMenuScraper();
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

                // Obtener la URL y el nombre del grupo de productos dentro de la categoría
                Elements categoryItems = categoryGroupHeader.nextElementSibling().select("ul.category__list li.sidebar-item--wrapper");

                for (Element categoryItem : categoryItems) {
                    Element anchor = categoryItem.select("a.link--wrapper").first();
                    if (anchor != null) {
                        // Obtener la URL del anchor
                        String urlAnchor = anchor.attr("href");

                        // Obtener el nombre del grupo de productos
                        String grupoProductos = anchor.select("div.sidebar__item.menu--link").text();

                        System.out.println("URL: " + urlAnchor);
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

            // Crear una lista para almacenar los productos
            List<Product> productList = new ArrayList<>();

            // Iterar sobre cada elemento <li>
            for (Element productItem : productItems) {
                // Obtener la información que buscas dentro de cada <li>
                Element imageElement = productItem.select("img.image--wrapper").first();
                String imageURL = imageElement.attr("src");

                Element nameElement = productItem.select("div.description__text.name").first();
                String productName = nameElement.text();

                Element priceElement = productItem.select("div.price__text.price").first();
                String productPrice = "-1";
                if(priceElement != null) {
                    productPrice = priceElement.text();
                }
                // Crear un objeto Product y agregarlo a la lista
                Product product = new Product(productName, productPrice, imageURL);
                productList.add(product);
            }

            // Imprimir la lista de productos (puedes realizar otras operaciones con la lista según tus necesidades)
            for (Product product : productList) {
                System.out.println(product);
            }
            System.out.println(productList.size());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

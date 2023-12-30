package ull.es;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.chrome.ChromeOptions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TuTrebolCrawler {

    public final int idSupermarket = 3;

    private WebDriver driver;
    private DatabaseManager myDB;


    private List<Product> productList;
    String url = "https://www.tutrebol.es";


    public TuTrebolCrawler() {
        this.productList = new ArrayList<Product>();
        this.driver = null;
        crawlingEntireWeb();
        this.myDB = new DatabaseManager();
        try {
            this.myDB.connect();
            this.myDB.storeProductsInDatabase(productList, idSupermarket);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void crawlingEntireWeb() {

        try {
            System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

            ChromeOptions options = new ChromeOptions();
//            options.setBinary("/usr/bin/brave-browser");  // Reemplaza con la ruta correcta del ejecutable de Brave
            options.setBinary("/usr/bin/google-chrome");
            options.addArguments("--headless"); // Activa el modo headless

            this.driver = new ChromeDriver(options);

            this.driver.get(this.url);

            // Encuentra el elemento de la megamenu
            WebElement megaMenu =  this.driver.findElement(By.className("megamenu"));

            // Encuentra todos los enlaces directos dentro del megamenu
            List<WebElement> categoryLinks = megaMenu.findElements(By.cssSelector("li.aligned-left.parent.dropdown > a"));

            // Almacenar los URLs en una lista
            List<String> categoryUrls = new ArrayList<>();
            for (WebElement categoryLink : categoryLinks) {
                String categoryUrl = categoryLink.getAttribute("href");
                categoryUrls.add(categoryUrl);
            }

            // Navegar a cada URL en un bucle separado
            for (String url : categoryUrls) {
                System.out.println("Navegando a: " + url);

                this.driver.get(url);

                // Realiza la captura de productos aquí
                captureProducts();
                // Puedes agregar un límite para pruebas, por ejemplo, solo procesar las primeras 2 categorías
                // if (limitExceeded) {
                //     break;
                // }
            }
        } finally {
            // Cierra el navegador asegurándose de que se ejecute incluso si hay un error
            if (this.driver != null) {
                for(String handle : this.driver.getWindowHandles()) {
                    this.driver.switchTo().window(handle);
                    this.driver.close();
                }
                this.driver.quit();
            }
            // System.err.println("Ha habido un error");
        }

    }

    private void captureProducts() {

        System.out.println("Capturando productos de: " + this.driver.getTitle());

        Document doc = Jsoup.parse(driver.getPageSource());

        Element productCountElement = doc.selectFirst("h1.page-heading.product-listing span.heading-counter");

        int totalProducts = extractTotalProducts(productCountElement.text());

        // Scroll hasta que se carguen todos los artículos o el número de productos alcance el límite
        int currentProductCount = 0;
        int previousProductCount = 0; // Variable para almacenar el valor anterior
        int sameCount = 0; // Contador de cuántas veces se ha repetido el valor
        while (currentProductCount < totalProducts) {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Utiliza JSoup para analizar el HTML después de cada desplazamiento
            doc = Jsoup.parse(driver.getPageSource());

            // Encuentra todos los elementos 'li' de los artículos
            Elements articles = doc.select("li.ajax_block_product");

            // Actualiza el contador de productos
            currentProductCount = articles.size();

            // Comprobar si el contador actual es igual al contador anterior
            if (currentProductCount == previousProductCount) {
                sameCount++; // Incrementa el contador de repeticiones
            } else {
                sameCount = 0; // Restablece el contador de repeticiones
                previousProductCount = currentProductCount; // Actualiza el valor anterior
            }

            // Salir del bucle si el mismo valor se repite 10 veces
            if (sameCount >= 20) {
                System.out.print("Ha habido un error con" + this.driver.getTitle());
                System.out.print(" || Articulos Encontrados: " + currentProductCount);
                System.out.println(" || Articulos totales: " + totalProducts);
                break;
            }
            System.out.println(currentProductCount);
        }

        // Utiliza JSoup para analizar el HTML

        // Encuentra todos los elementos 'li' de los artículos
        Elements articles = doc.select("li.ajax_block_product");

        // Extrae la información de cada artículo
        for (Element article : articles) {
            String title = article.select("a.product-name").attr("title");
            String url = article.select("a.product-name").attr("href");

            // Extrae el ID del producto
            int productId = Integer.parseInt(article.select("meta[itemprop=productID]").attr("content"));

            // Extrae el precio del producto
            String priceString = article.select("meta[itemprop=price]").attr("content");
            Double price = null;
            if(!priceString.isEmpty()) {
                price = Double.parseDouble(priceString);
            }
            // Extrae la URL de la imagen
            String imageUrl = article.select("img.replace-2x.img-responsive.pts-image").attr("src");

            System.out.println("Título: " + title);
            System.out.println("URL: " + url);
            System.out.println("Precio: " + price);
            System.out.println("ID del Producto: " + productId);
            System.out.println("URL de la Imagen: " + imageUrl);
            System.out.println("-----------------");

            Product p1 = new Product(productId, title, price, imageUrl, url);
            this.productList.add(p1);
        }
    }

    private int extractTotalProducts(String counterText) {
        // Utiliza una expresión regular para extraer el número de productos
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(counterText);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        } else {
            // Manejar el caso en el que no se encuentra ningún número
            return 0;
        }
    }
}

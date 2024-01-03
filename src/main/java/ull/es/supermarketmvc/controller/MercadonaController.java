package ull.es.supermarketmvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ull.es.supermarketmvc.crawler.MercadonaCrawler;
import ull.es.supermarketmvc.model.Product;
import ull.es.supermarketmvc.repository.PriceHistoryRepository;
import ull.es.supermarketmvc.repository.ProductRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MercadonaController {

    private String supermarketName = "Mercadona";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;
    @GetMapping("/mercadona/products")
    public String listaDeProductos(Model model) {
        // Obtener la lista de productos desde el repositorio
//        Iterable<Product> productos = productRepository.findAll();
        List<Product> products = productRepository.findProductBySupermarketId(2);
        List<Long> productsIds = products.stream().map(Product::getId).collect(Collectors.toList());

        LocalDate dateDatabaseUpdate = priceHistoryRepository.findMaxDateByProductIds(productsIds);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = dateDatabaseUpdate.format(formatter);

        model.addAttribute("products", products);
        model.addAttribute("supermarketName", supermarketName);
        model.addAttribute("dateDatabaseUpdate", formattedDate);
        model.addAttribute("selectedProducts", new ArrayList<Long>());

        return "productList";
    }

    @GetMapping("/mercadona/update")
    public ResponseEntity<String> executeCrawler() {
        System.out.println("Inicio actualizacion BD");
        long startTime = System.currentTimeMillis();
        MercadonaCrawler crawlerMercadona = new MercadonaCrawler();
        long mercadonaTime = System.currentTimeMillis() - startTime;
        System.out.println("Tiempo de ejecución de MercadonaCrawler: " + mercadonaTime / 1000 + " segundos");

        return ResponseEntity.ok("Actualización exitosa");
    }
}
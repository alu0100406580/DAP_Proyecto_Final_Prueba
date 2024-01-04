package ull.es.supermarketmvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ull.es.supermarketmvc.crawler.MercadonaCrawler;
import ull.es.supermarketmvc.model.Product;
import ull.es.supermarketmvc.model.SelectedProduct;
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

    private int supermarketId = 2;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;
    @GetMapping("/mercadona/products")
    public String listaDeProductos(Model model) {
        // Obtener la lista de productos desde el repositorio
//        Iterable<Product> productos = productRepository.findAll();
        int page = 0;
        int size = 200;
        Pageable pageable = PageRequest.of(page, size);
        List<Product> products = productRepository.findProductBySupermarketIdPageable(2, pageable);
        List<Product> allProducts = productRepository.findProductBySupermarketId(2);
        List<Long> productsIds = allProducts.stream().map(Product::getId).collect(Collectors.toList());

        LocalDate dateDatabaseUpdate = priceHistoryRepository.findMaxDateByProductIds(productsIds);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = dateDatabaseUpdate.format(formatter);

        String urlUpdate = "/mercadona/update";

        model.addAttribute("urlUpdate", urlUpdate);
        model.addAttribute("products", products);
        model.addAttribute("supermarketId", supermarketId);
        model.addAttribute("supermarketName", supermarketName);
        model.addAttribute("dateDatabaseUpdate", formattedDate);
        model.addAttribute("selectedProducts", new ArrayList<SelectedProduct>());

        return "productList";
    }

    @GetMapping("/mercadona/update")
    public ResponseEntity<String> executeCrawler() {
        System.out.println("Inicio actualizacion BD");
        long startTime = System.currentTimeMillis();
        MercadonaCrawler crawlerMercadona = new MercadonaCrawler();
        long mercadonaTime = System.currentTimeMillis() - startTime;
        System.out.println("Tiempo de ejecución de Mercadona Update: " + mercadonaTime / 1000 / 60 + " minutos");

        return ResponseEntity.ok("Actualización exitosa");
    }
}
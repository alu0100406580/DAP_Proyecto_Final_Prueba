package ull.es.supermarketmvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ull.es.supermarketmvc.crawler.MercadonaCrawler;
import ull.es.supermarketmvc.model.Product;
import ull.es.supermarketmvc.repository.PriceHistoryRepository;
import ull.es.supermarketmvc.repository.ProductRepository;

import java.util.List;

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

        model.addAttribute("products", products);
        model.addAttribute("supermarketName", supermarketName);

        return "listaProductos";
    }

    @GetMapping("/mercadona/update")  // Cambia "ejecutar-crawler" por la ruta que desees
    public String executeCrawler() {
        System.out.println("Inicio actualizacion BD");
        long startTime = System.currentTimeMillis();
        MercadonaCrawler crawlerMercadona = new MercadonaCrawler();
        long mercadonaTime = System.currentTimeMillis() - startTime;
        System.out.println("Tiempo de ejecución de MercadonaCrawler: " + mercadonaTime / 1000 + " segundos");

        // Puedes retornar una vista o redirigir a otra URL según tus necesidades
        return "listaProductos";  // Cambia "vista-resultados" por el nombre de tu vista
    }
}

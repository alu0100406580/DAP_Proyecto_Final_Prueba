package ull.es.supermarketmvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ull.es.supermarketmvc.model.PriceHistory;
import ull.es.supermarketmvc.model.Product;
import ull.es.supermarketmvc.repository.PriceHistoryRepository;
import ull.es.supermarketmvc.repository.ProductRepository;

import java.time.LocalDate;
import java.util.List;

@Controller
public class SupermarketController {

//    @Autowired
//    Environment env;
//
//    @Bean
//    public DataSource dataSource() {
//        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(env.getProperty("driverClassName"));
//        dataSource.setUrl(env.getProperty("url"));
//        dataSource.setUsername(env.getProperty("user"));
//        dataSource.setPassword(env.getProperty("password"));
//        return dataSource;
//    }

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    @GetMapping("/")
    public String home(Model model) {
        // Supongamos que tienes un servicio para obtener las fechas y otros datos necesarios
        List<Long> hiperdinoIds = productRepository.findIdBySupermarket(1);
        List<Long> mercadonaIds = productRepository.findIdBySupermarket(2);
        List<Long> tutrebolIds = productRepository.findIdBySupermarket(3);

        LocalDate hiperdinoDate = priceHistoryRepository.findMaxDateByProductIds(hiperdinoIds);
        LocalDate mercadonaDate = priceHistoryRepository.findMaxDateByProductIds(mercadonaIds);
        LocalDate tutrebolDate = priceHistoryRepository.findMaxDateByProductIds(tutrebolIds);

        model.addAttribute("hiperdinoDate", hiperdinoDate);
        model.addAttribute("mercadonaDate", mercadonaDate);
        model.addAttribute("tutrebolDate", tutrebolDate);

        return "home";
    }

    @GetMapping("/productos")
    public String listaDeProductos(Model model) {
        // Obtener la lista de productos desde el repositorio
//        Iterable<Product> productos = productRepository.findAll();
        List<Product> productos = productRepository.findProductBySupermarketId(2);
        model.addAttribute("productos", productos);

        // Devolver el nombre de la vista (plantilla Thymeleaf)
        return "listaProductos";
    }

    // Otros métodos del controlador para crear, actualizar, eliminar, etc.
}
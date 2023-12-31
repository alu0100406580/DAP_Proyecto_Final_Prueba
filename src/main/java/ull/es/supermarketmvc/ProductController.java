package ull.es.supermarketmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ull.es.supermarketmvc.model.Product;
import ull.es.supermarketmvc.repository.ProductRepository;

import java.util.List;

@Controller
public class ProductController {

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

    @GetMapping("/productos")
    public String listaDeProductos(Model model) {
        // Obtener la lista de productos desde el repositorio
//        Iterable<Product> productos = productRepository.findAll();
        List<Product> productos = productRepository.findBySupermarketId(2);
        model.addAttribute("productos", productos);

        // Devolver el nombre de la vista (plantilla Thymeleaf)
        return "listaProductos";
    }

    // Otros métodos del controlador para crear, actualizar, eliminar, etc.
}
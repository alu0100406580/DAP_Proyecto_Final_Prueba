package ull.es.supermarketmvc.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ull.es.supermarketmvc.repository.PriceHistoryRepository;
import ull.es.supermarketmvc.repository.ProductRepository;

import java.util.List;

@Controller
public class DashboardController {

    private List<Long> selectedProducts;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    @GetMapping("/dashboard/products")
    public String mostrarProductosSeleccionados(Model model, HttpSession session) {

        // Obtener los productos seleccionados de la sesión
        List<Long> selectedProducts = (List<Long>) session.getAttribute("selectedProducts");

        // Añadir la lista de productos seleccionados al modelo
        model.addAttribute("selectedProducts", selectedProducts);

        // Devolver el nombre de la vista Thymeleaf que mostrará los productos seleccionados
        return "dashboardProducts";

    }

    @PostMapping("/saveProductListController")
    public ResponseEntity<String> saveProductListController(@RequestBody List<Long> selectedProducts, HttpSession session) {
        // Guardar la lista en la sesión
        session.setAttribute("selectedProducts", selectedProducts);

        // Puedes devolver una respuesta JSON o simplemente un mensaje indicando el éxito
        return ResponseEntity.ok("Productos seleccionados guardados correctamente");
    }
}

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
import ull.es.supermarketmvc.model.SelectedProduct;
import ull.es.supermarketmvc.repository.PriceHistoryRepository;
import ull.es.supermarketmvc.repository.ProductRepository;
import ull.es.supermarketmvc.service.SelectedProductService;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private SelectedProductService selectedProductService;

    @GetMapping("/dashboard/products")
    public String mostrarProductosSeleccionados(Model model, HttpSession session) {

        // Obtener los productos seleccionados de la sesión
        List<SelectedProduct> selectedProducts = (List<SelectedProduct>) session.getAttribute("selectedProducts");
        List<SelectedProduct> fillSelectedProducts = selectedProductService.fillData(selectedProducts);
        selectedProductService.saveSelectedProducts(fillSelectedProducts);

        // Añadir la lista de productos seleccionados al modelo
        model.addAttribute("selectedProducts", fillSelectedProducts);

        // Devolver el nombre de la vista Thymeleaf que mostrará los productos seleccionados
        return "dashboardProducts";
    }
}

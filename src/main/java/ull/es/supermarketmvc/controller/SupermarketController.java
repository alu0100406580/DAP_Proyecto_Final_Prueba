package ull.es.supermarketmvc.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ull.es.supermarketmvc.model.PriceHistory;
import ull.es.supermarketmvc.model.Product;
import ull.es.supermarketmvc.model.SelectedProduct;
import ull.es.supermarketmvc.repository.PriceHistoryRepository;
import ull.es.supermarketmvc.repository.ProductRepository;
import ull.es.supermarketmvc.repository.SelectedProductRepository;
import ull.es.supermarketmvc.service.SelectedProductService;

import java.time.LocalDate;
import java.util.List;

@Controller
public class SupermarketController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PriceHistoryRepository priceHistoryRepository;
    @Autowired
    private SelectedProductService selectedProductService;

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

    @PostMapping("/saveProductListController")
    public ResponseEntity<String> saveProductListController(@RequestBody List<SelectedProduct> selectedProducts, HttpSession session) {
        // Guardar la lista en la sesión
        session.setAttribute("selectedProducts", selectedProducts);
        selectedProductService.saveSelectedProducts(selectedProducts);

        // Puedes devolver una respuesta JSON o simplemente un mensaje indicando el éxito
        return ResponseEntity.ok("Productos seleccionados guardados correctamente");
    }
    @PostMapping("/deleteProductListController")
    public ResponseEntity<String> deleteProductListController() {
        selectedProductService.deleteProductList();

        // Puedes devolver una respuesta JSON o simplemente un mensaje indicando el éxito
        return ResponseEntity.ok("Productos Seleccionados borrados");
    }
}
package ull.es.supermarketmvc.controller;

import org.apache.commons.math3.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ull.es.supermarketmvc.model.PriceHistory;
import ull.es.supermarketmvc.model.SelectedProduct;
import ull.es.supermarketmvc.repository.PriceHistoryRepository;
import ull.es.supermarketmvc.repository.SelectedProductRepository;
import ull.es.supermarketmvc.service.SelectedProductService;

import java.time.LocalDate;
import java.util.*;

@Controller
public class DashboardController {

    private List<SelectedProduct> selectedProducts;

    @Autowired
    private SelectedProductService selectedProductService;

    @Autowired
    private SelectedProductRepository selectedProductRepository;

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    DashboardController() {}

    @GetMapping("/dashboard/products")
    public String mostrarProductosSeleccionados(Model model) {

        // Obtener los productos seleccionados de la sesión
        this.selectedProducts = selectedProductRepository.findAll();
//                (List<SelectedProduct>).getAttribute("selectedProducts");
        List<SelectedProduct> fillSelectedProducts = this.selectedProductService.fillData(selectedProducts);

        // Añadir la lista de productos seleccionados al modelo
        model.addAttribute("selectedProducts", fillSelectedProducts);

        Map<String, Object> chartData = createDashboardBuyingChart();

        chartData.put("labels", Arrays.asList("Aguacates", "Melocoton", "Lentejas", "Derivados"));
        chartData.put("data", Arrays.asList(100, 100, 100, 100));
        model.addAttribute("chartData", chartData);

        // Devolver el nombre de la vista Thymeleaf que mostrará los productos seleccionados
        return "dashboardProducts";
    }

    private Map<String, Object> createDashboardBuyingChart() {
        Map<String, Object> chartData = new HashMap<>();
        List<PriceHistory> selectedProductsHistoric = retrievePriceHistoryProducts();
        Map<String, LocalDate> datesSupermarket = categorizeDateProducts(selectedProductsHistoric);
        return chartData;
    }

    private Map<String, LocalDate> categorizeDateProducts(List<PriceHistory> selectedProductsHistoric) {
        Map<String, LocalDate> datesSupermarket = new HashMap<>();
        return datesSupermarket;
    }

    private List<PriceHistory> retrievePriceHistoryProducts() {
        List<PriceHistory> priceHistoryProducts = new ArrayList<>();
        for(SelectedProduct selectedProduct : this.selectedProducts) {
            Long idProduct = selectedProduct.getIdProduct();
            priceHistoryProducts.addAll(priceHistoryRepository.findByProductId(idProduct));
        }
        return priceHistoryProducts;
    }
}

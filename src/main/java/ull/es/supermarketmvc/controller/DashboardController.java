package ull.es.supermarketmvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ull.es.supermarketmvc.component.DashboardShoppingChartHistoricDataComponent;
import ull.es.supermarketmvc.model.SelectedProduct;
import ull.es.supermarketmvc.repository.PriceHistoryRepository;
import ull.es.supermarketmvc.repository.ProductRepository;
import ull.es.supermarketmvc.repository.SelectedProductRepository;
import ull.es.supermarketmvc.service.SelectedProductService;

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

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DashboardShoppingChartHistoricDataComponent dataComponent;

    DashboardController() {}

    @GetMapping("/dashboard/products")
    public String mostrarProductosSeleccionados(Model model) {

        // Obtener los productos seleccionados de la sesión
        this.selectedProducts = selectedProductRepository.findAll();
        List<SelectedProduct> fillSelectedProducts = this.selectedProductService.fillData(selectedProducts);

        // Añadir la lista de productos seleccionados al modelo
        model.addAttribute("selectedProducts", fillSelectedProducts);

        createDataForShoppingChart(model);


        // Devolver el nombre de la vista Thymeleaf que mostrará los productos seleccionados
        return "dashboardProducts";
    }

    void createDataForShoppingChart(Model model) {
        dataComponent.retrieveData(this.selectedProducts);
        Map<String, Object> chartDataHiperdino = new HashMap<>();
        Map<String, Object> chartDataMercadona = new HashMap<>();
        Map<String, Object> chartDataTuTrebol = new HashMap<>();


        chartDataHiperdino.put("labels", dataComponent.getHiperdinoData().keySet());
        chartDataHiperdino.put("data", dataComponent.getHiperdinoData().values());
        model.addAttribute("hiperdinoChartData", chartDataHiperdino);

        chartDataMercadona.put("labels", dataComponent.getMercadonaData().keySet());
        chartDataMercadona.put("data", dataComponent.getMercadonaData().values());
        model.addAttribute("mercadonaChartData", chartDataMercadona);

        chartDataTuTrebol.put("labels", dataComponent.getTuTrebolData().keySet());
        chartDataTuTrebol.put("data", dataComponent.getTuTrebolData().values());
        model.addAttribute("tuTrebolChartData", chartDataTuTrebol);
    }
}

package ull.es.supermarketmvc.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ull.es.supermarketmvc.model.PriceHistory;
import ull.es.supermarketmvc.model.SelectedProduct;
import ull.es.supermarketmvc.repository.PriceHistoryRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DashboardShoppingChartHistoricDataComponent {

    @Autowired
    public PriceHistoryRepository priceHistoryRepository;

    private List<SelectedProduct> selectedProducts;
    private Map<LocalDate, Double> hiperdinoData;
    private Map<LocalDate, Double> mercadonaData;
    private Map<LocalDate, Double> tuTrebolData;

    public Map<LocalDate, Double> getHiperdinoData() {
        return hiperdinoData;
    }

    public Map<LocalDate, Double> getMercadonaData() {
        return mercadonaData;
    }

    public Map<LocalDate, Double> getTuTrebolData() {
        return tuTrebolData;
    }

    public DashboardShoppingChartHistoricDataComponent() {
        hiperdinoData = new HashMap<>();
        mercadonaData = new HashMap<>();
        tuTrebolData = new HashMap<>();
    }

    public void retrieveData(List<SelectedProduct> selectedProducts) {
        this.selectedProducts = selectedProducts;
        List<PriceHistory> selectedProductsHistoric = retrievePriceHistoryProducts();
        Map<Long, List<PriceHistory>> productHistoricbySupermarket = categorizeSupermarketHistoricProducts(selectedProductsHistoric);
        dataDateAndPrice(productHistoricbySupermarket);
    }

    private void saveTotalPriceByDay(Long idSupermarket, LocalDate date, double totalPrice) {
        if(idSupermarket == 1) this.hiperdinoData.put(date, totalPrice);
        else if(idSupermarket == 2) this.mercadonaData.put(date, totalPrice);
        else if(idSupermarket == 3) this.tuTrebolData.put(date, totalPrice);
    }

    private void sumPricesByDatesAndSupermarket(Long idSupermarket, Map<LocalDate, List<PriceHistory>> daysByProductsHistoric) {
        int totalProductsByIdSupermarket = 0;
        for (SelectedProduct selectedProduct : this.selectedProducts) {
            Long supermarketId = selectedProduct.getIdSupermarket();
            if (idSupermarket.equals(supermarketId)) {
                totalProductsByIdSupermarket++;
            }
        }
        for (Map.Entry<LocalDate, List<PriceHistory>> entry : daysByProductsHistoric.entrySet()) {
            LocalDate localDate = entry.getKey();
            List<PriceHistory> priceHistoryList = entry.getValue();

            if (priceHistoryList.size() == totalProductsByIdSupermarket) {
                double totalPrices = 0;
                for (PriceHistory priceHistory : priceHistoryList) {
                    // Obtener la cantidad de productos desde selectedProducts
                    long productQuantity = this.selectedProducts.stream()
                            .filter(selectedProduct ->
                                    selectedProduct.getIdProduct().equals(priceHistory.getProduct().getId()))
                            .mapToLong(SelectedProduct::getQuantity)
                            .sum();
                    // Multiplicar el valor de 'price' por la cantidad y sumar al total
                    totalPrices += priceHistory.getPrice() * productQuantity;
                }
                // Precios ya sumados
                saveTotalPriceByDay(idSupermarket, localDate,totalPrices);
            } else {
                // 0 si el número de elementos no coincide con el total de productsSelected
                saveTotalPriceByDay(idSupermarket,localDate,0);
            }
        }

    }

    private void dataDateAndPrice(Map<Long, List<PriceHistory>> productHistoricbySupermarket) {
        for(Map.Entry<Long, List<PriceHistory>> entry : productHistoricbySupermarket.entrySet()) {
            Map<LocalDate, List<PriceHistory>> daysByProductsHistoric = new HashMap<>();
            Long supermarketId = entry.getKey();
            List<PriceHistory> priceHistoryList = entry.getValue();
            for(PriceHistory historyPrice : priceHistoryList ) {
                LocalDate localDateToMap = historyPrice.getReadAt().toLocalDate();
                List<PriceHistory> historyListToMap = daysByProductsHistoric.getOrDefault(localDateToMap, new ArrayList<>());
                historyListToMap.add(historyPrice);
                daysByProductsHistoric.put(localDateToMap, historyListToMap);
            }
            sumPricesByDatesAndSupermarket(supermarketId, daysByProductsHistoric);
        }
    }

    private Map<Long, List<PriceHistory>> categorizeSupermarketHistoricProducts(List<PriceHistory> selectedProductsHistoric) {
        Map<Long, List<PriceHistory>> productsHistoricBySupermarket = new HashMap<>();

        for (PriceHistory productHistory : selectedProductsHistoric) {
            Long supermarketId = productHistory.getProduct().getIdSupermarket();
            List<PriceHistory> historyList = productsHistoricBySupermarket.getOrDefault(supermarketId, new ArrayList<>());
            historyList.add(productHistory);
            // Actualizar la lista en el mapa
            productsHistoricBySupermarket.put(supermarketId, historyList);
        }
        return productsHistoricBySupermarket;
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

package ull.es.supermarketmvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ull.es.supermarketmvc.model.Product;
import ull.es.supermarketmvc.model.SelectedProduct;
import ull.es.supermarketmvc.model.Supermarket;
import ull.es.supermarketmvc.repository.ProductRepository;
import ull.es.supermarketmvc.repository.SelectedProductRepository;
import ull.es.supermarketmvc.repository.SupermarketRepository;

import java.util.List;

@Service
public class SelectedProductService {

    @Autowired
    private SelectedProductRepository selectedProductRepository;

    @Autowired
    private SupermarketRepository supermarketRepository;

    @Autowired
    private ProductRepository productRepository;


    public void saveSelectedProduct(SelectedProduct selectedProduct) {
        selectedProductRepository.save(selectedProduct);
    }

    public void saveSelectedProducts(List<SelectedProduct> selectedProducts) {
        selectedProductRepository.deleteBySupermarketId(selectedProducts.get(0).getIdSupermarket());
        for(SelectedProduct selectedProduct : selectedProducts) {
            saveSelectedProduct(selectedProduct);
        }
    }

    public List<SelectedProduct> fillData(List<SelectedProduct> selectedProducts) {
        for(SelectedProduct product : selectedProducts) {
            product.setSupermarket(loadSupermarket(product.getIdSupermarket()));
            product.setProduct(loadProduct(product.getIdProduct()));
        }
        return selectedProducts;
    }

    public Supermarket loadSupermarket(Long idSupermarket) {
        return supermarketRepository.findById(idSupermarket).orElse(null);
    }

    // Método para cargar Product
    public Product loadProduct(Long idProduct) {
        return productRepository.findById(idProduct).orElse(null);
    }
}
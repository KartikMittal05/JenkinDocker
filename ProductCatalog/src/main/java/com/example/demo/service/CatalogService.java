//package com.example.demo.service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import com.example.demo.model.Inventory;
//import com.example.demo.model.Price;
//import com.example.demo.model.Product;
//import com.example.demo.model.ProductCatalog;
//
//@Service
//public class CatalogService {
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    private final String PRODUCT = "http://Product/products/";
//    private final String PRICE = "http://Price/prices/";
//    private final String INVENTORY = "http://Inventory/inventory/";
//
//    public ProductCatalog getCatalog(Long pid) {
//
//        Product p = restTemplate.getForObject(PRODUCT + pid, Product.class);
//        Price price = restTemplate.getForObject(PRICE + pid, Price.class);
//        Inventory inv = restTemplate.getForObject(INVENTORY + pid, Inventory.class);
//
//        ProductCatalog pc = new ProductCatalog();
//        pc.setPid(pid);
//        pc.setPname(p.getPname());
//        pc.setPcategory(p.getPcategory());
//        pc.setDiscountedPrice(price.getOfferPrice());
//        pc.setNoOfItems(inv.getNoOfItemsLeft());
//
//        return pc;
//    }
//
//    public List<ProductCatalog> getByCategory(String category) {
//
//        Product[] products = restTemplate.getForObject(
//                PRODUCT + "category/" + category,
//                Product[].class
//        );
//
//        List<ProductCatalog> list = new ArrayList<>();
//        for (Product p : products) {
//            list.add(getCatalog(p.getPid()));
//        }
//        return list;
//    }
//}

package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Inventory;
import com.example.demo.model.Price;
import com.example.demo.model.Product;
import com.example.demo.model.ProductCatalog;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class CatalogService {

    @Autowired
    private RestTemplate restTemplate;

    private final String PRODUCT = "http://PRODUCT/products/";
    private final String PRICE = "http://PRICE/prices/";
    private final String INVENTORY = "http://INVENTORY/inventory/";

    // =====================================================
    // 🔥 SINGLE PRODUCT
    // =====================================================
    @CircuitBreaker(name = "catalogCB", fallbackMethod = "catalogFallback")
    public ProductCatalog getCatalog(Long pid) {

        Product p = restTemplate.getForObject(PRODUCT + pid, Product.class);
        Price price = restTemplate.getForObject(PRICE + pid, Price.class);
        Inventory inv = restTemplate.getForObject(INVENTORY + pid, Inventory.class);

        if (p == null || price == null || inv == null) {
            throw new RuntimeException("Service failure");
        }

        ProductCatalog pc = new ProductCatalog();
        pc.setPid(pid);
        pc.setPname(p.getPname());
        pc.setPcategory(p.getPcategory());
        pc.setDiscountedPrice(price.getOfferPrice());
        pc.setNoOfItems(inv.getNoOfItemsLeft());

        return pc;
    }

    // =====================================================
    // 🔥 CATEGORY
    // =====================================================
    @CircuitBreaker(name = "catalogCB", fallbackMethod = "categoryFallback")
    public List<ProductCatalog> getByCategory(String category) {

        Product[] products = restTemplate.getForObject(
                PRODUCT + "category/" + category,
                Product[].class
        );

        if (products == null) {
            throw new RuntimeException("Product service down");
        }

        List<ProductCatalog> list = new ArrayList<>();

        for (Product p : products) {
            list.add(getCatalog(p.getPid()));
        }

        return list;
    }

    // =====================================================
    // 🔥 FALLBACK (SINGLE)
    // =====================================================
    public ProductCatalog catalogFallback(Long pid, Throwable ex) {

        System.out.println("🔥 FALLBACK WORKING: " + ex.getMessage());

        ProductCatalog pc = new ProductCatalog();
        pc.setPid(pid);
        pc.setPname("Fallback Product");
        pc.setPcategory("Service Down");
        pc.setDiscountedPrice(0.0);
        pc.setNoOfItems(0);

        return pc;
    }

    // =====================================================
    // 🔥 FALLBACK (CATEGORY)
    // =====================================================
    public List<ProductCatalog> categoryFallback(String category, Throwable ex) {

        System.out.println("🔥 CATEGORY FALLBACK: " + ex.getMessage());

        List<ProductCatalog> list = new ArrayList<>();

        ProductCatalog dummy = new ProductCatalog();
        dummy.setPid(0L);
        dummy.setPname("Fallback Product");
        dummy.setPcategory(category);
        dummy.setDiscountedPrice(0.0);
        dummy.setNoOfItems(0);

        list.add(dummy);

        return list;
    }
}

//package com.example.demo.service;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import com.example.demo.model.Cart;
//import com.example.demo.model.ProductCatalog;
//import com.example.demo.repository.CartRepository;
//
//@Service
//public class CartService {
//
//    @Autowired
//    private CartRepository repo;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    private final String CATALOG = "http://ProductCatalog/catalogs/products/";
//    private final String RECOMMEND = "http://Recommendation/recommendations";
//   
//    public Map<String, Object> add(Long cartId, Long pid) {
//
//        Cart cart = repo.findById(cartId).orElse(new Cart());
//        cart.setCartId(cartId);
//
//        // 🔹 Fetch product from catalog
//        ProductCatalog pc = restTemplate.getForObject(
//                CATALOG + pid,
//                ProductCatalog.class
//        );
//
//        // 🔹 Add to cart
//        cart.getProducts().put(pid, pc.getPname());
//        repo.save(cart);
//
//        // 🔹 Prepare pid list
//        List<Long> pids = new ArrayList<>(cart.getProducts().keySet());
//
//        // 🔹 Call recommendation service
//        ProductCatalog[] rec =
//                restTemplate.postForObject(RECOMMEND, pids, ProductCatalog[].class);
//
//        // 🔹 Prepare response
//        Map<String, Object> res = new HashMap<>();
//        res.put("message", "Product added to cart");
////        res.put("addedProduct", pc);
//        res.put("recommendedItems", Arrays.asList(rec));
//
//        return res;
//    }
//    public List<ProductCatalog> get(Long cartId) {
//
//        Cart cart = repo.findById(cartId).orElse(null);
//
//        List<ProductCatalog> cartItems = new ArrayList<>();
//
//        if (cart != null) {
//            for (Long pid : cart.getProducts().keySet()) {
//                cartItems.add(restTemplate.getForObject(
//                        CATALOG + pid,
//                        ProductCatalog.class
//                ));
//            }
//        }
//
//        return cartItems;
//    }
//}
//
//

package com.example.demo.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Cart;
import com.example.demo.model.ProductCatalog;
import com.example.demo.repository.CartRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class CartService {

    @Autowired
    private CartRepository repo;

    @Autowired
    private RestTemplate restTemplate;

    private final String CATALOG = "http://ProductCatalog/catalogs/products/";
    private final String RECOMMEND = "http://Recommendation/recommendations";

    // =====================================================
    // 🔥 ADD TO CART (WITH SINGLE CIRCUIT BREAKER)
    // =====================================================
    @CircuitBreaker(name = "cartServiceCB", fallbackMethod = "cartFallback")
    public Map<String, Object> add(Long cartId, Long pid) {

        Cart cart = repo.findById(cartId).orElse(new Cart());
        cart.setCartId(cartId);

        // 🔹 Get product from catalog
        ProductCatalog pc = restTemplate.getForObject(
                CATALOG + pid,
                ProductCatalog.class
        );

        if (pc == null) {
            throw new RuntimeException("Catalog service failed");
        }

        // 🔹 Add to cart
        cart.getProducts().put(pid, pc.getPname());
        repo.save(cart);

        // 🔹 Prepare pid list
        List<Long> pids = new ArrayList<>(cart.getProducts().keySet());

        // 🔹 Get recommendations
        ProductCatalog[] rec = restTemplate.postForObject(
                RECOMMEND,
                pids,
                ProductCatalog[].class
        );

        if (rec == null) {
            throw new RuntimeException("Recommendation service failed");
        }

        // 🔹 Response
        Map<String, Object> res = new HashMap<>();
        res.put("message", "Product added successfully");
        res.put("cartItems", Arrays.asList(pc));
        res.put("recommendedItems", Arrays.asList(rec));

        return res;
    }

    // =====================================================
    // 🔥 GET CART (OPTIONAL CB)
    // =====================================================
    @CircuitBreaker(name = "cartServiceCB", fallbackMethod = "getFallback")
    public List<ProductCatalog> get(Long cartId) {

        Cart cart = repo.findById(cartId).orElse(null);
        List<ProductCatalog> cartItems = new ArrayList<>();

        if (cart != null) {
            for (Long pid : cart.getProducts().keySet()) {

                ProductCatalog pc = restTemplate.getForObject(
                        CATALOG + pid,
                        ProductCatalog.class
                );

                if (pc != null) {
                    cartItems.add(pc);
                }
            }
        }

        return cartItems;
    }

    // =====================================================
    // 🔥 COMMON FALLBACK FOR ADD
    // =====================================================
    public Map<String, Object> cartFallback(Long cartId, Long pid, Throwable ex) {

        System.out.println("🔥 FALLBACK TRIGGERED: " + ex.getMessage());

        // 🔹 Dummy cart item
        ProductCatalog dummyProduct = new ProductCatalog();
        dummyProduct.setPid(pid);
        dummyProduct.setPname("Demo Product (Service Down)");
        dummyProduct.setPcategory("Fallback");

        // 🔹 Dummy recommendation
        ProductCatalog rec1 = new ProductCatalog();
        rec1.setPid(999L);
        rec1.setPname("Demo Recommendation 1");
        rec1.setPcategory("Fallback");

        ProductCatalog rec2 = new ProductCatalog();
        rec2.setPid(1000L);
        rec2.setPname("Demo Recommendation 2");
        rec2.setPcategory("Fallback");

        Map<String, Object> res = new HashMap<>();
        res.put("message", "⚠️ Services are down - showing fallback data");
        res.put("cartItems", Arrays.asList(dummyProduct));
        res.put("recommendedItems", Arrays.asList(rec1, rec2));

        return res;
    }

    // =====================================================
    // 🔥 FALLBACK FOR GET
    // =====================================================
    public List<ProductCatalog> getFallback(Long cartId, Throwable ex) {

        System.out.println("🔥 GET FALLBACK: " + ex.getMessage());

        ProductCatalog dummy = new ProductCatalog();
        dummy.setPid(0L);
        dummy.setPname("Demo Cart Item (Service Down)");
        dummy.setPcategory("Fallback");

        return Arrays.asList(dummy);
    }
}
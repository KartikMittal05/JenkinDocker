

package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.service.ProductCatalog;

@Service
public class RecommendationService {

    @Autowired
    private RestTemplate restTemplate;

    private final String PRODUCT = "http://Product/products/";
    private final String CATALOG = "http://ProductCatalog/catalogs/products/category/";

    public List<ProductCatalog> recommend(List<Long> pids) {

        List<ProductCatalog> result = new ArrayList<>();

        for (Long pid : pids) {

            Product p = restTemplate.getForObject(PRODUCT + pid, Product.class);

            ProductCatalog[] sameCategory =
                    restTemplate.getForObject(
                            CATALOG + p.getPcategory(),
                            ProductCatalog[].class
                    );

            for (ProductCatalog pc : sameCategory) {
                if (!pids.contains(pc.getPid())) {
                    result.add(pc);
                }
            }
        }

        return result;
    }
}
package com.foodfetish.picker.controllers;

import com.foodfetish.picker.models.FoodProduct;
import com.foodfetish.picker.repo.FoodProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class FoodRepositoryController {
    @Autowired
    private FoodProductRepository foodProductRepository;



    @GetMapping("/list")
    public String find(Model model) {
        Iterable<FoodProduct> output = foodProductRepository.findAll();
        model.addAttribute("products", output);
        return "prod-table";
    }

    @PostMapping(path="/add-product/add")
    public String addNewProduct(@RequestParam String name, @RequestParam Double prots, @RequestParam Double fats, @RequestParam Double carbs) {
        FoodProduct n = new FoodProduct(name, prots, fats, carbs);
        foodProductRepository.save(n);
        return "add-product";
    }

}

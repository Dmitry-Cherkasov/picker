package com.foodfetish.picker.controllers;

import com.foodfetish.picker.models.FoodProduct;
import com.foodfetish.picker.repo.FoodProductRepository;
import com.foodfetish.picker.utils.ProductLister;
import jakarta.persistence.GeneratedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Locale;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home page");
        return "home";
    }

    @GetMapping("/mix")
    public String mix(Model model) {
        model.addAttribute("title", "Mixer");
        
        return "mix";
    }


    @GetMapping("/add-product")
    public String addFoodProduct(Model model) {
        return "add-product";
    }


    @GetMapping("/database")
    public String database(Model model) {
        model.addAttribute("title", "Database mng");
        return "database";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }



}

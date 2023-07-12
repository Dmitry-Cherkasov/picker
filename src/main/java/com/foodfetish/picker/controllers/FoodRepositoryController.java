package com.foodfetish.picker.controllers;

import com.foodfetish.picker.models.FoodProduct;
import com.foodfetish.picker.repo.FoodProductRepository;
import com.foodfetish.picker.utils.ProductLister;
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
public class FoodRepositoryController {
    @Autowired
    private FoodProductRepository foodProductRepository;


    @GetMapping("/list")
    public String find(Model model) {
        Iterable<FoodProduct> output = foodProductRepository.findAll();
        model.addAttribute("products", output);
        return "prod-table";
    }

    @PostMapping(path="/add-product/add") // Map ONLY POST Requests
    public String addNewProduct(@RequestParam String name
            , @RequestParam Double prots, @RequestParam Double fats, @RequestParam Double carbs) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        FoodProduct n = new FoodProduct(name, prots, fats, carbs);
        foodProductRepository.save(n);
        return "add-product";
    }

    @PostMapping(path="/database/clear") // Map ONLY POST Requests
    public @ResponseBody
    String clearAll() {
        foodProductRepository.deleteAll();
        return  "Database is deleted!";
    }

    @PostMapping(path="/database/upload")
    public String uploadData() {
        List<String> input = new ProductLister().getList();
        input.forEach(s->{
            String [] arr = s.replaceAll("\\ ", "").strip().split(":");
            if (arr.length == 4){
            FoodProduct el = new FoodProduct(arr[0].strip(),
                    Double.valueOf(arr[1].strip()),
                    Double.valueOf(arr[2].strip()),
                    Double.valueOf(arr[3].strip()));
            foodProductRepository.save(el);}
        });

        return  "/";
    }

    public FoodProductRepository getFoodProductRepository() {
        return foodProductRepository;
    }

}

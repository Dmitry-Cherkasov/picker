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

@Controller
public class DatabaseController {
    @Autowired
    private FoodProductRepository foodProductRepository;




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

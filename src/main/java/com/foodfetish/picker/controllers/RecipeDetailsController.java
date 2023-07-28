package com.foodfetish.picker.controllers;

import com.foodfetish.picker.models.FoodProduct;
import com.foodfetish.picker.models.Recipe;
import com.foodfetish.picker.repo.FoodProductRepository;
import com.foodfetish.picker.repo.RecipeRepository;
import com.foodfetish.picker.utils.ContentJsonParser;
import com.foodfetish.picker.utils.ProductLister;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.LinkedHashMap;
import java.util.Optional;

@Controller
public class RecipeDetailsController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private FoodProductRepository foodProductRepository;


    @GetMapping("/recipes/recipe/{id}")
    public String getDetails(@PathVariable(value = "id") long id, Model model) {
        if (!recipeRepository.existsById(id)) {

            return "redirect:/recipes";
        }

        Optional<Recipe> current = recipeRepository.findById(id);
        Recipe recipe = current.get();
        ContentJsonParser parser = new ContentJsonParser(foodProductRepository);
        LinkedMap<FoodProduct, Integer> recipeContent = parser.parse(recipe.getContentMap());
        StringBuilder content = new StringBuilder();
        recipeContent.forEach(((product, weight) -> content.append(product.getName() + " - " + weight + " g.  ") ));

        model.addAttribute("recipe", recipe);
        model.addAttribute("content", content);
        model.addAttribute("prots", recipe.getProts());
        model.addAttribute("fats", recipe.getFats());
        model.addAttribute("carbs", recipe.getCarbs());
        model.addAttribute("cals", recipe.getCalories());

        return "recipe-details";
    }
}

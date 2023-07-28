package com.foodfetish.picker.controllers;

import com.foodfetish.picker.models.Recipe;
import com.foodfetish.picker.repo.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class RecipeDeleteController {

    @Autowired
    private RecipeRepository recipeRepository;

    @GetMapping("/recipes/recipe/{id}/delete")
    public String deleteRecipe(@PathVariable(value = "id") long id, Model model) {
        recipeRepository.deleteById(id);
        return "redirect:/recipes";
    }

}

package com.foodfetish.picker.controllers;

import com.foodfetish.picker.models.FoodProduct;
import com.foodfetish.picker.models.Recipe;
import com.foodfetish.picker.repo.FoodProductRepository;
import com.foodfetish.picker.repo.RecipeRepository;
import com.foodfetish.picker.utils.ContentJsonParser;
import com.foodfetish.picker.utils.FoodProductByNameFinder;
import com.foodfetish.picker.utils.ProductNameFinder;
import com.foodfetish.picker.utils.RecipeCreator;
import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class RecipeEditController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private FoodProductRepository foodProductRepository;

    private String picked;
    private String sample = "А";
    private LinkedMap<FoodProduct,Integer> currentRecipe;

    @GetMapping("/recipes/recipe/{id}/edit")
    public String editRecipe(@PathVariable(value = "id") long id, Model model) {
        if (!recipeRepository.existsById(id)) {

            return "redirect:/recipes";
        }
        model.addAttribute("picked", picked);

        Optional<Recipe> current = recipeRepository.findById(id);
        Recipe recipe = current.get();
        model.addAttribute("recipe", recipe);


            ContentJsonParser parser = new ContentJsonParser(foodProductRepository);
            currentRecipe = parser.parse(recipe.getContentMap());
        model.addAttribute("content", currentRecipe);


        ProductNameFinder finder =new ProductNameFinder(foodProductRepository);
        model.addAttribute("products", finder.find(sample));

        return "recipe-edit";
    }

    @GetMapping("/recipes/recipe/{id}/edited")
    public String showChangedRecipe(@PathVariable(value = "id") long id, Model model) {
        if (!recipeRepository.existsById(id)) {

            return "redirect:/recipes";
        }
        model.addAttribute("picked", picked);

        Optional<Recipe> current = recipeRepository.findById(id);
        Recipe recipe = current.get();
        model.addAttribute("recipe", recipe);


        model.addAttribute("content", currentRecipe);


        ProductNameFinder finder =new ProductNameFinder(foodProductRepository);
        model.addAttribute("products", finder.find(sample));

        return "recipe-edit-edited";
    }



    @GetMapping("/recipes/recipe/{id}/edit/{prodId}/delete")
    public String deleteIngredient(@PathVariable(value = "id") long id, @PathVariable(value = "prodId") long prodId) {
        if (!recipeRepository.existsById(id)) {

            return "redirect:/recipes";
        }
        if (!foodProductRepository.existsById(prodId)) {

            return "redirect:/recipes";
        }
        FoodProduct product = foodProductRepository.findById(prodId).get();
        currentRecipe.remove(product);
        String edited = String.format("redirect:/recipes/recipe/%d/edited", id);
        return edited;
    }

    @PostMapping( path = "/recipes/recipe/{id}/edit")
    public String setSample(@PathVariable(value = "id") long id, @RequestParam String substring) {
        sample = substring;
        String address = String.format("redirect:/recipes/recipe/%d/edited", id);
    return address;
    }

    @GetMapping("/recipes/recipe/{id}/edit/pick/{prodId}")
    public String pickProduct(@PathVariable(value = "id") long id, @PathVariable(value = "prodId") long prodId) {
        picked = foodProductRepository.findById(prodId).get().getName();
        String address = String.format("redirect:/recipes/recipe/%d/edited", id);
        return address;
    }

    @GetMapping("/recipes/recipe/{id}/edit/apply")
    public String pickProduct(@PathVariable(value = "id") long id) {
        String address = String.format("redirect:/recipes/recipe/%d", id);
        if(!recipeRepository.existsById(id)){
            return "redirect:/recipes";
        }
        Recipe recipe = recipeRepository.findById(id).get();
        RecipeCreator creator = new RecipeCreator();
        recipe = creator.createRecipe(recipe.getName(), recipe.getAnons(), currentRecipe);
        recipe.setId(id);
        recipeRepository.save(recipe);

        return address;
    }

    @PostMapping( path = "/recipes/recipe/{id}/edit/change-name")
    public String changeName(@PathVariable(value = "id") long id, @RequestParam String name){
        String address = String.format("redirect:/recipes/recipe/%d/edited", id);

        if (name.isEmpty()){
            return address;
        }
        if(!recipeRepository.existsById(id)){
            return "redirect:/recipes";
        }
        Recipe recipe = recipeRepository.findById(id).get();
        recipe.setName(name);
        recipeRepository.save(recipe);

        return address;
    }

    @PostMapping( path = "/recipes/recipe/{id}/edit/change-anons")
    public String changeAnons(@PathVariable(value = "id") long id, @RequestParam String anons){
        if(!recipeRepository.existsById(id)){
            return "redirect:/recipes";
        }
        Recipe recipe = recipeRepository.findById(id).get();
        recipe.setAnons(anons);
        recipeRepository.save(recipe);
        String address = String.format("redirect:/recipes/recipe/%d/edited", id);
        return address;
    }

    @PostMapping( path = "/recipes/recipe/{id}/edit/{prodId}/change")
    public String changeIngredientValue( @PathVariable(value = "id") long id, @PathVariable(value = "prodId") long prodId, @RequestParam int weight){
        if(!recipeRepository.existsById(id)){
            return "redirect:/recipes";
        }
        if(!foodProductRepository.existsById(prodId)){
            return "redirect:/recipes";
        }
        FoodProduct product = foodProductRepository.findById(prodId).get();
        currentRecipe.computeIfPresent(product, (prod, prodWeight) -> weight);

        String address = String.format("redirect:/recipes/recipe/%d/edited", id);
        return address;
    }
    ///recipes/recipe/'+ ${recipe.id} +'/edit/add

    @PostMapping( path = "/recipes/recipe/{id}/edit/add")
    public String addIngredient( @PathVariable(value = "id") long id, @RequestParam String nameNewProduct, @RequestParam int weightNewProduct){
        String address = String.format("redirect:/recipes/recipe/%d/edited", id);
        if(!recipeRepository.existsById(id)){
            return "redirect:/recipes";
        }
        FoodProductByNameFinder finder = new FoodProductByNameFinder(foodProductRepository);
        FoodProduct product = finder.find(nameNewProduct);

        if(product == null){
            return address;
        }

        currentRecipe.computeIfPresent(product, (prod, prodWeight) -> prodWeight+weightNewProduct);
        currentRecipe.putIfAbsent(product, weightNewProduct);
        picked = null;
        sample = "";

        return address;
    }
}

package com.foodfetish.picker.controllers;

import com.foodfetish.picker.models.FoodProduct;
import com.foodfetish.picker.models.Recipe;
import com.foodfetish.picker.repo.FoodProductRepository;
import com.foodfetish.picker.repo.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Controller
public class RecipeRepositoryController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private FoodProductRepository foodProductRepository;

    private String content;
    private TreeMap<FoodProduct, Integer> recipe;
    private String sample;
    private String picked;

    @GetMapping("/recipes")
    public String getRecipes(Model model) {
        if (recipeRepository.count() > 0) {
            Iterable<Recipe> recipes = recipeRepository.findAll();
            model.addAttribute("recipes", recipes);
        } else {
            List<Recipe> recipes = new ArrayList<>();
            Recipe test = new Recipe("Вода з цукром");
            test.setCalories(150);
            test.addComponent(new FoodProduct("Цукор", 0.1, 0.1, 99.7), 10);
            test.addComponent(new FoodProduct("Вода", 0.01, 0.01, 0.01), 90);
            recipes.add(test);
            Iterable<Recipe> recipes1 = recipes;
            model.addAttribute("recipes", recipes1);
        }
        content = "Content: ";
        sample = "а";
        recipe = new TreeMap<>();

        //picked = "Enter food product";
        return "recipes";
    }

    @GetMapping("/recipes/recipe/{id}")
    public String getDetails(@PathVariable(value = "id") long id, Model model) {
        if (!recipeRepository.existsById(id)) {

            return "redirect:/recipes";
        }

        Optional<Recipe> current = recipeRepository.findById(id);
        Recipe recipe = current.get();
        String prots = String.format("%.2f", recipe.getProts());
        String fats = String.format("%.2f", recipe.getFats());
        String carbs = String.format("%.2f", recipe.getCarbs());

        model.addAttribute("recipe", recipe);
        model.addAttribute("prots", prots);
        model.addAttribute("fats", fats);
        model.addAttribute("carbs", carbs);
        model.addAttribute("cals", recipe.getCalories());

        return "recipe-details";
    }

    @GetMapping("/recipes/recipe/{id}/delete")
    public String deleteRecipe(@PathVariable(value = "id") long id, Model model) {
        recipeRepository.deleteById(id);
        return "redirect:/recipes";
    }

    @GetMapping("/recipes/compose/add/{name}")
    public String pickProduct(@PathVariable(value = "name") String name, Model model) {
        picked = name;
        return "redirect:/recipes/compose";
    }

    @GetMapping("/recipes/create")
    public String getNewRecipe(Model model) {
        return "create-recipe";
    }


    @GetMapping("/recipes/compose")
    public String createRecipe(Model model) {

        model.addAttribute("picked", picked);
        model.addAttribute("products", getSimilarNameProducts(sample));
        model.addAttribute("recipe", content);
        return "compose-recipe-test";
    }

    @PostMapping( path = "/recipes/compose")
    public  String findIgredient(@RequestParam String substring) {
        sample = substring;

        return "redirect:/recipes/compose";
    }

    public List<FoodProduct> getSimilarNameProducts(String substring){
        Iterable<FoodProduct> repo = foodProductRepository.findAll();

        List<FoodProduct> products = StreamSupport.stream(repo.spliterator(), false)
                .filter(product -> product.getName().toLowerCase().contains(substring.toLowerCase()))
                .limit(20)
                .collect(Collectors.toList());

        return products;
    }

    public void rebuildContent(){

        StringBuilder cont = new StringBuilder();
        recipe.forEach((k,v)-> cont.append(k.getName() + " - " + v + " g. "));
        content = "Content: " + cont;
    }

    @PostMapping(path = "/recipes/create/new") // Map ONLY POST Requests
    public String addRecipe(@RequestParam String name, @RequestParam Optional<String> anons) {
        try {
            Recipe composed = new Recipe(name);
            if (anons.isPresent()) {
                composed.setAnons(anons.get());
            } else {
                composed.setAnons("Without description");
            }
            recipe.forEach((product, weight) -> composed.addComponent(product, weight));
            composed.setProts(composed.getProts() / composed.getWeight() * 100);
            composed.setFats(composed.getFats() / composed.getWeight() * 100);
            composed.setCarbs(composed.getCarbs() / composed.getWeight() * 100);
            composed.setCalories(composed.countCalories());

            recipeRepository.save(composed);
        }catch (Exception e){
            return "redirect:/fail";
        }

        return "redirect:/recipes";

    }

    @PostMapping(path = "/recipes/compose/add-ingredient") // Map ONLY POST Requests
    public String addIngredient(@RequestParam String name, @RequestParam Integer weight) {
        picked = null;
        List<FoodProduct> base = new ArrayList<>();
        foodProductRepository.findAll().forEach(base::add);
        base.sort(Comparator.comparing(FoodProduct::getName).thenComparing(s -> s.getName().length()));
        Optional<FoodProduct> result = base
                .stream()
                .filter(s -> s.getName().toLowerCase().contains(name.toLowerCase()))
                .findFirst();
        if (result.isPresent()) {

            recipe.computeIfPresent(result.get(),(product, prodWeight) -> prodWeight+weight);

            recipe.putIfAbsent(result.get(), weight);

            rebuildContent();

            return "redirect:/recipes/compose";
        }

        return "compose-recipe-fail";
    }

    @GetMapping("/recipes/compose/del-last-ingredient")
    public String deletaLastIngredient (Model model) {
        recipe.pollLastEntry();
        rebuildContent();
        return "redirect:/recipes/compose";
    }
}

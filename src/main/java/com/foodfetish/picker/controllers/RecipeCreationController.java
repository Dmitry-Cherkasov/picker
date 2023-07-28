package com.foodfetish.picker.controllers;

import com.foodfetish.picker.models.FoodProduct;
import com.foodfetish.picker.models.Recipe;
import com.foodfetish.picker.repo.FoodProductRepository;
import com.foodfetish.picker.repo.RecipeRepository;
import com.foodfetish.picker.utils.ProductNameFinder;
import com.foodfetish.picker.utils.RecipeCreator;
import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;



@Controller
public class RecipeCreationController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private FoodProductRepository foodProductRepository;

    private String content;
    private LinkedMap<FoodProduct, Integer> recipeMap;
    private LinkedMap<Long, Integer> recipeIdMap;
    private String sample;
    private String picked;


    public void generateFields(){
        content = "";
        sample = "а";
        recipeMap = new LinkedMap<>();
        recipeIdMap = new LinkedMap<>();
    }

    private void rebuildContent(){

        StringBuilder cont = new StringBuilder();
        recipeMap.forEach((k, v)-> cont.append(k.getName() + " - " + v + " g. "));
        content = cont.toString();
    }

    @GetMapping("/recipes/compose")
    public String createRecipe(Model model) {
        if(content == null){
            generateFields();
        }
        ProductNameFinder finder = new ProductNameFinder(foodProductRepository);

        model.addAttribute("picked", picked);
        model.addAttribute("products", finder.find(sample));
        model.addAttribute("recipe", content);
        return "compose-recipe-test";
    }


    @PostMapping( path = "/recipes/compose")
    public  String findIgredient(@RequestParam String substring) {
        sample = substring;
        return "redirect:/recipes/compose";
    }


    @PostMapping(path = "/recipes/compose/add-ingredient")
    public String addIngredient(@RequestParam String name, @RequestParam Integer weight) {
        if(weight <=0){
            return "compose-recipe-fail-weight";
        }
        picked = null;
        ArrayList<FoodProduct> base = new ArrayList<>();
        foodProductRepository.findAll().forEach(base::add);
        base.sort(Comparator.comparing(FoodProduct::getName).thenComparing(s -> s.getName().length(), Comparator.reverseOrder()));
        Optional<FoodProduct> result = base
                .stream()
                .filter(s -> s.getName().toLowerCase().equalsIgnoreCase(name))
                .findFirst();
        if (result.isPresent()) {

            recipeMap.computeIfPresent(result.get(),(product, prodWeight) -> prodWeight+weight);
            recipeIdMap.computeIfPresent(result.get().getId(),(product, prodWeight) -> prodWeight+weight);


            recipeMap.putIfAbsent(result.get(), weight);
            recipeIdMap.putIfAbsent(result.get().getId(), weight);
            rebuildContent();

            return "redirect:/recipes/compose";
        }
        else {
            result = base
                    .stream()
                    .filter(s -> s.getName().toLowerCase().contains(name.toLowerCase()))
                    .findFirst();
            if (result.isPresent()) {

                recipeMap.computeIfPresent(result.get(), (product, prodWeight) -> prodWeight + weight);
                recipeIdMap.computeIfPresent(result.get().getId(), (product, prodWeight) -> prodWeight + weight);


                recipeMap.putIfAbsent(result.get(), weight);
                recipeIdMap.putIfAbsent(result.get().getId(), weight);
                rebuildContent();

                return "redirect:/recipes/compose";
            }

        }

        return "compose-recipe-fail";
    }


    @GetMapping("/recipes/compose/del-last-ingredient")
    public String deletaLastIngredient (Model model) {
        recipeMap.remove(recipeMap.lastKey());
        recipeIdMap.remove(recipeIdMap.lastKey());
        rebuildContent();
        return "redirect:/recipes/compose";
    }

    @GetMapping("/recipes/compose/clear-all")
    public String clearAllIngredients (Model model) {
        generateFields();
        rebuildContent();
        return "redirect:/recipes/compose";
    }

    @GetMapping("/recipes/compose/add/{id}")
    public String pickProduct(@PathVariable(value = "id") long id, Model model) {
        picked = foodProductRepository.findById(id).get().getName();
        return "redirect:/recipes/compose";
    }

    @GetMapping("/recipes/create")
    public String getNewRecipe(Model model) {
        return "create-recipe";
    }


    @PostMapping(path = "/recipes/create/new")
    public String addRecipe(@RequestParam String name, @RequestParam Optional<String> anons) {
        try {
            RecipeCreator creator = new RecipeCreator();
            Recipe composed = creator.createRecipe(name, anons.get(), recipeMap);

            recipeRepository.save(composed);
        }catch (Exception e){
            return "redirect:/fail";
        }
        generateFields();
        return "redirect:/recipes";

    }
}

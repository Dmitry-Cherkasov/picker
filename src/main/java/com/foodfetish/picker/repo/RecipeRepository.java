package com.foodfetish.picker.repo;

import com.foodfetish.picker.models.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {

}
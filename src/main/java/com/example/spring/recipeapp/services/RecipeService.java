package com.example.spring.recipeapp.services;

import com.example.spring.recipeapp.commands.RecipeCommand;
import com.example.spring.recipeapp.domain.Recipe;

import java.util.Set;

public interface RecipeService {

    Set<Recipe> getRecipes();

    Recipe findById(Long recipeID);

    RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand);

    RecipeCommand findCommandById(Long recipeID);

    void deleteById(Long idToDelete);
}

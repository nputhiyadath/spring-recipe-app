package com.example.spring.recipeapp.services;

import com.example.spring.recipeapp.commands.RecipeCommand;
import com.example.spring.recipeapp.domain.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeService {

    Flux<Recipe> getRecipes();

    Mono<Recipe> findById(String recipeID);

    Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipeCommand);

    Mono<RecipeCommand> findCommandById(String recipeID);

    Mono<Void> deleteById(String idToDelete);
}

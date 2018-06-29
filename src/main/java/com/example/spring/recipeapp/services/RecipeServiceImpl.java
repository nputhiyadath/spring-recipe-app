package com.example.spring.recipeapp.services;

import com.example.spring.recipeapp.commands.RecipeCommand;
import com.example.spring.recipeapp.converters.RecipeCommandToRecipe;
import com.example.spring.recipeapp.converters.RecipeToRecipeCommand;
import com.example.spring.recipeapp.domain.Recipe;
import com.example.spring.recipeapp.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeReactiveRepository recipeReactiveRepository;

    @Autowired
    private RecipeToRecipeCommand recipeToRecipeCommand;

    @Autowired
    private RecipeCommandToRecipe recipeCommandToRecipe;

    @Override
    public Flux<Recipe> getRecipes() {
        log.info("Finding all recipes");
        return recipeReactiveRepository.findAll();
    }

    @Override
    public Mono<Recipe> findById(String recipeID) {
        return recipeReactiveRepository.findById(recipeID);
    }

    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand recipeCommand) {
        return recipeReactiveRepository.save(recipeCommandToRecipe.convert(recipeCommand))
                .map(recipeToRecipeCommand::convert);
    }

    @Override
    public Mono<RecipeCommand> findCommandById(String recipeID) {
        return recipeReactiveRepository.findById(recipeID)
                .map(recipe -> {
                    RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);
                    recipeCommand.getIngredients().forEach(rc -> {
                        rc.setRecipeId(recipeCommand.getId());
                    });
                    return recipeCommand;
                });
    }

    @Override
    public Mono<Void> deleteById(String idToDelete) {
        recipeReactiveRepository.deleteById(idToDelete).block();
        return Mono.empty();
    }
}

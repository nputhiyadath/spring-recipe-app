package com.example.spring.recipeapp.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class CategoryTest {

    private Category category;

    @Before
    public void setUp() {
        category = new Category();
    }

    @Test
    public void getId() {
        Long idValue = 4L;
        category.setId(idValue);
        assertEquals(idValue, category.getId());
    }

    @Test
    public void getDescription() {
        String description = "American";
        category.setDescription(description);
        assertEquals(description, category.getDescription());
    }

    @Test
    public void getRecipes() {
        Recipe recipe = new Recipe();
        Long recipeID = 1L;
        recipe.setId(recipeID);
        Set<Recipe> recipeSet = new HashSet<>();
        recipeSet.add(recipe);
        category.setRecipes(recipeSet);
        assertEquals(recipeID, category.getRecipes().iterator().next().getId());
    }
}
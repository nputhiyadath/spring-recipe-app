package com.example.spring.recipeapp.controllers;

import com.example.spring.recipeapp.domain.Recipe;
import com.example.spring.recipeapp.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class IndexControllerTest {

    @InjectMocks
    private IndexController indexController;

    @Mock
    private Model model;

    @Mock
    private RecipeService recipeService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getIndexPage() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Set<Recipe> recipes = new HashSet<>();
        recipes.add(recipe);
        when(recipeService.getRecipes()).thenReturn(recipes);
        assertEquals("index", indexController.getIndexPage(model));
        verify(model, times(1)).addAttribute("recipes", recipes);
        verify(recipeService, times(1)).getRecipes();
    }

    @Test
    public void testMockMVC() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
}
package com.example.spring.recipeapp.controllers;

import com.example.spring.recipeapp.commands.RecipeCommand;
import com.example.spring.recipeapp.domain.Recipe;
import com.example.spring.recipeapp.exceptions.NotFoundException;
import com.example.spring.recipeapp.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RecipeControllerTest {

    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController recipeController;

    private MockMvc mockMvc;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController).setControllerAdvice(new ControllerExceptionHandler()).build();
    }

    @Test
    public void testGetRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId("1");

        when(recipeService.findById(anyString())).thenReturn(Mono.just(recipe));

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testGetRecipeNotFound() throws Exception {
        when(recipeService.findById(anyString())).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"))
                .andExpect(model().attributeExists("errorCodeAndMessage"))
                .andExpect(model().attribute("errorCodeAndMessage", "404 Not Found"));
    }

    @Test
    public void testGetRecipeNumberFormatException() throws Exception {
        when(recipeService.findById(anyString())).thenThrow(NumberFormatException.class);
        mockMvc.perform(get("/recipe/abc/show"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("404error"))
                .andExpect(model().attributeExists("errorCodeAndMessage"))
                .andExpect(model().attribute("errorCodeAndMessage", "400 Bad Request"));
    }

    @Test
    public void testGetNewRecipeForm() throws Exception {
        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testPostNewRecipeForm() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("2");

        when(recipeService.saveRecipeCommand(any())).thenReturn(Mono.just(recipeCommand));
        mockMvc.perform(post("/recipe/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "some string")
                .param("directions", "some directions"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/show"));

    }


    @Test
    public void testPostNewRecipeFormValidationFail() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId("2");

        when(recipeService.saveRecipeCommand(any())).thenReturn(Mono.just(command));

        mockMvc.perform(post("/recipe/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("cookTime", "3000")
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipe/recipeform"));
    }

    @Test
    public void testGetUpdateVie() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId("2");
        when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(recipeCommand));

        mockMvc.perform(get("/recipe/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testDeleteAction() throws Exception {
        mockMvc.perform(get("/recipe/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(recipeService, times(1)).deleteById(anyString());
    }
}
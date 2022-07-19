package hu.gehorvath.recipes.service;

import hu.gehorvath.recipes.exception.ItemAlreadyExistsException;
import hu.gehorvath.recipes.exception.ItemDoesNotExistException;
import hu.gehorvath.recipes.model.Recipe;
import hu.gehorvath.recipes.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
class RecipeServiceTest {
    @MockBean
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeService recipeService;

    @BeforeEach
    public void setup() {
        when(recipeRepository.save(any(Recipe.class)))
                .thenAnswer(invocation -> {
                    Recipe recipe = new Recipe(invocation.getArgument(0));
                    if(recipe.getId() == null)
                        recipe.setId(1L);
                    return recipe;
                });
    }

    @Test
    public void testCreateRecipe() throws Exception {
        //given
        Recipe recipe = createRecipe(null, "TEST", 4L, "potato, water, air", "cook", true);

        // when
        Recipe entityCreated = recipeService.createRecipe(recipe);

        // then
        assertEquals(entityCreated, new Recipe(1L, "TEST", 4L, "potato, water, air", "cook", true));
    }

    @Test
    public void testFailureCreateWithId() throws Exception {
        //given
        Recipe recipe = createRecipe(10L, "TEST", 4L, "potato, water, air", "cook", true);
        when(recipeRepository.existsById(recipe.getId())).thenReturn(true);

        Exception exception = null;

        // when
        try {
            Recipe entityCreated = recipeService.createRecipe(recipe);
        }catch (Exception ex){
            exception = ex;
        }

        // then
        assertInstanceOf(ItemAlreadyExistsException.class, exception);
    }

    @Test
    public void testUpdate() throws Exception {
        //given
        Recipe recipe = createRecipe(null, "TEST", 4L, "potato, water, air", "cook", true);
        when(recipeRepository.existsById(10L)).thenReturn(true);

        // when
        Recipe responseEntity = recipeService.updateRecipe(recipe, 10L);

        // then
        assertEquals(recipe, new Recipe(10L, "TEST", 4L, "potato, water, air", "cook", true));
    }

    @Test
    public void testUpdateNotExists() throws Exception {
        //given
        Recipe recipe = createRecipe(null, "TEST", 4L, "potato, water, air", "cook", true);
        when(recipeRepository.existsById(10L)).thenReturn(false);
        Exception exception = null;

        // when
        try {
            Recipe entityCreated = recipeService.updateRecipe(recipe, 10L);
        }catch (Exception ex){
            exception = ex;
        }

        // then
        assertInstanceOf(ItemDoesNotExistException.class, exception);
    }

    @Test
    public void testDelete() throws Exception {
        //given
        when(recipeRepository.existsById(1L)).thenReturn(true);

        // when
        recipeService.deleteRecipe(1L);

        // then
        verify(recipeRepository, times(1)).deleteById(any());
    }

    @Test
    public void testDeleteDoesNotExist() throws Exception {
        //given
        when(recipeRepository.existsById(10L)).thenReturn(false);

        Exception exception = null;

        // when
        try {
            recipeService.deleteRecipe(10L);
        }catch (Exception ex){
            exception = ex;
        }

        // then
        assertInstanceOf(ItemDoesNotExistException.class, exception);
    }

    @Test
    public void testGetRecipe() throws Exception {
        //given
        Recipe recipe = createRecipe(10L, "TEST", 4L, "potato, water, air", "cook", true);
        when(recipeRepository.findById(10L)).thenReturn(Optional.of(recipe));

        // when
        Recipe entity = recipeService.getRecipe(10L);

        // then
        assertEquals(entity, new Recipe(10L, "TEST", 4L, "potato, water, air", "cook", true));
    }

    @Test
    public void testGetNotFoundRecipe() throws Exception {
        //given
        when(recipeRepository.findById(10L)).thenReturn(Optional.empty());
        Exception exception = null;

        // when
        try {
            Recipe entity = recipeService.getRecipe(10L);
        }catch (Exception ex){
            exception = ex;
        }

        // then
        assertInstanceOf(ItemDoesNotExistException.class, exception);
    }

    @Test
    public void testListRecipe() throws Exception {
        //given
        Recipe recipe = createRecipe(10L, "TEST", 4L, "potato, water, air", "cook", true);
        Recipe recipe2 = createRecipe(20L, "TEST", 4L, "potato, water, air", "cook", true);
        when(recipeRepository.findAll()).thenReturn(List.of(recipe, recipe2));

        // when
        List<Recipe> entity = recipeService.listRecipe();

        // then
        assertEquals(entity.get(0), new Recipe(10L, "TEST", 4L, "potato, water, air", "cook", true));
        assertEquals(entity.get(1), new Recipe(20L, "TEST", 4L, "potato, water, air", "cook", true));
    }

    private Recipe createRecipe(Long id, String name, Long servings, String ingredients, String instructions, boolean vegan) {
        Recipe recipe = new Recipe();
        recipe.setId(id);
        recipe.setName(name);
        recipe.setServings(servings);
        recipe.setIngredients(ingredients);
        recipe.setInstructions(instructions);
        recipe.setVegan(vegan);
        return recipe;
    }

}
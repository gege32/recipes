package hu.gehorvath.recipes.service;

import hu.gehorvath.recipes.exception.ItemAlreadyExistsException;
import hu.gehorvath.recipes.model.Recipe;
import hu.gehorvath.recipes.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


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

        Exception exception = null;

        // when
        try {
            Recipe entityCreated = recipeService.createRecipe(recipe);
        }catch (Exception ex){
            exception = ex;
        }

        // then
        assertTrue(exception instanceof ItemAlreadyExistsException);
    }

    @Test
    public void testUpdate() throws Exception {
        //given
        Recipe recipe = createRecipe(null, "TEST", 4L, "potato, water, air", "cook", true);
        when(recipeRepository.existsById(10L)).thenReturn(true);

        // when
        Recipe responseEntity = recipeService.updateRecipe(recipe, 10L);

        // then
        assertEquals(responseEntity.getBody(), new Recipe(10L, "TEST", 4L, "potato, water, air", "cook", true));
    }

    @Test
    public void testDelete() throws Exception {
        //given
        when(recipeRepository.existsById(1L)).thenReturn(true);

        // when
        ResponseEntity responseEntity = recipeService.deleteRecipe(1L);

        // then
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testDeleteDoesNotExist() throws Exception {
        //given
        when(recipeRepository.existsById(10L)).thenReturn(false);

        // when
        ResponseEntity responseEntity = recipeService.deleteRecipe(10L);

        // then
        assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
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
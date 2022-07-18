package hu.gehorvath.recipes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.gehorvath.recipes.model.Recipe;
import hu.gehorvath.recipes.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
class RecipeControllerTest {

    @MockBean
    private RecipeRepository recipeRepository;

    @InjectMocks
    @Autowired
    private RecipeController recipeController;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        when(recipeRepository.save(any(Recipe.class)))
                .thenAnswer(invocation -> new Recipe(invocation.getArgument(0)));
    }

    @Test
    public void testCreateRecipe() throws Exception {
        //given
        Recipe recipe = createRecipe(null, "TEST", 4L, "potato, water, air", "cook", true);

        // when
        ResponseEntity<Recipe> responseEntity = recipeController.createRecipe(recipe);

        // then
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertEquals(responseEntity.getBody(), new Recipe(1L, "TEST", 4L, "potato, water, air", "cook", true));
    }

    @Test
    public void testFailureCreateWithId() throws Exception {
        //given
        Recipe recipe = createRecipe(10L, "TEST", 4L, "potato, water, air", "cook", true);

        // when
        ResponseEntity<Recipe> responseEntity = recipeController.createRecipe(recipe);

        // then
        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    private Recipe createRecipe(Long id, String name, Long servings, String ingredients, String instructions, boolean vegan) {
        Recipe recipe = new Recipe();
        recipe.setId(10L);
        recipe.setName("TEST");
        recipe.setServings(4L);
        recipe.setIngredients("potato, water, air");
        recipe.setInstructions("cook");
        recipe.setVegan(true);
        return recipe;
    }

}
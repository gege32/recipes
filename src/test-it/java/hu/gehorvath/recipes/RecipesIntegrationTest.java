package hu.gehorvath.recipes;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.gehorvath.recipes.model.Recipe;
import hu.gehorvath.recipes.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class RecipesIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    public void setup(){
        recipeRepository.deleteAll();
    }

    @Test
    public void testGetRecipe() throws Exception{
        //given

        Recipe recipe = createRecipe(null, "TEST", 4L, "potato, water, air", "cook", true);
        recipeRepository.save(recipe);


        //when-then
        mvc.perform( MockMvcRequestBuilders
                        .get("/recipe/{id}", recipe.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TEST"));

    }

    @Test
    public void testGetFailedRecipe() throws Exception{
        //given

        //when-then
        mvc.perform( MockMvcRequestBuilders
                        .get("/recipe/{id}", 419)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testListRecipe() throws Exception{
        //given

        recipeRepository.save(createRecipe(1L, "TEST13", 4L, "potato, water, air", "cook", true));
        recipeRepository.save(createRecipe(2L, "TEST15", 4L, "potato, water, air", "cook", true));


        //when-then
        mvc.perform( MockMvcRequestBuilders
                        .get("/recipe/list")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("TEST13"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("TEST15"));

    }

    @Test
    public void testCreate() throws Exception{
        //given

        Recipe recipe = createRecipe(null, "TEST3", 4L, "potato, water, air", "cook", true);

        //when-then
        mvc.perform( MockMvcRequestBuilders
                        .post("/recipe")
                        .content(asJsonString(recipe))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TEST3"));
    }

    @Test
    public void testCreateFailAlreadyExists() throws Exception{
        //given

        Recipe recipe = createRecipe(null, "TEST4", 4L, "potato, water, air", "cook", true);
        Recipe save = recipeRepository.save(recipe);

        //when-then
        mvc.perform( MockMvcRequestBuilders
                        .post("/recipe")
                        .content(asJsonString(save))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdate() throws Exception{
        //given

        Recipe toUpdate = createRecipe(null, "TEST3", 4L, "potato, water, air", "cook", true);
        Recipe save = recipeRepository.save(toUpdate);

        Recipe updated = createRecipe(null, "TEST3", 8L, "potato, water, air", "cook", true);

        //when-then
        mvc.perform( MockMvcRequestBuilders
                        .put("/recipe/{id}", save.getId())
                        .content(asJsonString(updated))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.servings").value(8));
    }

    @Test
    public void testUpdateFailed() throws Exception{
        //given

        Recipe updated = createRecipe(null, "TEST3", 8L, "potato, water, air", "cook", true);

        //when-then
        mvc.perform( MockMvcRequestBuilders
                        .put("/recipe/{id}", 123)
                        .content(asJsonString(updated))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFilter_vegan() throws Exception{
        //given

        recipeRepository.save(createRecipe(null, "TEST1", 4L, "potato, water, air", "cook", true));
        recipeRepository.save(createRecipe(null, "TEST2", 8L, "potato, milk, butter", "bake", false));

        //when-then
        mvc.perform( MockMvcRequestBuilders
                        .get("/recipe")
                        .queryParam("vegan", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("TEST1"));
    }

    @Test
    public void testFilter_ingredients_include() throws Exception{
        //given

        recipeRepository.save(createRecipe(null, "TEST1", 4L, "potato, water, air", "cook", true));
        recipeRepository.save(createRecipe(null, "TEST2", 8L, "potato, milk, butter", "bake", false));

        //when-then
        mvc.perform( MockMvcRequestBuilders
                        .get("/recipe")
                        .queryParam("ingredient", "milk")
                        .queryParam("include", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("TEST2"));
    }

    @Test
    public void testFilter_ingredients_exclude() throws Exception{
        //given

        recipeRepository.save(createRecipe(null, "TEST1", 4L, "potato, water, air", "cook", true));
        recipeRepository.save(createRecipe(null, "TEST2", 8L, "potato, milk, butter", "bake", false));

        //when-then
        mvc.perform( MockMvcRequestBuilders
                        .get("/recipe")
                        .queryParam("ingredient", "water")
                        .queryParam("include", "false")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("TEST2"));
    }

    @Test
    public void testFilter_multiple() throws Exception{
        //given

        recipeRepository.save(createRecipe(null, "TEST1", 4L, "potato, water, air", "cook", true));
        recipeRepository.save(createRecipe(null, "TEST2", 8L, "potato, milk, butter", "bake", true));
        recipeRepository.save(createRecipe(null, "TEST3", 8L, "potato, milk, bacon", "bake", false));
        recipeRepository.save(createRecipe(null, "TEST4", 5L, "potato, milk, bacon", "bake", false));
        recipeRepository.save(createRecipe(null, "TEST5", 8L, "potato, milk, butter", "cook", true));

        //when-then
        mvc.perform( MockMvcRequestBuilders
                        .get("/recipe")
                        .queryParam("vegan", "true")
                        .queryParam("servings", "8")
                        .queryParam("instructions", "bake")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("TEST2"));
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

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}

package hu.gehorvath.recipes.service;

import hu.gehorvath.recipes.exception.ItemAlreadyExistsException;
import hu.gehorvath.recipes.exception.ItemDoesNotExistException;
import hu.gehorvath.recipes.model.Recipe;
import hu.gehorvath.recipes.repository.RecipeRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Recipe createRecipe(Recipe recipe) {
        if(recipe.getId() != null && recipeRepository.existsById(recipe.getId())) throw new ItemAlreadyExistsException("Cannot create Recipe, ID already exists");
        return recipeRepository.save(recipe);
    }

    public Recipe updateRecipe(Recipe recipe, Long id) {
        if (!recipeRepository.existsById(id)) throw new ItemDoesNotExistException();
        recipe.setId(id);
        return recipeRepository.save(recipe);
    }

    public void deleteRecipe(Long id) {
        if (!recipeRepository.existsById(id)) throw new ItemDoesNotExistException();
        recipeRepository.deleteById(id);
    }

    public Recipe getRecipe(Long id) {
        return recipeRepository.findById(id).orElseThrow(ItemDoesNotExistException::new);
    }

    public List<Recipe> listRecipe() {
        return recipeRepository.findAll();
    }

    public List<Recipe> filterRecipe(Boolean vegan, Long servings, List<String> ingredients, Boolean include, String instructions) {
        Specification<Recipe> specification = Specification.where(veganIs(vegan)).and(servingSizeIs(servings)).and(instructionsContains(instructions));

        if (ingredients != null) {
            ingredients.forEach(ingredient -> {
                specification.and(include == null ? ingredientsContains(ingredient) : include ? ingredientsContains(ingredient) : ingredientsNotContains(ingredient));
            });
        }
        return recipeRepository.findAll(specification);
    }

    private static Specification<Recipe> veganIs(Boolean vegan) {
        return (root, query, builder) -> vegan == null ? builder.conjunction() : builder.equal(root.get("vegan"), vegan);
    }

    private static Specification<Recipe> servingSizeIs(Long servings) {
        return (root, query, builder) -> servings == null ? builder.conjunction() : builder.equal(root.get("servings"), servings);
    }

    private static Specification<Recipe> ingredientsContains(String ingredients) {
        return (root, query, builder) -> ingredients == null ? builder.conjunction() : builder.like(root.get("ingredients"), ingredients);
    }

    private static Specification<Recipe> ingredientsNotContains(String ingredients) {
        return (root, query, builder) -> ingredients == null ? builder.conjunction() : builder.notLike(root.get("ingredients"), ingredients);
    }

    private static Specification<Recipe> instructionsContains(String instrucations) {
        return (root, query, builder) -> instrucations == null ? builder.conjunction() : builder.like(root.get("instructions"), instrucations);
    }

}

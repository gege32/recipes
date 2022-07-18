package hu.gehorvath.recipes.controller;

import hu.gehorvath.recipes.model.Recipe;
import hu.gehorvath.recipes.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class RecipeController {

    private static final Logger LOG = LoggerFactory.getLogger(RecipeController.class);

    private final RecipeRepository recipeRepository;

    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }


    @GetMapping("/recipe/list")
    public ResponseEntity<List<Recipe>> listRecipes() {
        LOG.info("listRecipes");
        return ResponseEntity.ok(StreamSupport.stream(recipeRepository.findAll().spliterator(), false).collect(Collectors.toList()));
    }

    @GetMapping("/recipe/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable("id") Long id) {
        LOG.info("getRecipe");
        Optional<Recipe> byId = recipeRepository.findById(id);
        if (byId.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(byId.get());
    }

    @GetMapping("/recipe")
    public ResponseEntity<List<Recipe>> filterRecipe(@RequestParam(value = "vegan", required = false) Boolean vegan,
                                                     @RequestParam(value = "servings", required = false) Long servings,
                                                     @RequestParam(value = "ingredient", required = false) List<String> ingredients,
                                                     @RequestParam(value = "include", required = false) Boolean include,
                                                     @RequestParam(value = "instructions", required = false) String instructions) {
        LOG.info("filterRecipe");
        Specification<Recipe> specification = Specification.where(veganIs(vegan)).and(servingSizeIs(servings)).and(instructionsContains(instructions));

        if (ingredients != null) {
            ingredients.forEach(ingredient -> {
                specification.and(include == null ? ingredientsContains(ingredient) : include ? ingredientsContains(ingredient) : ingredientsNotContains(ingredient));
            });
        }
        return ResponseEntity.ok(recipeRepository.findAll(specification));

    }

    @PostMapping("/recipe/update")
    public ResponseEntity<Recipe> updateRecipe(@RequestBody @Valid Recipe recipe) {
        LOG.info("updateRecipe");
        return ResponseEntity.ok(recipeRepository.save(recipe));
    }

    @PostMapping("/recipe/create")
    public ResponseEntity<Recipe> createRecipe(@RequestBody @Valid Recipe recipe) {
        LOG.info("createRecipe");
        if (recipe.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(recipeRepository.save(recipe));
    }

    @GetMapping("/recipe/delete/{id}")
    public ResponseEntity deleteRecipe(@PathVariable("id") Long id) {
        LOG.info("deleteRecipe");
        recipeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public static Specification<Recipe> veganIs(Boolean vegan) {
        return (root, query, builder) -> vegan == null ? builder.conjunction() : builder.equal(root.get("vegan"), vegan);
    }

    public static Specification<Recipe> servingSizeIs(Long servings) {
        return (root, query, builder) -> servings == null ? builder.conjunction() : builder.equal(root.get("servings"), servings);
    }

    public static Specification<Recipe> ingredientsContains(String ingredients) {
        return (root, query, builder) -> ingredients == null ? builder.conjunction() : builder.like(root.get("ingredients"), ingredients);
    }

    public static Specification<Recipe> ingredientsNotContains(String ingredients) {
        return (root, query, builder) -> ingredients == null ? builder.conjunction() : builder.notLike(root.get("ingredients"), ingredients);
    }

    public static Specification<Recipe> instructionsContains(String instrucations) {
        return (root, query, builder) -> instrucations == null ? builder.conjunction() : builder.like(root.get("instructions"), instrucations);
    }

}

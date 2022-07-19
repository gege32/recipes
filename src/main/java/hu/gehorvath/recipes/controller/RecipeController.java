package hu.gehorvath.recipes.controller;

import hu.gehorvath.recipes.model.Recipe;
import hu.gehorvath.recipes.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Recipe controller")
@RestController
public class RecipeController {

    private static final Logger LOG = LoggerFactory.getLogger(RecipeController.class);

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Operation(summary = "List all the recipes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe list generated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Recipe.class))})})
    @GetMapping("/recipe/list")
    public List<Recipe> listRecipes() {
        LOG.info("listRecipes");
        return recipeService.listRecipe();
    }

    @Operation(summary = "Get a single recipe by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the recipe",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Recipe.class))}),
            @ApiResponse(responseCode = "404", description = "Recipe not found",
                    content = @Content)
    })
    @GetMapping("/recipe/{id}")
    public Recipe getRecipe(@PathVariable("id") Long id) {
        LOG.info("getRecipe");
        return recipeService.getRecipe(id);
    }

    @Operation(summary = "Filter the recipes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the recipe",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Recipe.class))})
    })
    @GetMapping("/recipe")
    public List<Recipe> filterRecipe(@RequestParam(value = "vegan", required = false) Boolean vegan,
                                                     @RequestParam(value = "servings", required = false) Long servings,
                                                     @RequestParam(value = "ingredient", required = false) List<String> ingredients,
                                                     @RequestParam(value = "include", required = false) Boolean include,
                                                     @RequestParam(value = "instructions", required = false) String instructions) {
        LOG.info("filterRecipe");
        return recipeService.filterRecipe(vegan, servings, ingredients, include, instructions);
    }

    @Operation(summary = "Update a recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Recipe.class))}),
            @ApiResponse(responseCode = "404", description = "Recipe not found",
                    content = @Content)
    })
    @PutMapping("/recipe/{id}")
    public Recipe updateRecipe(@RequestBody @Valid Recipe recipe, @PathVariable Long id) {
        LOG.info("updateRecipe");
        return recipeService.updateRecipe(recipe, id);
    }

    @Operation(summary = "Create a recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Recipe.class))}),
            @ApiResponse(responseCode = "400", description = "Item already exists",
                    content = @Content)
    })
    @PostMapping("/recipe")
    public Recipe createRecipe(@RequestBody @Valid Recipe recipe) {
        LOG.info("createRecipe");
        return recipeService.createRecipe(recipe);
    }

    @Operation(summary = "Delete a recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Recipe.class))}),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content)
    })
    @DeleteMapping("/recipe/{id}")
    public String deleteRecipe(@PathVariable("id") Long id) {
        LOG.info("deleteRecipe");
        recipeService.deleteRecipe(id);
        return "OK";
    }

}

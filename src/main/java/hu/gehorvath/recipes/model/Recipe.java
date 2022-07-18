package hu.gehorvath.recipes.model;

import io.swagger.v3.oas.annotations.Hidden;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class Recipe {

    @Id
    @GeneratedValue
    @Hidden
    private Long id;
    @NotBlank
    private String name;

    @NotNull
    private Long servings;

    @NotBlank
    private String ingredients;

    @NotBlank
    private String instructions;

    private boolean vegan;

    public Recipe() {
    }

    public Recipe(Long id, String name, Long servings, String ingredients, String instructions, boolean vegan) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.vegan = vegan;
    }

    public Recipe(Recipe recipe) {
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.servings = recipe.getServings();
        this.ingredients = recipe.getIngredients();
        this.instructions = recipe.getInstructions();
        this.vegan = recipe.isVegan();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getServings() {
        return servings;
    }

    public void setServings(Long servings) {
        this.servings = servings;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public boolean isVegan() {
        return vegan;
    }

    public void setVegan(boolean vegan) {
        this.vegan = vegan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return vegan == recipe.vegan && Objects.equals(id, recipe.id) && Objects.equals(name, recipe.name) && Objects.equals(servings, recipe.servings) && Objects.equals(ingredients, recipe.ingredients) && Objects.equals(instructions, recipe.instructions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

package cloud.ptl.indexer.api.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{id}")
    @Operation(
            method = "GET",
            description = "Get single item by its id"
    )
    public CategoryDTO get(
            @Parameter(description = "Id to resolve") @PathVariable(name = "id") Long id
    ) {
        return CategoryDTO.of(categoryService.findById(id));
    }

    @GetMapping("/")
    @Operation(
            method = "GET",
            description = "Get all categories"
    )
    public List<CategoryDTO> getAll() {
        return categoryService.getAll().stream().map(CategoryDTO::of).toList();
    }

    @PostMapping("/")
    @Operation(
            method = "POST",
            description = "Save single category"
    )
    public CategoryDTO post(@RequestBody @Valid CategoryDTO categoryDTO) {
        return CategoryDTO.of(categoryService.save(categoryDTO.toEntity()));
    }

    @PutMapping("/")
    @Operation(
            method = "PUT",
            description = "Update single item"
    )
    public CategoryDTO update(@RequestBody CategoryDTO categoryDTO) {
        return CategoryDTO.of(categoryService.save(categoryDTO.toEntity()));
    }

    @DeleteMapping("/{id}")
    @Operation(
            method = "DELETE",
            description = "Delete category checking if it has no items in it"
    )
    @ResponseStatus(HttpStatus.OK)
    public void delete(
            @Parameter(description = "Id to resolve") @PathVariable(name = "id") Long id
    ) {
        categoryService.delete(id);
    }
}

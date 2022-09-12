package cloud.ptl.indexer.api.category;

import cloud.ptl.indexer.model.CategoryEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

@Data
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    @Schema(
            description = "Category id",
            example = "1"
    )
    private Long id;
    @NotNull
    @Schema(
            description = "Name of category",
            example = "food"
    )
    private String name;
    @Schema(
            description = "Shor description of category",
            nullable = true,
            example = "This category contains food items"
    )
    private String description;

    public static CategoryDTO of(CategoryEntity categoryEntity) {
        return CategoryDTO.builder()
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .description(categoryEntity.getDescription())
                .build();
    }

    public CategoryEntity toEntity() {
        return CategoryEntity.builder()
                .id(getId())
                .name(getName())
                .description(getDescription())
                .build();
    }
}

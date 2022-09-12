package cloud.ptl.indexer.api.category;

import cloud.ptl.indexer.model.CategoryEntity;
import cloud.ptl.indexer.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryEntity save(CategoryEntity category) {
        return categoryRepository.save(category);
    }

    public List<CategoryEntity> getAll() {
        return (List<CategoryEntity>) categoryRepository.findAll();
    }

    public CategoryEntity findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no item with id " + id));
    }

    public void delete(Long id) {
        CategoryEntity category = findById(id);
        if (!category.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category has some items assigned");
        }
        categoryRepository.deleteById(id);
    }
}

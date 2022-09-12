package cloud.ptl.indexer.repositories;

import cloud.ptl.indexer.model.CategoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Long> {
}

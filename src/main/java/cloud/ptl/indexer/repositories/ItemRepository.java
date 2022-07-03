package cloud.ptl.indexer.repositories;

import cloud.ptl.indexer.model.ItemEntity;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<ItemEntity, Long> {
}

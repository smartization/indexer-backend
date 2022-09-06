package cloud.ptl.indexer.repositories;

import cloud.ptl.indexer.model.ItemEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemRepository extends CrudRepository<ItemEntity, Long> {
    List<ItemEntity> findAllByStoragePlace_Id(Long placeId);
    Long countByStoragePlace_Id(Long placeId);
}

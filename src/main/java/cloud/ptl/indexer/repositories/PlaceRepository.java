package cloud.ptl.indexer.repositories;

import cloud.ptl.indexer.model.PlaceEntity;
import org.springframework.data.repository.CrudRepository;

public interface PlaceRepository extends CrudRepository<PlaceEntity, Long> {
}

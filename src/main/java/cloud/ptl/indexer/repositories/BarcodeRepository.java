package cloud.ptl.indexer.repositories;

import cloud.ptl.indexer.model.BarcodeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BarcodeRepository extends CrudRepository<BarcodeEntity, Long> {
    boolean existsByValue(String value);
    Optional<BarcodeEntity> findByValue(String value);
}

package cloud.ptl.indexer.repositories;

import cloud.ptl.indexer.model.ItemEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface ItemRepository extends CrudRepository<ItemEntity, Long> {
    List<ItemEntity> findByDueDateBefore(LocalDate now);
    List<ItemEntity> findByDueDateIsBetween(LocalDate startDate, LocalDate endDate);
}

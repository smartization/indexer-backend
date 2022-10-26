package cloud.ptl.indexer.repositories;

import cloud.ptl.indexer.model.ItemEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface ItemRepository extends CrudRepository<ItemEntity, Long> {
    List<ItemEntity> findByDueDateBefore(LocalDate now);
    List<ItemEntity> findByDueDateIsBetween(LocalDate startDate, LocalDate endDate);
    List<ItemEntity> findAllByStoragePlace_Id(Long placeId);
    List<ItemEntity> findAllByCategory_Id(Long categoryId);
    Long countByStoragePlace_Id(Long placeId);
    Long countByCategory(Long categoryId);
    @Query("SELECT item from ItemEntity item WHERE item.quantity <= item.notifyQuantity")
    List<ItemEntity> findByQuantityLessThanEqual();

}

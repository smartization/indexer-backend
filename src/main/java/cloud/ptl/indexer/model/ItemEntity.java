package cloud.ptl.indexer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String barcode;
    private Integer quantity;
    @Enumerated(EnumType.STRING)
    private BarcodeType barcodeType;
    @ManyToOne
    private PlaceEntity storagePlace;
    @CreatedDate
    private Timestamp dateAdded;
    @LastModifiedDate
    private Timestamp lastModifiedDate;
    private LocalDate dueDate;

    public String stringRepresentationWithDueDate(){
        String itemDescription = this.getName();
        if(this.getStoragePlace() != null){
            itemDescription += " stored in " + this.getStoragePlace();
        }
        itemDescription += " with due date: " + this.getDueDate();
        return itemDescription;
    }

    public Integer incrementQuantity() {
        return ++this.quantity;
    }

    public Integer decrementQuantity() {
        return --this.quantity;
    }
}


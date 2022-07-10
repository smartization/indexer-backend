package cloud.ptl.indexer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

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
    @Enumerated(EnumType.STRING)
    private BarcodeType barcodeType;
    @ManyToOne
    private PlaceEntity storagePlace;
    @CreatedDate
    private Date dateAdded;
    @LastModifiedDate
    private Date lastModifiedDate;
}

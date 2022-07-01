package cloud.ptl.indexer.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Document
@Data
@Builder
public class ItemEntity {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    private String description;
    private String barcode;
    @Enumerated(EnumType.STRING)
    private BarcodeType barcodeType;
    @CreatedDate
    private Date dateAdded;
    @LastModifiedDate
    private Date lastModifiedDate;
}

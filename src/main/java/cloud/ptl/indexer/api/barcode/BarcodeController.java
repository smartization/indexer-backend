package cloud.ptl.indexer.api.barcode;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/barcodes")
@RestController
@RequiredArgsConstructor
public class BarcodeController {
    private final BarcodeService barcodeService;

    @GetMapping("/{barcode}")
    @Operation(
            summary = "Resolve single barcode",
            description = "For single barcode it will try to resolve via google service"
    )
    public BarcodeDTO fetch(
            @PathVariable("barcode") String barcode
    ) {
        return barcodeService.fetch(barcode);
    }
}

package cloud.ptl.indexer.api.item.validators;

import cloud.ptl.indexer.api.item.ItemDTO;
import cloud.ptl.indexer.model.BarcodeType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BarcodeLengthValidator implements ConstraintValidator<BarcodeLength, ItemDTO> {
    @Override
    public boolean isValid(ItemDTO itemDTO, ConstraintValidatorContext constraintValidatorContext) {
        final String barcode = itemDTO.getBarcode();
        final BarcodeType barcodeType = itemDTO.getBarcodeType();
        // remove if more code types added
        //noinspection SwitchStatementWithTooFewBranches
        switch (barcodeType) {
            case EAN -> {
                return barcode.length() == 13;
            }
        }
        return false;
    }
}

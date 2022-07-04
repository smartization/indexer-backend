package cloud.ptl.indexer.api.item.validators;

import cloud.ptl.indexer.api.item.ItemDTO;
import cloud.ptl.indexer.model.BarcodeType;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;


public class BarcodeFormatValidator implements ConstraintValidator<BarcodeFormat, ItemDTO> {
    @Override
    public void initialize(BarcodeFormat constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ItemDTO itemDTO, ConstraintValidatorContext constraintValidatorContext) {
        final String barcode = itemDTO.getBarcode();
        final BarcodeType barcodeType = itemDTO.getBarcodeType();
        // remove if more code types added
        //noinspection SwitchStatementWithTooFewBranches
        switch (barcodeType) {
            case EAN -> {
                return validateEan(barcode);
            }
        }
        return false;
    }

    /**
     * The check digit for an EAN-13 code is calculated as follows:
     * <p>
     * Count digit positions from the left to the right, starting at 1.
     * Sum all the digits in odd positions. (In the example shown in Figure 1, this is 9 + 8 + 5 + 1 + 2 + 5 = 30 – note that the final 5 is not included since this is the check digit, which is what we are currently trying to calculate.)
     * Sum all the digits in even positions and multiply the result by 3. (In the example, this is (7 + 0 + 2 + 4 + 5 + 7) × 3 = 75.)
     * Add the results of step 2 and step 3, and take just the final digit (the ‘units’ digit) of the answer. This is equivalent to taking the answer modulo-10. (In the example, the sum is 30 + 75 = 105, so the units digit is 5.)
     * If the answer to step 4 was 0, this is the check digit. Otherwise, the check digit is given by ten minus the answer from step 4. (In the example, this is 10 – 5 = 5.)
     * The check digit is appended to the right of the 12 identification digits. The check digit can have any value from 0 to 9.
     *
     * @param barcode barcode to verify
     */
    private boolean validateEan(String barcode) {
        try {
            final String barcodeWithoutLast = barcode.substring(0, barcode.length() - 1);
            final List<Integer> digits = Arrays.stream(barcodeWithoutLast.split("(?!^)")).map(Integer::valueOf)
                    .toList();
            final int evenSum = IntStream.iterate(0, n -> n < digits.size(), n -> n + 2).map(digits::get).sum();
            final int oddSum = IntStream.iterate(1, n -> n < digits.size(), n -> n + 2).map(digits::get).sum();
            final int unit = evenSum + oddSum * 3;
            final int check = 10 - (unit % 10);
            final int lastDigit = barcode.charAt(barcode.length() - 1) - 48;
            return check == lastDigit;
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Number format exception " + e.getMessage());
        }
    }
}

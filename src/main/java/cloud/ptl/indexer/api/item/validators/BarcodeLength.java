package cloud.ptl.indexer.api.item.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = BarcodeLengthValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BarcodeLength {
    String message() default "Barcode has wrong length";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

package cloud.ptl.indexer.api.firebase;

import cloud.ptl.indexer.api.notification.MediaEnum;
import cloud.ptl.indexer.api.notification.NotificationMediator;
import cloud.ptl.indexer.api.notification.TemplateEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/firebase")
@RequiredArgsConstructor
public class FirebaseController {
    private final FirebaseService firebaseService;
    private final NotificationMediator notificationMediator;
    @Operation(
            summary = "Update firebase token",
            method = "PATCH"
    )
    @PatchMapping("/token/{oldToken}/update/{newToken}")
    public ResponseEntity<String> updateToken(
            @Parameter(description = "old token") @PathVariable(value = "oldToken", required = false) String oldToken,
            @Parameter(description = "new token") @PathVariable("newToken") String newToken
    ) {
        firebaseService.updateToken(oldToken, newToken);
        return ResponseEntity.ok("Token updated");
    }
    @GetMapping("/notify/expired-products")
    public void sendNotification() throws Exception {
        notificationMediator.sendNotification(TemplateEnum.EXPIRED_PRODUCTS, MediaEnum.FIREBASE);
    }

    @GetMapping("/notify/soon-expired-products/{daysNum}")
    public void sendNotification(@PathVariable int daysNum) throws Exception {
        HashMap<Object, Object> params = new HashMap<>();
        params.put("daysNum", daysNum);
        notificationMediator.sendNotification(TemplateEnum.SOON_EXPIRED_PRODUCTS, MediaEnum.FIREBASE, params);
    }

    @GetMapping("/notify/low-quantity-products")
    public void sendEmailWithLowQuantityProducts() throws Exception {
        notificationMediator.sendNotification(TemplateEnum.QUANTITY, MediaEnum.FIREBASE);
    }
}

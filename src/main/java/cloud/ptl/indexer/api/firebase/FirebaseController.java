package cloud.ptl.indexer.api.firebase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/firebase")
@RequiredArgsConstructor
public class FirebaseController {
    private final FirebaseService firebaseService;

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
}

package cloud.ptl.indexer.api.item.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Command created during updating item
 */
@Slf4j
public class UpdateSyncCommand extends FirebaseItemSyncCommand {
    public UpdateSyncCommand(Long id) {
        super(id);
    }

    @Override
    public void execute() {
        Map<String, String> data = new HashMap<>();
        data.put("id", id.toString());
        data.put("type", "update");
        log.info("Sending update message to firebase: {}", data);
        firebaseService.sendDataMessageToAll(data);
    }
}

package cloud.ptl.indexer.api.item.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Command created during deleting item
 */
@Slf4j
public class DeleteSyncCommand extends FirebaseItemSyncCommand {
    public DeleteSyncCommand(Long id) {
        super(id);
    }

    @Override
    public void execute() {
        Map<String, String> data = new HashMap<>();
        data.put("id", this.id.toString());
        data.put("type", "delete");
        log.info("Sending delete message to firebase: {}", data);
        firebaseService.sendDataMessageToAll(data);
    }
}

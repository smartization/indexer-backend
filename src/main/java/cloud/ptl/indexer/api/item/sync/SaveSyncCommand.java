package cloud.ptl.indexer.api.item.sync;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Command created during saving item
 */
@Slf4j
public class SaveSyncCommand extends FirebaseItemSyncCommand {
    public SaveSyncCommand(Long id) {
        super(id);
    }

    @Override
    public void execute() {
        Map<String, String> data = new HashMap<>();
        data.put("id", this.id.toString());
        data.put("type", "save");
        log.info("Sending save message to firebase: {}", data);
        firebaseService.sendDataMessageToAll(data);
    }
}

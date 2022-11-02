package cloud.ptl.indexer.api.item.sync;

import cloud.ptl.indexer.api.firebase.FirebaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * This class is responsible for executing item sync commands
 * so each device subscribed to firebase or other message broker will be notified
 */
@Service
@RequiredArgsConstructor
public class ItemSyncEventExecutor {
    private final FirebaseService firebaseService;

    public void execute(ItemSyncCommand command) {
        if (command instanceof FirebaseItemSyncCommand) {
            ((FirebaseItemSyncCommand) command).setFirebaseService(this.firebaseService);
        }
        command.execute();
    }
}

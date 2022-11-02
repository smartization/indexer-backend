package cloud.ptl.indexer.api.item.sync;

import cloud.ptl.indexer.api.firebase.FirebaseService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public abstract class FirebaseItemSyncCommand implements ItemSyncCommand {
    protected final Long id;
    @Setter
    protected FirebaseService firebaseService;
}

package cloud.ptl.indexer.api.notification;

public enum MediaEnum {
    FIREBASE("firebase"),
    MAIL("mail");
    private final String media;
    MediaEnum(String media) {
        this.media = media;
    }

    public String getValue() {
        return media;
    }
}

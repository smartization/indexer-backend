package cloud.ptl.indexer.api.notification;

public enum TemplateEnum {
    QUANTITY("quantity"),
    EXPIRED_PRODUCTS("expiredProducts"),
    SOON_EXPIRED_PRODUCTS("soonExpiredProducts");
    private final String template;
    TemplateEnum(String template) {
        this.template = template;
    }

    public String getValue() {
        return template;
    }
}

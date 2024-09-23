package arc.haldun.quotes;

public class Quote {

    public static final int MAGIC_NUMBER = 0x746F7571;

    private String content;
    private String sourceType;
    private Source source;

    public Quote(String content, String sourceType, Source source) {
        this.content = content;
        this.sourceType = sourceType;
        this.source = source;
    }

    public String getContent() {
        return content;
    }

    public String getSourceType() {
        return sourceType;
    }

    public Source getSource() {
        return source;
    }

    public enum SourceType {
        BOOK("kitap"),
        MOVIE("film"),
        SERIES("dizi"),
        PERSON("kişi");

        private final String val;

        SourceType(String val) {
            this.val = val;
        }

        public String getValue() {
            return val;
        }
    }
}

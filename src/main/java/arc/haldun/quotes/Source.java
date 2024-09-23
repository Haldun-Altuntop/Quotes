package arc.haldun.quotes;

public class Source {

    private final String sourceName;
    private String sourceOwner;

    public Source(String sourceName, String sourceOwner) {
        this.sourceName = sourceName;
        this.sourceOwner = sourceOwner;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getSourceOwner() {
        return sourceOwner;
    }
}

package arc.haldun.quotes;

import arc.haldun.io.FileWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class QuoteWriter {

    private final String filePath;
    private final File quoteFile;
    private final FileWriter fileWriter;

    public QuoteWriter(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        this.quoteFile = new File(filePath);
        this.fileWriter = new FileWriter(filePath);
    }

    public void writeQuote(Quote q) throws IOException {
        initFile();

        fileWriter.writeString(Labels.quote);

        // Write content
        fileWriter.writeString(Labels.content);
        fileWriter.writeString(q.getContent());

        // Write source type
        fileWriter.writeString(Labels.sourceType);
        fileWriter.writeString(q.getSourceType());

        // Write source name
        fileWriter.writeString(Labels.sourceName);
        fileWriter.writeString(q.getSource().getSourceName());

        // Write source owner
        fileWriter.writeString(Labels.sourceOwner);
        fileWriter.writeString(q.getSource().getSourceOwner());

        fileWriter.writeString(Labels.sourceEnd); // Source end
        fileWriter.writeString(Labels.end); // Universal end
    }

    public void close() throws IOException {
        fileWriter.close();
    }

    private void initFile() throws IOException {

        if (quoteFile.length() < 4) {
            // Write magic number
            fileWriter.writeInt(Quote.MAGIC_NUMBER);
        }
    }

    public File getFile() {
        return this.quoteFile;
    }

    public String getFilePath() {
        return this.filePath;
    }
}

package arc.haldun.quotes;

import arc.haldun.io.FileReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class QuoteReader {

    private final String filePath;
    private final File quoteFile;
    private final FileReader fileReader;

    public QuoteReader(String filePath) throws IOException {
        this.filePath = filePath;
        this.quoteFile = new File(filePath);

        if (!quoteFile.exists()) {
            throw new FileNotFoundException("Belirtilen yola erişilemiyor ya da öyle bir dosya yok.");
        }

        this.fileReader = new FileReader(filePath);

        checkFileValidity();
    }

    public Quote readQuote() throws IOException {

        Quote quote = null;

        String lbl;
        while (!(lbl = fileReader.readString()).equals(Labels.end)) {

            if (lbl.equals(Labels.quote)) {

                String content = "";
                String sourceType = "";
                Source source = null;

                String sourceName = "", sourceOwner = "";

                // Start to read quote

                while (!(lbl = fileReader.readString()).equals(Labels.sourceEnd)) {

                    if (lbl.equals(Labels.content)) {
                        content = fileReader.readString();
                    }

                    if (lbl.equals(Labels.sourceType)) {
                        sourceType = fileReader.readString();
                    }

                    if (lbl.equals(Labels.sourceName)) {
                        sourceName = fileReader.readString();
                    }

                    if (lbl.equals(Labels.sourceOwner)) {
                        sourceOwner = fileReader.readString();
                    }
                }

                // Create quote
                source = new Source(sourceName,sourceOwner);
                quote = new Quote(content, sourceType, source);
            }
        }

        return quote;
    }

    private void checkFileValidity() throws IOException {
        int magicNumber = fileReader.readInt();
        if (magicNumber != Quote.MAGIC_NUMBER) throw new RuntimeException("Belirtilen dosya geçerli bir quot dosyası değil.");
    }

    public void close() throws IOException {
        fileReader.close();
    }

    public String getFilePath() {
        return this.filePath;
    }

    public File getFile() {
        return this.quoteFile;
    }
}

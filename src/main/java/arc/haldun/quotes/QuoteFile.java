package arc.haldun.quotes;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class QuoteFile {

    private final String filePath;
    private final File file;
    private State lastState;

    private RandomAccessFile randomAccessFile_quotes;

    public QuoteFile(String filePath) throws IOException {
        this.filePath = filePath;
        this.file = new File(filePath);

        initRandomAccessFile();
        checkFileValidity();
    }

    public QuoteFile(File file) throws IOException {
        this.file = file;
        this.filePath = file.getAbsolutePath();

        initRandomAccessFile();
        checkFileValidity();
    }

    public Quote read() throws IOException {
        if (lastState == State.WRITE) randomAccessFile_quotes.seek(4);
        lastState = State.READ;

        Quote quote = null;

        String lbl;
        while (!(lbl = randomAccessFile_quotes.readUTF()).equals(Labels.end)) {

            if (lbl.equals(Labels.quote)) {

                String content = "";
                String sourceType = "";
                Source source;

                String sourceName = "", sourceOwner = "";

                // Start to read quote

                while (!(lbl = randomAccessFile_quotes.readUTF()).equals(Labels.sourceEnd)) {

                    if (lbl.equals(Labels.content)) {
                        content = randomAccessFile_quotes.readUTF();
                    }

                    if (lbl.equals(Labels.sourceType)) {
                        sourceType = randomAccessFile_quotes.readUTF();
                    }

                    if (lbl.equals(Labels.sourceName)) {
                        sourceName = randomAccessFile_quotes.readUTF();
                    }

                    if (lbl.equals(Labels.sourceOwner)) {
                        sourceOwner = randomAccessFile_quotes.readUTF();
                    }
                }

                // Create quote
                source = new Source(sourceName,sourceOwner);
                quote = new Quote(content, sourceType, source);
            }
        }

        return quote;
    }

    public void write(Quote q) throws IOException {
        if (lastState == State.READ) randomAccessFile_quotes.seek(4);
        lastState = State.WRITE;

        randomAccessFile_quotes.writeUTF(Labels.quote);

        // Write content
        randomAccessFile_quotes.writeUTF(Labels.content);
        randomAccessFile_quotes.writeUTF(q.getContent());

        // Write source type
        randomAccessFile_quotes.writeUTF(Labels.sourceType);
        randomAccessFile_quotes.writeUTF(q.getSourceType());

        // Write source name
        randomAccessFile_quotes.writeUTF(Labels.sourceName);
        randomAccessFile_quotes.writeUTF(q.getSource().getSourceName());

        // Write source owner
        randomAccessFile_quotes.writeUTF(Labels.sourceOwner);
        randomAccessFile_quotes.writeUTF(q.getSource().getSourceOwner());

        randomAccessFile_quotes.writeUTF(Labels.sourceEnd); // Source end
        randomAccessFile_quotes.writeUTF(Labels.end); // Universal end
    }

    public void close() throws IOException {
        randomAccessFile_quotes.close();
    }

    private void checkFileValidity() throws IOException {
        randomAccessFile_quotes.seek(0);
        int magicNumber = randomAccessFile_quotes.readInt();
        if (magicNumber != Quote.MAGIC_NUMBER) throw new RuntimeException("Belirtilen dosya geçerli bir quot dosyası değil.");
    }

    private void initRandomAccessFile() {

        boolean writeMagicNumber = false;

        if (!file.exists()) {
            writeMagicNumber = true;
            try {
                boolean res = file.createNewFile();
                if (!res) {
                    System.err.println("Dosya oluşturulamadı!");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        try {
            randomAccessFile_quotes = new RandomAccessFile(file, "rw");

            if (writeMagicNumber) {
                randomAccessFile_quotes.writeInt(Quote.MAGIC_NUMBER);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private enum State {
        READ,
        WRITE
    }
}

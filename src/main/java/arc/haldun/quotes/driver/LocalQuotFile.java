package arc.haldun.quotes.driver;

import arc.haldun.io.FileReader;
import arc.haldun.quotes.Labels;
import arc.haldun.quotes.Quote;
import arc.haldun.quotes.QuoteWriter;
import arc.haldun.quotes.Source;

import java.io.IOException;
import java.util.ArrayList;

public class LocalQuotFile implements IDatabase {

    private String filePath = "local.quot";

    public LocalQuotFile() {

    }

    public LocalQuotFile(String quotFilePath) {
        this.filePath = quotFilePath;
    }

    @Override
    public void add(Quote q) {
        try {

            QuoteWriter quoteWriter = new QuoteWriter(filePath);
            quoteWriter.writeQuote(q);
            quoteWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Quote get(int quoteId) {
        return null;
    }

    @Override
    public Quote[] select() {

        ArrayList<Quote> quotes = new ArrayList<>();

        try {

            FileReader fileReader = new FileReader(filePath);

            int magicNumber = fileReader.readInt();
            if (magicNumber != Quote.MAGIC_NUMBER) throw new RuntimeException("Bu bir quot dosyası değil! (<filename>)");

            String content = "";
            String sourceType = "";
            Source source = null;

            String lbl;

            while (fileReader.available() != 0) {
                lbl = fileReader.readString();

                if (lbl.equals(Labels.quote)) {
                    // Start to read quote

                    while (!(lbl = fileReader.readString()).equals(Labels.end)) {

                        if (lbl.equals(Labels.content)) {
                            content = fileReader.readString();
                        }

                        if (lbl.equals(Labels.sourceType)) {
                            sourceType = fileReader.readString();

                            source = readSource(fileReader);
                        }
                    }
                }

                if (lbl.equals(Labels.end)) {
                    //Create quote
                    Quote quote = new Quote(content, sourceType,source);
                    quotes.add(quote);
                }
            }



        /*
        while (true) {

            String lbl = fileReader.readString();

            if (lbl.equals(Labels.quote)) {
                //create quote object
                continue;
            }

            if (lbl.equals(Labels.content)) {
                content = fileReader.readString();
                continue;
            }

            if (lbl.equals(Labels.sourceType)) {
                String sourceType_ = fileReader.readString();

                if (sourceType_.equals(Quote.SourceType.BOOK.getValue())) {
                    book = readSource(fileReader);
                }
            }

            if (lbl.equals(Labels.end)) {
                System.out.println("Alıntı: " + content);
                System.out.println("Kaynak: kitap");
                System.out.println("Kitap adı: " + book.getBookName());
                System.out.println("yazar adı: " + book.getAuthor());
                System.out.println();

                if (fileReader.available() > 0) continue;
                else break;
            }
        }
         */

            fileReader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return quotes.toArray(new Quote[0]);
    }

    private Source readSource(FileReader fileReader) throws IOException {

        String sourceName = "";
        String sourceOwner = "";

        while (true) {

            String lbl = fileReader.readString();

            if (lbl.equals(Labels.sourceName)) {
                sourceName = fileReader.readString();
                continue;
            }

            if (lbl.equals(Labels.sourceOwner)) {
                sourceOwner = fileReader.readString();
                continue;
            }

            if (lbl.equals(Labels.sourceEnd)) {
                break;
            }
        }

        return new Source(sourceName, sourceOwner);
    }

    @Override
    public void delete(int id) {

    }
}

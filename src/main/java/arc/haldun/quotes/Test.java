package arc.haldun.quotes;

import arc.haldun.io.FileReader;
import arc.haldun.io.FileWriter;
import arc.haldun.quotes.driver.LocalQuotFile;

import java.io.IOException;
import java.util.ArrayList;

public class Test {

    static FileWriter fileWriter;
    static FileReader fileReader;

    static final int MAGIC_NUMBER = 0x746F7571;

    static ArrayList<Quote> quotes = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        //new Test().createQuoteFileWithOneQuote();
        //new Test().readQuoteFromFile();

        Quote q = new Quote(
                "Bağışlamak, menekşeleri onları ezen topuklarda hoş koku bırakmasıdır.",
                Quote.SourceType.PERSON.getValue(),
                new Source("Mark Twain", "")
        );

        Quote q2 = new Quote(
                "Ölümün nefesi hissedilince yaşaması gerekn ve yaşamak isteyen tamamen farklı iki kavram haline gelir.",
                Quote.SourceType.MOVIE.getValue(),
                new Source("Testere", "")
        );

        Quote q3 = new Quote(
                "Bir de kavgadan sonra barışmak, sevgiliden özür dilemek ya da onu affetmek ne doyulmaz zevktir.",
                Quote.SourceType.BOOK.getValue(),
                new Source("Yeraltından Notlar", "Dostoyevski")
        );

        QuotManager localQuotes = new QuotManager(new LocalQuotFile());
        // print qoutes

        Quote[] qs = localQuotes.select();

        for (Quote q_ : qs) {
            System.out.println("Quot:");
            System.out.println("\tQuote: " + q_.getContent());
            System.out.println("\tFrom: " + q_.getSource().getSourceName());
            System.out.println();
        }
    }

    public void readQuoteFromFile() throws IOException {

        FileReader fileReader = new FileReader("test.quot");

        int magicNumber = fileReader.readInt();
        if (magicNumber != MAGIC_NUMBER) throw new RuntimeException("Bu bir quot dosyası değil! (<filename>)");

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

    public void createQuoteFileWithOneQuote() throws IOException {

        FileWriter fileWriter = new FileWriter("test.quot");

        // Write magic number
        fileWriter.writeInt(MAGIC_NUMBER);

        //
        // Write quote
        //

        fileWriter.writeString(Labels.quote);

        // Write content
        fileWriter.writeString(Labels.content);
        fileWriter.writeString("Test Deneme");

        //
        // Write Source
        //

        // Write source type
        fileWriter.writeString(Labels.sourceType);
        fileWriter.writeString(Quote.SourceType.BOOK.getValue());

        // Write source (book)
        fileWriter.writeString(Labels.sourceName);
        fileWriter.writeString("Suç ve Ceza");
        fileWriter.writeString(Labels.sourceOwner);
        fileWriter.writeString("Dostoyevski");
        fileWriter.writeString(Labels.sourceEnd);

        fileWriter.writeString(Labels.end);

        //******
        //
        // Write second
        //
        fileWriter.writeString(Labels.quote);

        // Write content
        fileWriter.writeString(Labels.content);
        fileWriter.writeString("Ikinci Alinti");

        //
        // Write Source
        //

        // Write source type
        fileWriter.writeString(Labels.sourceType);
        fileWriter.writeString(Quote.SourceType.BOOK.getValue());

        // Write source (book)
        fileWriter.writeString(Labels.sourceName);
        fileWriter.writeString("Savas ve Baris");
        fileWriter.writeString(Labels.sourceOwner);
        fileWriter.writeString("Dostoyevski");
        fileWriter.writeString(Labels.sourceEnd);

        fileWriter.writeString(Labels.end);

        fileWriter.close();
    }
    
    static void readQuote() throws IOException {


        
        while (true) {
            
            String lbl = fileReader.readString();
            String content = "";

            if (lbl.equals("content")) {
                content = fileReader.readString();
            }

            if (lbl.equals("source")) {
                readSource();
            }

            if (lbl.equals(":endQ")) {
                // Create quote

                System.out.println("> " + content);
                break;
            }
        }


        
    }

    static void readSource() throws IOException {
        String lbl = fileReader.readString();
        String name = "", author = "", pageInfo = "";

        if (lbl.equals("type")) {

        }

        lbl = fileReader.readString();

        
        if (lbl.equals(Quote.SourceType.BOOK.name())) {
            
            switch (fileReader.readString()) {
                
                case "name":
                    name = fileReader.readString();
                    
                case "author":
                    author = fileReader.readString();
                    
                case "pageinfo":
                    pageInfo = fileReader.readString();
                    
                case ":endsrc":
                    break;
            }
        }


        System.out.println("Name: " + name);
        System.out.println("Author: " + author);
        System.out.println("Page: " + pageInfo);
    }
    
    static void create() throws IOException {
        fileWriter.writeInt(MAGIC_NUMBER);

        fileWriter.writeString("quot:");

        fileWriter.writeString("content");
        fileWriter.writeString("Bu bir deneme alıntısıdır.");

        fileWriter.writeString("source");
        fileWriter.writeString("type");
        fileWriter.writeString(Quote.SourceType.BOOK.name());

        fileWriter.writeString("name");
        fileWriter.writeString("Suç ve Ceza");
        fileWriter.writeString("author");
        fileWriter.writeString("Dostoyevski");
        fileWriter.writeString("pageinfo");
        fileWriter.writeString("65");
        fileWriter.writeString(":endsrc");
        
        fileWriter.writeString(":endQ");

        fileWriter.close();
    }
}

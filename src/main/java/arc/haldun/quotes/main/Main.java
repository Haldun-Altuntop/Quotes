package arc.haldun.quotes.main;

import arc.haldun.quotes.QuotManager;
import arc.haldun.quotes.Quote;
import arc.haldun.quotes.Source;
import arc.haldun.quotes.driver.LocalQuotFile;

import java.util.Scanner;

public class Main {

    static QuotManager quotManager = new QuotManager(new LocalQuotFile("Haldun.quot"));

    public static void main(String[] args) {

        if (args[0].equals("add")) {
            getQuotFromUserAndAdd();
        }
    }

    static void getQuotFromUserAndAdd() {

        Scanner scanner = new Scanner(System.in);
        String content, type, srcName = "", srcOwner;

        System.out.print("Alıntı Metni: ");
        content = scanner.nextLine();

        System.out.print("Tür: ");
        type = scanner.nextLine();

        if (!type.equals(Quote.SourceType.PERSON)) {
            System.out.print("Kaynak: ");
            srcName = scanner.nextLine();
        }

        System.out.println("Kaynak Sahibi: ");
        srcOwner = scanner.nextLine();

        Quote quote = new Quote(
                content,
                type,
                new Source(srcName, srcOwner)
        );

        quotManager.add(quote);
    }
}

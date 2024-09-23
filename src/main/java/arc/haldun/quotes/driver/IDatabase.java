package arc.haldun.quotes.driver;

import arc.haldun.quotes.Quote;

public interface IDatabase {

    void add(Quote q);
    Quote get(int quoteId);
    Quote[] select();
    void delete(int id);
}

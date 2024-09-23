package arc.haldun.quotes;

import arc.haldun.quotes.driver.IDatabase;

public class QuotManager {

    private final IDatabase database;

    public QuotManager(IDatabase database) {
        this.database = database;
    }

    public void add(Quote q) {
        database.add(q);
    }

    public Quote get(int id) {
        return database.get(id);
    }

    public Quote[] select() {
        return database.select();
    }

    public void delete(int id) {
        database.delete(id);
    }
}

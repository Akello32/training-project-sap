package com.sap.cap.bookstore.book.repository;

import com.sap.cds.ql.Select;
import com.sap.cds.ql.Update;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.ql.cqn.CqnUpdate;
import com.sap.cds.services.persistence.PersistenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import cds.gen.sap.capire.bookstore.Books;
import cds.gen.sap.capire.bookstore.Books_;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static cds.gen.sap.capire.products.Products_.CDS_NAME;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BookRepository {
    private final PersistenceService db;

    public Optional<Books> findBook(String id) {
        CqnSelect sel = Select.from(Books_.class).where(b -> b.ID().eq(id));

        return db.run(sel).first(Books.class);
    }

    public Optional<Books> findBookWithStock(String id) {
        CqnSelect sel = Select.from(Books_.class).columns(Books_::stock, Books_::ID).where(b -> b.ID().eq(id));

        return db.run(sel).first(Books.class);
    }

    public void updateBook(Books book) {
        CqnUpdate update = Update.entity(Books_.class).data(book).where(b -> b.ID().eq(book.getId()));
        db.run(update);
    }

    public void batchUpdateBooks(List<Map<String, Object>> params) {
        log.info("\n \n Start updating  \n \n");
        CqnUpdate upd = Update.entity(CDS_NAME).entries(params);
        db.run(upd, params);
    }
}

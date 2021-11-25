package com.sap.cap.bookstore.author.repository;

import cds.gen.adminservice.Authors;
import cds.gen.adminservice.Authors_;
import com.sap.cds.ql.Select;
import com.sap.cds.ql.StructuredType;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.services.persistence.PersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorRepository {
    private final PersistenceService db;

    public Optional<Authors> findAuthor(String id) {
        CqnSelect select = Select.from(Authors_.class)
                .columns(StructuredType::expand)
                .where(a -> a.ID().eq(id));

        return db.run(select).first(Authors.class);
    }
}

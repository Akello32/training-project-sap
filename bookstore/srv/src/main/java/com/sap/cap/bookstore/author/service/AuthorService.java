package com.sap.cap.bookstore.author.service;

import cds.gen.adminservice.Authors;
import com.sap.cap.bookstore.author.repository.AuthorRepository;
import com.sap.cds.ql.cqn.CqnAnalyzer;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.reflect.CdsModel;
import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorService {
    private final AuthorRepository repository;
    private final CqnAnalyzer analyzer;

    public AuthorService(AuthorRepository repository, CdsModel model) {
        this.repository = repository;
        this.analyzer = CqnAnalyzer.create(model);
    }

    @Transactional
    public Authors getAuthor(String id) {
        return repository.findAuthor(id)
                .orElseThrow(() -> new ServiceException(ErrorStatuses.NOT_FOUND, "Authors does not exist"));
    }

    public String getAuthorId(CqnSelect select) {
        return (String) analyzer.analyze(select).targetKeys().get(Authors.ID);
    }
}

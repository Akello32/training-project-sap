package com.sap.cap.bookstore.book.service;

import cds.gen.sap.capire.bookstore.Books;
import com.sap.cap.bookstore.book.repository.BookRepository;
import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cds.gen.sap.capire.bookstore.Books.AUTHOR_ID;
import static cds.gen.sap.capire.bookstore.Books.ID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final BookRepository repository;

    @Transactional
    public Books getBook(String id) {
        return repository.findBook(id)
                .orElseThrow(() -> new ServiceException(ErrorStatuses.NOT_FOUND, "Book does not exist"));
    }

    @Transactional
    public Books getBookWithStock(String id) {
        return repository.findBookWithStock(id)
                .orElseThrow(() -> new ServiceException(ErrorStatuses.NOT_FOUND, "Book does not exist"));
    }

    @Transactional
    public void updateBook(Books books) {
        repository.updateBook(books);
    }

    @Transactional
    public void batchUpdateBooks(List<Books> books) {
        List<Map<String, Object>> params = new ArrayList<>();
        books.forEach(b -> {
            Map<String, Object> mapParam = new HashMap<>();
            mapParam.put(ID, b.getId());
            mapParam.put(AUTHOR_ID, b.getAuthorId());
            params.add(mapParam);
        });

        log.info("\n \n Created params map\n \n");
        repository.batchUpdateBooks(params);
    }
}

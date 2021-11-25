package com.sap.cap.bookstore.author.handler;

import cds.gen.adminservice.AdminService_;
import cds.gen.adminservice.Authors;
import cds.gen.adminservice.Authors_;
import cds.gen.adminservice.SwapBooksContext;
import cds.gen.sap.capire.bookstore.Books;
import com.sap.cap.bookstore.author.service.AuthorService;
import com.sap.cap.bookstore.book.service.BookService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ServiceName(AdminService_.CDS_NAME)
@RequiredArgsConstructor
@Slf4j
public class AuthorHandler implements EventHandler {
    private final AuthorService service;
    private final BookService bookService;

    @On(event = SwapBooksContext.CDS_NAME, entity = Authors_.CDS_NAME)
    public void swapBooks(SwapBooksContext context) {
        log.info("\n \n Trigger the action \n \n");

        String firstAuthorId = service.getAuthorId(context.getCqn());
        String secondAuthorId = context.getAuthorId();

        Authors firstAuthors = service.getAuthor(firstAuthorId);
        Authors secondAuthors = service.getAuthor(secondAuthorId);

        List<Books> firstBooks = firstAuthors.getBooks();
        firstBooks.forEach(b -> b.setAuthorId(secondAuthorId));

        List<Books> secondBooks = secondAuthors.getBooks();
        secondBooks.forEach(b -> b.setAuthorId(firstAuthorId));

        log.info("\n \n Start updating authors \n \n");

        bookService.batchUpdateBooks(firstBooks);
        bookService.batchUpdateBooks(secondBooks);

        context.setResult("The authors exchanged books");
    }
}

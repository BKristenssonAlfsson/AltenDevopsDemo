package se.alten.demo.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book addBook(Book book) {
        System.out.println(book.toString());
        return bookRepository.save(book);
    }

    public Book findByTitle(String title){
        return bookRepository.findByTitle(title);
    }
}
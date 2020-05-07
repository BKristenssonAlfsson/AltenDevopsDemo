package se.alten.demo.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService = new BookService();

    @GetMapping()
    public ResponseEntity<List<Book>> getAll() {
        try {
            List<Book> books = bookService.findAll();

            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping(value = "/title")
    public ResponseEntity<Book> findByTitle(@RequestParam String title) {
        try {
            Book book = bookService.findByTitle(title);
            if (book == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(book, HttpStatus.OK);
            }
        } catch ( Exception e ) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        try {
            System.out.println(book.toString());
            Book bookAdded = bookService.addBook(book);

            return new ResponseEntity<>(bookAdded, HttpStatus.OK);
        } catch ( Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
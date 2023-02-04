package com.example.obrestdatajpa.controllers;

import com.example.obrestdatajpa.entities.Book;
import com.example.obrestdatajpa.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    //allows you to display messages with error levels, and other data.
    private final Logger log = LoggerFactory.getLogger(BookController.class);
    private BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/api/books")
    public List<Book> findAll(){
        return bookRepository.findAll();
    }

    @GetMapping("/api/books/{id}")
    public ResponseEntity<Book> findOneById(@PathVariable Long id){

        Optional<Book> bookOpt = bookRepository.findById(id);

        if(bookOpt.isPresent()){
            return ResponseEntity.ok(bookOpt.get());
        }
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping("/api/books")
    public ResponseEntity<Book> create(@RequestBody Book book, @RequestHeader HttpHeaders headers){
        System.out.println(headers.get("User-Agent"));

        if(book.getId() != null){
            log.warn("trying to create a book with id");
            return ResponseEntity.badRequest().build();
        }

        Book result = bookRepository.save(book);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/api/books")
    public ResponseEntity<Object> update(@RequestBody Book book){
        if(book.getId() == null){
            log.warn("Trying to update a non existent book");
            return ResponseEntity.badRequest().build();
        }

        if(!bookRepository.existsById(book.getId())){
            log.warn("Trying to update a non existent book");
            return ResponseEntity.notFound().build();
        }

        Book result = bookRepository.save(book);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable Long id){

        if(!bookRepository.existsById(id)){
            log.warn("Trying to delete a non existent book");
            return ResponseEntity.notFound().build();
        }

        bookRepository.deleteById(id);

        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/api/books")
    public ResponseEntity<Book> deleteAll(){
        log.info("REST Request for delete all books");
        bookRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }



}

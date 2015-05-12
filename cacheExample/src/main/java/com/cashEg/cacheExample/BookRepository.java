package com.cashEg.cacheExample;

public interface BookRepository {

    Book getByIsbn(String isbn);

}
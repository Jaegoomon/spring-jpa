package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.item.Movie;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemService itemService;

    @Test
    public void 아이템_등록() {
        // given
        Item album = new Album();
        album.setName("Test Album");

        Item book = new Book();
        book.setName("Test Book");

        Item movie = new Movie();
        movie.setName("Test Movie");
        // when
        Long savedAlbumId = itemService.saveItem(album);
        Long savedBookId = itemService.saveItem(book);
        Long savedMovieId = itemService.saveItem(movie);
        // then
        assertThat(itemRepository.findOne(savedAlbumId)).isEqualTo(album);
        assertThat(itemRepository.findOne(savedAlbumId)).isInstanceOf(Album.class);

        assertThat(itemRepository.findOne(savedBookId)).isEqualTo(book);
        assertThat(itemRepository.findOne(savedBookId)).isInstanceOf(Book.class);

        assertThat(itemRepository.findOne(savedMovieId)).isEqualTo(movie);
        assertThat(itemRepository.findOne(savedMovieId)).isInstanceOf(Movie.class);
    }

    @Test
    public void 아이템_수정() {
        // given
        Book book = new Book();
        book.setName("총균쇠");
        book.setPrice(12000);
        book.setStockQuantity(20);
        book.setAuthor("재레드 다이아몬드");
        book.setIsbn("A1020");

        Long bookId = itemService.saveItem(book);

        // when
        int updatedPrice = 13000;
        int updateStockQuantity = 25;
        itemService.updateItem(bookId, 13000, 25);
        Book updatedBook = (Book) itemRepository.findOne(bookId);

        // then
        assertThat(book.getName()).isEqualTo(updatedBook.getName());
        assertThat(book.getAuthor()).isEqualTo(updatedBook.getAuthor());
        assertThat(book.getIsbn()).isEqualTo(updatedBook.getIsbn());

        assertThat(updatedBook.getPrice()).isEqualTo(updatedPrice);
        assertThat(updatedBook.getStockQuantity()).isEqualTo(updateStockQuantity);
    }
}
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
}
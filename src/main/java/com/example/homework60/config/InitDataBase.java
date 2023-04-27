package com.example.homework60.config;

import com.example.homework60.dao.*;
import com.example.homework60.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class InitDataBase {
    @Bean
    CommandLineRunner init(UserDao userDao, PublicationDao publicationDao, PublicationImageDao publicationImageDao,
                           CommentDao commentDao, LikeDao likeDao, BookmarkDao bookmarkDao){
        return (args) -> {
            userDao.createTable();
            publicationDao.createTable();
            publicationImageDao.createTable();
            commentDao.createTable();
            likeDao.createTable();
            bookmarkDao.createTable();
            Optional<User> optionalUser = userDao.findUserByEmail("johny@gmail.com");
            if(optionalUser.isEmpty()) {
                User user = User.builder()
                        .id(1L)
                        .name("John")
                        .email("johny@gmail.com")
                        .password(SecurityConfiguration.passwordEncoder().encode("john123"))
                        .publications(0)
                        .subscriptions(0)
                        .followers(0)
                        .enabled(true)
                        .roles("ROLE_USER")
                        .build();
                userDao.insertUser(user);
                User secondUser = User.builder()
                        .id(2L)
                        .name("Bob")
                        .email("bob@gmail.com")
                        .password(SecurityConfiguration.passwordEncoder().encode("bob123"))
                        .publications(0)
                        .subscriptions(0)
                        .followers(0)
                        .enabled(true)
                        .roles("ROLE_USER")
                        .build();
                userDao.insertUser(secondUser);
                PublicationImage publicationImage = PublicationImage.builder()
                        .id(1L)
                        .link("src/main/resources/templates/start.jpg")
                        .build();
                publicationImageDao.save(publicationImage);
                PublicationImage secondPublicationImage = PublicationImage.builder()
                        .id(2L)
                        .link("src/main/resources/templates/rain.jpg")
                        .build();
                publicationImageDao.save(secondPublicationImage);
                PublicationImage thirdPublicationImage = PublicationImage.builder()
                        .id(3L)
                        .link("src/main/resources/templates/river.jpg")
                        .build();
                publicationImageDao.save(thirdPublicationImage);
                Publication publication = Publication.builder()
                        .id(1L)
                        .userID(user.getId())
                        .imageID(publicationImage.getId())
                        .description("It is test publication")
                        .time(LocalDateTime.now())
                        .build();
                publicationDao.insertPublication(publication);
                Publication secondPublication = Publication.builder()
                        .id(2L)
                        .userID(user.getId())
                        .imageID(secondPublicationImage.getId())
                        .description("It is second test publication")
                        .time(LocalDateTime.now())
                        .build();
                publicationDao.insertPublication(secondPublication);
                Publication thirdPublication = Publication.builder()
                        .id(3L)
                        .userID(secondUser.getId())
                        .imageID(thirdPublicationImage.getId())
                        .description("It is third test publication")
                        .time(LocalDateTime.now())
                        .build();
                publicationDao.insertPublication(thirdPublication);
                Comment comment = Comment.builder()
                        .id(1L)
                        .userID(user.getId())
                        .publicationID(publication.getId())
                        .text("It is test comment")
                        .time(LocalDateTime.now())
                        .build();
                commentDao.insertComment(comment.getPublicationID(), comment.getText(), comment.getUserID());
                ILike iLike = ILike.builder()
                        .id(1L)
                        .userID(user.getId())
                        .likeObjectID(publication.getId())
                        .time(LocalDateTime.now())
                        .build();
                likeDao.insertPublicationLike(iLike);
                ILike secondILike = ILike.builder()
                        .id(2L)
                        .userID(secondUser.getId())
                        .likeObjectID(thirdPublication.getId())
                        .time(LocalDateTime.now())
                        .build();
                likeDao.insertPublicationLike(secondILike);
                Bookmark bookmark = Bookmark.builder()
                        .id(1L)
                        .userID(user.getId())
                        .publicationID(publication.getId())
                        .build();
                bookmarkDao.insertBookmark(bookmark.getUserID(), bookmark.getPublicationID());
                Bookmark secondBookmark = Bookmark.builder()
                        .id(2L)
                        .userID(user.getId())
                        .publicationID(secondPublication.getId())
                        .build();
                bookmarkDao.insertBookmark(secondBookmark.getUserID(), secondBookmark.getPublicationID());
            }
        };
    }
}

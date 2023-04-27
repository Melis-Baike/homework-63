package com.example.homework60.service;

import com.example.homework60.dao.BookmarkDao;
import com.example.homework60.dao.UserDao;
import com.example.homework60.dto.BookmarkDTO;
import com.example.homework60.entity.Bookmark;
import com.example.homework60.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkDao bookmarkDao;
    private final UserDao userDao;

    public String setBookmark(Authentication authentication, Long publicationID){
        User user = userDao.findUserByEmail(authentication.getName()).get();
        Optional<Bookmark> optionalBookmark = bookmarkDao.checkBookmark(user.getId(), publicationID);
        if(optionalBookmark.isPresent()){
            return "You have already added this publication in bookmarks";
        } else {
            bookmarkDao.insertBookmark(user.getId(), publicationID);
            return "You have successfully added this publication in bookmarks";
        }
    }

    public String removeBookmark(Authentication authentication, Long publicationID){
        User user = userDao.findUserByEmail(authentication.getName()).get();
        Optional<Bookmark> optionalBookmark = bookmarkDao.checkBookmark(user.getId(), publicationID);
        if(optionalBookmark.isPresent()){
            bookmarkDao.removeBookmark(user.getId(), publicationID);
            return "You have successfully removed bookmark";
        } else {
            return "Something went wrong";
        }
    }

    public Optional<BookmarkDTO> getBookmarkInfo(Authentication authentication, Long publicationID){
        User user = userDao.findUserByEmail(authentication.getName()).get();
        return bookmarkDao.checkBookmark(user.getId(), publicationID).map(BookmarkDTO::from);
    }
}

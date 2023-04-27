package com.example.homework60.service;

import com.example.homework60.dao.CommentDao;
import com.example.homework60.dao.UserDao;
import com.example.homework60.dto.CommentDTO;
import com.example.homework60.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentDao commentDao;
    private final UserDao userDao;
    public String comment(String msg, Long publicationID, Authentication authentication) {
        User user = userDao.findUserByEmail(authentication.getName()).get();
        return commentDao.insertComment(publicationID, msg, user.getId());
    }

    public Optional<List<CommentDTO>> getComments(Long publicationID){
        return Optional.of(commentDao.getComments(publicationID).get().stream().map(CommentDTO::from)
                .collect(Collectors.toList()));
    }
}

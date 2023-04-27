package com.example.homework60.service;

import com.example.homework60.dao.LikeDao;
import com.example.homework60.dao.UserDao;
import com.example.homework60.dto.ILikeDTO;
import com.example.homework60.entity.ILike;
import com.example.homework60.entity.Publication;
import com.example.homework60.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeDao likeDao;
    private final UserDao userDao;

    public String setPublicationLike(Long publicationID, Authentication authentication){
        ILike iLike = setLike(publicationID, authentication);
        Optional<ILike<Publication>> optionalILike = likeDao.checkPublicationLike(iLike.getUserID(), publicationID);
        if(optionalILike.isPresent()) {
            return "You have already liked this publication";
        } else {
            likeDao.insertPublicationLike(iLike);
            return "You have successfully liked publication";
        }
    }

    public String setCommentLike(Long commentID, Authentication authentication){
        ILike iLike = setLike(commentID, authentication);
        likeDao.insertCommentLike(iLike);
        return "All it's OK";
    }

    public ILike setLike(Long likedObjectID, Authentication authentication){
        User user = userDao.findUserByEmail(authentication.getName()).get();
        return ILike.builder()
                .userID(user.getId())
                .likeObjectID(likedObjectID)
                .time(LocalDateTime.now())
                .build();
    }

    public String removeLike(Authentication authentication, Long publicationID){
        User user = userDao.findUserByEmail(authentication.getName()).get();
        Optional<ILike<Publication>> optionalILike = likeDao.checkPublicationLike(user.getId(), publicationID);
        if(optionalILike.isPresent()){
            likeDao.removePublicationLike(user.getId(), publicationID);
            return "You have successfully removed like";
        } else {
            return "Something went wrong";
        }
    }

    public Optional<ILikeDTO> getLikeInfo(Long publicationID, Authentication authentication){
        User user = userDao.findUserByEmail(authentication.getName()).get();
        return likeDao.checkPublicationLike(user.getId(), publicationID).map(ILikeDTO::from);
    }
}

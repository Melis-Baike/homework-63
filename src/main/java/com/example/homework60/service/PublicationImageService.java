package com.example.homework60.service;

import com.example.homework60.dao.PublicationImageDao;
import com.example.homework60.entity.PublicationImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PublicationImageService {
    private final PublicationImageDao publicationImageDao;

    public String getImage(Long id){
        Optional<PublicationImage> publicationImage = publicationImageDao.findById(id);
        if(publicationImage.isPresent()) {
            return publicationImage.get().getLink().split("/")[4];
        } else {
            return "Something went wrong";
        }
    }
}

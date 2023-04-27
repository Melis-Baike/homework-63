package com.example.homework60.service;

import com.example.homework60.dao.PublicationDao;
import com.example.homework60.dao.PublicationImageDao;
import com.example.homework60.dao.UserDao;
import com.example.homework60.dto.PublicationDTO;
import com.example.homework60.entity.Publication;
import com.example.homework60.entity.PublicationImage;
import com.example.homework60.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicationService {
    private final PublicationDao publicationDao;
    private final PublicationImageDao publicationImageDao;
    private final FilesStorageServiceImpl filesStorageService;
    private final UserDao userDao;
    public String postPublication(MultipartFile file, String description, Authentication authentication){
        User user = userDao.findUserByEmail(authentication.getName()).get();
        String path = filesStorageService.save(file);
        byte[] data = new byte[0];
        try {
            data = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (data.length != 0) {
            PublicationImage publicationImage = PublicationImage.builder()
                    .id((long) (publicationImageDao.getAll().get().size() + 1))
                    .link(path)
                    .build();
            publicationImageDao.save(publicationImage);
            Publication publication = Publication.builder()
                    .id((long) (publicationDao.selectAllPublications().size() + 1))
                    .userID(user.getId())
                    .imageID(publicationImage.getId())
                    .description(description)
                    .time(LocalDateTime.now())
                    .build();
            return publicationDao.insertPublication(publication);
        } else {
            return "You have sent broken file";
        }
    }

    public Optional<PublicationDTO> getPublication(Long id){
        Optional<Publication> optionalPublication = publicationDao.getPublication(id);
        return optionalPublication.map(PublicationDTO::from);
    }

    public Optional<List<PublicationDTO>> getAllPublications(){
        return Optional.of(publicationDao.selectAllPublications().stream().map(PublicationDTO::from)
                .collect(Collectors.toList()));
    }
}

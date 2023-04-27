package com.example.homework60.controller;

import com.example.homework60.dto.BookmarkDTO;
import com.example.homework60.dto.CommentDTO;
import com.example.homework60.dto.ILikeDTO;
import com.example.homework60.dto.PublicationDTO;
import com.example.homework60.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/publications")
public class PublicationController {
    private final PublicationService service;
    private final CommentService commentService;
    private final PublicationImageService publicationImageService;
    private final LikeService likeService;
    private final BookmarkService bookmarkService;

    @GetMapping
    public Optional<List<PublicationDTO>> getAllPublications(){
        return service.getAllPublications();
    }

    @GetMapping("/{id}")
    public Optional<PublicationDTO> getPublication(@PathVariable Long id){
        return service.getPublication(id);
    }
    @PostMapping(path ="/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postPublication(@RequestParam("file") MultipartFile file, @RequestParam(
            name = "description"
    ) String description, Authentication authentication){
        return new ResponseEntity<>(service.postPublication(file, description, authentication), HttpStatus.OK);
    }

    @PostMapping("/{publicationID}/comment")
    public ResponseEntity<String> commentPublication(@PathVariable Long publicationID, @RequestParam("comment")
    String comment, Authentication authentication){
        return new ResponseEntity<>(commentService.comment(comment, publicationID, authentication), HttpStatus.OK);
    }

    @GetMapping("/image/{id}")
    public String getImage(@PathVariable Long id){
        return publicationImageService.getImage(id);
    }

    @GetMapping("/{publicationID}/comment")
    public Optional<List<CommentDTO>> getComments(@PathVariable Long publicationID){
        return commentService.getComments(publicationID);
    }

    @PostMapping("/{publicationID}/like")
    public ResponseEntity<String> setPublicationLike(@PathVariable Long publicationID, Authentication authentication){
        return new ResponseEntity<>(likeService.setPublicationLike(publicationID, authentication), HttpStatus.OK);
    }

    @PostMapping("/comments/{commentID}/like")
    public ResponseEntity<String> setCommentLike(@PathVariable Long commentID, Authentication authentication){
        return new ResponseEntity<>(likeService.setCommentLike(commentID, authentication), HttpStatus.OK);
    }

    @DeleteMapping("/{publicationID}/like")
    public ResponseEntity<String> removePublicationLike(@PathVariable Long publicationID, Authentication authentication){
        return new ResponseEntity<>(likeService.removeLike(authentication, publicationID), HttpStatus.OK);
    }

    @PostMapping("/{publicationID}/bookmark")
    public ResponseEntity<String> setBookmark(@PathVariable Long publicationID, Authentication authentication){
        return new ResponseEntity<>(bookmarkService.setBookmark(authentication, publicationID), HttpStatus.OK);
    }

    @DeleteMapping("/{publicationID}/bookmark")
    public ResponseEntity<String> removeBookmark(@PathVariable Long publicationID, Authentication authentication){
        return new ResponseEntity<>(bookmarkService.removeBookmark(authentication, publicationID), HttpStatus.OK);
    }

    @GetMapping("/{publicationID}/like")
    public Optional<ILikeDTO> getLikeInfo(@PathVariable Long publicationID, Authentication authentication){
        return likeService.getLikeInfo(publicationID, authentication);
    }

    @GetMapping("/{publicationID}/bookmark")
    public Optional<BookmarkDTO> getBookmarkInfo(@PathVariable Long publicationID, Authentication authentication){
        return bookmarkService.getBookmarkInfo(authentication, publicationID);
    }
}

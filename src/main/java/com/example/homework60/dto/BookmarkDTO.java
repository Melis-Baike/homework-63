package com.example.homework60.dto;

import com.example.homework60.entity.Bookmark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkDTO {
    public static BookmarkDTO from(Bookmark bookmark){
        return BookmarkDTO.builder()
                .id(bookmark.getId())
                .userID(bookmark.getUserID())
                .publicationID(bookmark.getPublicationID())
                .build();
    }

    private Long id;
    private Long userID;
    private Long publicationID;
}

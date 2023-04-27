package com.example.homework60.dto;

import com.example.homework60.entity.Publication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublicationDTO {
    public static PublicationDTO from(Publication publication){
        return PublicationDTO.builder()
                .id(publication.getId())
                .userID(publication.getUserID())
                .imageID(publication.getImageID())
                .description(publication.getDescription())
                .time(publication.getTime())
                .build();
    }
    private Long id;
    private Long userID;
    private Long imageID;
    private String description;
    private LocalDateTime time;
}

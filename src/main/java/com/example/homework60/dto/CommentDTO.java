package com.example.homework60.dto;

import com.example.homework60.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    public static CommentDTO from(Comment comment){
        return CommentDTO.builder()
                .id(comment.getId())
                .userID(comment.getUserID())
                .publicationID(comment.getPublicationID())
                .text(comment.getText())
                .time(comment.getTime())
                .build();
    }
    private Long id;
    private Long userID;
    private Long publicationID;
    private String text;
    private LocalDateTime time;
}

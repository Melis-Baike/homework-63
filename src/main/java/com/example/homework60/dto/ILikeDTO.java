package com.example.homework60.dto;

import com.example.homework60.entity.ILike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ILikeDTO {
    public static ILikeDTO from(ILike iLike){
        return ILikeDTO.builder()
                .id(iLike.getId())
                .userID(iLike.getUserID())
                .likeObjectID(iLike.getLikeObjectID())
                .time(iLike.getTime())
                .build();
    }

    private Long id;
    private Long userID;
    private Long likeObjectID;
    private LocalDateTime time;
}

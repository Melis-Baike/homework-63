package com.example.homework60.entity;

import com.example.homework60.able.Likeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ILike<V extends Likeable> {
    private Long id;
    private Long userID;
    private Long likeObjectID;
    private LocalDateTime time;
}
package com.example.homework60.entity;

import com.example.homework60.able.Likeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Publication implements Likeable {
    private Long id;
    private Long userID;
    private Long imageID;
    private String description;
    private LocalDateTime time;
}

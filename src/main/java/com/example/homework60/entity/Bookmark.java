package com.example.homework60.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bookmark {
    private Long id;
    private Long userID;
    private Long publicationID;
}

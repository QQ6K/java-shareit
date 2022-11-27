package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class CommentDto {
    private int id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}

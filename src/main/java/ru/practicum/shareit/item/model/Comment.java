package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
    @Entity
    @Table(name = "Comments")
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public class Comment {
        @Id
        @Column(name = "id", nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String text;

        @OneToOne
        @JoinColumn(name = "item_id")
        private Item item;

        @OneToOne
        @JoinColumn(name = "author_id")
        private User author;

        private LocalDateTime created;
    }

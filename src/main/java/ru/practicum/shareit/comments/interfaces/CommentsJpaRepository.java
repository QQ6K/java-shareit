package ru.practicum.shareit.comments.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.comments.model.Comment;

public interface CommentsJpaRepository extends JpaRepository<Comment,Long> {
}

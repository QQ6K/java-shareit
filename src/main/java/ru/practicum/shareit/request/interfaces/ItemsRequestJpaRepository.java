package ru.practicum.shareit.request.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

public interface ItemsRequestJpaRepository extends JpaRepository<ItemRequest, Long> {
}

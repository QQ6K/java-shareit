package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemsRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByRequestId(Long requestId);

    List<Item> findAllByOwnerIdOrderByIdAsc(Long userId);

}

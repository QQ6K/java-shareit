package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemsRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByRequestId(Long requestId);

}

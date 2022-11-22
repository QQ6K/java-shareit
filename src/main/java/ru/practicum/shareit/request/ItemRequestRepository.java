package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByRequesterId(Long requester);

    //  Page<ItemRequest> findAllByRequesterIdIsNot(Long requesterId);

    //List<ItemRequest> findAllByRequesterIdOrderByCreatedAsc
    List<ItemRequest> findAllByRequesterIdOrderByCreatedAsc(Long user);

    Page<ItemRequest> findAllByRequesterIdIsNot(Long requesterId, Pageable pageable);
}
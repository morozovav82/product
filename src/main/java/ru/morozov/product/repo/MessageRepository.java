package ru.morozov.product.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.morozov.product.entity.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySentIsNullOrderById();
}

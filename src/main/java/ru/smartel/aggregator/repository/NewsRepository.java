package ru.smartel.aggregator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smartel.aggregator.entity.News;

import java.util.List;
import java.util.UUID;

@Repository
public interface NewsRepository extends JpaRepository<News, UUID> {
    List<News> findAllByTitleContaining(String query);
    boolean existsByTitle(String title);
}

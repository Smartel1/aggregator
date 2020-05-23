package ru.smartel.aggregator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smartel.aggregator.entity.Source;

import java.util.UUID;

@Repository
public interface SourceRepository extends JpaRepository<Source, UUID> {
}

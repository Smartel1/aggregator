package ru.smartel.aggregator.entity;

import java.util.Objects;
import java.util.UUID;

/**
 * Сравнение JPA-сущностей производится по id
 */
public abstract class BaseEntity {

    public abstract UUID getId();

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        final BaseEntity that = (BaseEntity) other;
        if (that.getId() == null) {
            return false;
        }
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

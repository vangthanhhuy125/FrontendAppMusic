package com.example.manhinhappmusic.model;
import java.time.Instant;

import lombok.Data;
@Data
public abstract class BaseDocument {
    private Instant createdAt;
    private Instant updatedAt;
    // Getter
    public Instant getCreatedAt() {
        return createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
}

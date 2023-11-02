package com.andy.shared;

import java.util.UUID;

public record BookDTO(
        UUID id,
        String title,
        String author
) {
}

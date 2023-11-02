package com.andy.shared;

public record BookCreateUpdateRequest(
        String title,
        String author
) {
}

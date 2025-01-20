package com.kosher.iskosher.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageResponse<T> {
    // A list containing the actual content or data of the current page
    private List<T> content;

    // The current page number, starting from 0 (zero-based index)
    private int pageNumber;

    // The number of items (elements) per page
    private int pageSize;

    // The total number of elements available across all pages
    private long totalElements;

    // The total number of pages based on the total number of elements and page size
    private int totalPages;

    // Constructor that initializes the fields using a Spring Data Page object

    public PageResponse() {

    }

    public PageResponse(Page<T> page) {
        // The actual content of the page (a list of items)
        this.content = page.getContent();

        // The current page number (zero-based index)
        this.pageNumber = page.getNumber();

        // The number of items per page (page size)
        this.pageSize = page.getSize();

        // The total number of elements across all pages
        this.totalElements = page.getTotalElements();

        // The total number of pages available, based on the total number of elements and page size
        this.totalPages = page.getTotalPages();
    }
}

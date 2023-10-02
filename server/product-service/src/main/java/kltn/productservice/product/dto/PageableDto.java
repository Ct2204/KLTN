
package kltn.productservice.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageableDto {

    private int totalPages;

    private long totalElements;

    private int perPage;

    private int numberOfElements;

    private int currentPage;

    private boolean first;

    private boolean last;

    public PageableDto(int totalPages, long totalElements, int perPage,
                       int numberOfElements, int currentPage,
                       boolean first, boolean last) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.perPage = perPage;
        this.numberOfElements = numberOfElements;
        this.currentPage = currentPage;
        this.first = first;
        this.last = last;
    }

}

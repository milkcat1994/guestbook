package org.zerock.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {

    // 화면에서 전달되는 page, size 파라미터
    private int page;
    private int size;

    private String type;
    private String keyword;

    // page, size의 기본값은 1과 10으로 지정
    public PageRequestDTO(){
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable(Sort sort){
        // page가 음수로 들어올 수 도 있음.
        page = Math.max(page, 1);

        return PageRequest.of(page -1, size, sort);
    }
}

package org.zerock.guestbook.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
// 다양한 곳에 사용할 수 있도록 제너릭 타입 사용
public class PageResultDTO<DTO, EN> {

    // 결과 리스트
    private List<DTO> dtoList;

    // 총 페이지 번호
    private int totalPage;

    // 현재 페이지 번호
    private int page;
    // 목록 사이즈
    private int size;

    // 시작 페이지, 끝 페이지 번호
    private int start, end;

    // 이전, 다음
    private boolean prev, next;

    // 페이지 번호 목록
    private List<Integer> pageList;

    // Function은 Entity 객체들을 DTO로 변환해주는 function을 사용하면 된다.
    // ex) GuestbookService의 entityToDto()
    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn){
        dtoList = result.stream()
                .map(fn)
                .collect(Collectors.toList());

        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable){
        this.page = pageable.getPageNumber() + 1;
        this.size = pageable.getPageSize();

        // temp end page
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;

        start = tempEnd - 9;
        prev = start > 1;
        end = Math.min(totalPage, tempEnd);
        // 다음 페이징으로 넘어갈 수 있다면 next = true
        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end)
                .boxed()
                .collect(Collectors.toList());
    }
}

package org.zerock.guestbook.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
// 다양한 곳에 사용할 수 있도록 제너릭 타입 사용
public class PageResultDTO<DTO, EN> {

    private List<DTO> dtoList;

    // Function은 Entity 객체들을 DTO로 변환해주는 function을 사용하면 된다.
    // ex) GuestbookService의 entityToDto()
    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn){
        dtoList = result.stream()
                .map(fn)
                .collect(Collectors.toList());
    }
}

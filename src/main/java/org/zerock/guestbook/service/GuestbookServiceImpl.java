package org.zerock.guestbook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.repository.GuestbookRepository;

import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor // 의존성 자동 주입
public class GuestbookServiceImpl implements GuestbookService{

    // 반드시 final로 선언
    // @RequiredArgsConstructor로 인해 초기화 되지않은 final 필드, @NonNull이 붙은 필드를 매개변수로 갖는 생성자를 자동으로 생성한다.
    private final GuestbookRepository repository;

    @Override
    public Long register(GuestbookDTO dto) {
        log.info("DTO-----------------------------");
        log.info(dto);

        // Guestbook의 DTO를 JPA에서 처리하기 위해 Entity로 변경
        Guestbook entity = dtoToEntity(dto);
        log.info(entity);

        repository.save(entity);
        return entity.getGno();
    }

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());
        Page<Guestbook> result = repository.findAll(pageable);
        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }
}

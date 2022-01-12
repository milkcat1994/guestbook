package org.zerock.guestbook.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;
import org.zerock.guestbook.mapper.GuestbookMapper;
import org.zerock.guestbook.repository.GuestbookRepository;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor // 의존성 자동 주입
public class GuestbookServiceImpl implements GuestbookService{

    // 반드시 final로 선언
    // @RequiredArgsConstructor로 인해 초기화 되지않은 final 필드, @NonNull이 붙은 필드를 매개변수로 갖는 생성자를 자동으로 생성한다.
    private final GuestbookRepository repository;

    private final GuestbookMapper mapper = Mappers.getMapper(GuestbookMapper.class);

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

        // 검색 조건 처리
        BooleanBuilder booleanBuilder = getSearch(requestDTO);

        // Querydsl 사용
        Page<Guestbook> result = repository.findAll(booleanBuilder, pageable);
        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public Guestbook dtoToEntity(GuestbookDTO dto) {
        return mapper.dtoToEntity(dto);
    }

    @Override
    public GuestbookDTO entityToDto(Guestbook entity) {
        return mapper.entityToDto(entity);
    }

    @Override
    public GuestbookDTO read(Long gno) {
        Optional<Guestbook> result = repository.findById(gno);
//        return result.map(this::entityToDto).orElse(null);
        return result.map(entity -> entityToDto(entity)).orElse(null);
    }

    @Override
    public void modify(GuestbookDTO dto) {
        // 업데이트 하는 항목은 '제목', '내용'
        Optional<Guestbook> result = repository.findById(dto.getGno());

        if(result.isPresent()){
            Guestbook entity = result.get();
            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());
            repository.save(entity);
        }
    }

    @Override
    public void remove(Long gno) {
        repository.deleteById(gno);
    }

    // Querydsl 처리
    private BooleanBuilder getSearch(PageRequestDTO requestDTO){
        // 검색조건, 검색어 가 있는 경우 conditionBuilder 변수를 이용해서 조건 추가하기
        String type = requestDTO.getType();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = requestDTO.getKeyword();
        // gno > 0 조건
        BooleanExpression expression = qGuestbook.gno.gt(0L);
        booleanBuilder.and(expression);

        // 검색 조건이 없는 경우
        if(type == null || type.trim().length() == 0){
            return booleanBuilder;
        }

        // 검색 조건 작성하기
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("t")){
            conditionBuilder.or(qGuestbook.title.contains(keyword));
        }
        if(type.contains("c")){
            conditionBuilder.or(qGuestbook.content.contains(keyword));
        }
        if(type.contains("w")){
            conditionBuilder.or(qGuestbook.writer.contains(keyword));
        }

        // 모든 조건 통합
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }
}

package org.zerock.guestbook.mapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.entity.Guestbook;

import java.time.LocalDateTime;

@SpringBootTest
@RequiredArgsConstructor
public class GuestbookMapperTests {

    private final GuestbookMapper guestbookMapper = Mappers.getMapper(GuestbookMapper.class);

    @Test
    public void GuestbookMapperTest(){
        // DTO
        GuestbookDTO guestbookDTO = new GuestbookDTO(1L, "title", "content", "writer", LocalDateTime.now(), LocalDateTime.now());
        // Entity
        Guestbook guestbook = new Guestbook(1L, "Etitle", "Econtent", "Ewriter");

        // DTO, Entity
        GuestbookDTO testDTO = guestbookMapper.entityToDto(guestbook);
        Guestbook testEntity = guestbookMapper.dtoToEntity(guestbookDTO);

        System.out.println("origin DTO: "+ guestbookDTO);
        System.out.println("DTO To Entity: "+ testEntity);
        System.out.println("----------------------");
        System.out.println("origin Entity: "+ guestbook);
        System.out.println("Entity To DTO: "+ testDTO);
    }
}

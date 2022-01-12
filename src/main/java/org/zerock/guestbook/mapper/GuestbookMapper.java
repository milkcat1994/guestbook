package org.zerock.guestbook.mapper;

import org.mapstruct.Mapper;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.entity.Guestbook;

@Mapper
public interface GuestbookMapper {
    Guestbook dtoToEntity(GuestbookDTO dto);

    GuestbookDTO entityToDto(Guestbook entity);
}

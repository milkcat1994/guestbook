package org.zerock.guestbook.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;

@SpringBootTest
public class GuestbookServiceTests {

    @Autowired
    private GuestbookService service;

    @Test
    public void testRegister(){
        GuestbookDTO guestbookDTO = GuestbookDTO.builder()
                .title("Sample Title...")
                .content("Sample Content...")
                .writer("user0")
                .build();

        System.out.println(service.register(guestbookDTO));
    }

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = service.getList(pageRequestDTO);

        System.out.println("PREV: "+ resultDTO.isPrev());
        System.out.println("NEXT: "+ resultDTO.isNext());
        System.out.println("TOTAL: "+ resultDTO.getTotalPage());

        System.out.println("---------------------------------------");
        for(GuestbookDTO guestbookDTO : resultDTO.getDtoList()){
            System.out.println(guestbookDTO);
        }

        System.out.println("=======================================");
        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }

    @Test
    public void testGuestBook(){
        long gno = 123L;

        System.out.println(service.read(gno));
    }

    @Test
    public void testModify(){
        long gno = 123L;
        GuestbookDTO guestbookDTO = service.read(gno);

        System.out.println("EX Modify: " + guestbookDTO);

        guestbookDTO.setTitle("Modify Test1");
        service.modify(guestbookDTO);

        System.out.println("POST Modify: " + service.read(gno));
    }

    @Test
    public void testRemove(){
        long gno = 305L;
        service.remove(gno);

        if(service.read(gno) == null)
            System.out.println("already Removed");
        else
            System.out.println("not Removed");
    }

    @Test
    public void testSearch(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                // 검색 조건
                // t : 제목 / c : 내용 / w : 저자
                .type("tc")
                // 검색 키워드
                .keyword("test")
                .build();

        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = service.getList(pageRequestDTO);

        System.out.println("PREV: "+ resultDTO.isPrev());
        System.out.println("NEXT: "+ resultDTO.isNext());
        System.out.println("TOTAL: "+ resultDTO.getTotalPage());

        System.out.println("---------------------------------------");
        for(GuestbookDTO guestbookDTO : resultDTO.getDtoList()){
            System.out.println(guestbookDTO);
        }

        System.out.println("=======================================");
        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }
}

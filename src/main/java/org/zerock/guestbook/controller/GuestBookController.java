package org.zerock.guestbook.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.service.GuestbookService;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
public class GuestBookController {

    private final GuestbookService service;

    @GetMapping("/")
    public String index(){
        return "redirect:/guestbook/list";
    }

    @GetMapping("/list")
    public void list(@ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model){
        log.info("list.........."+ requestDTO);
        model.addAttribute("result", service.getList(requestDTO));
    }

    // 화면 도식
    @GetMapping("/register")
    public void register(){
        log.info("register get...");
    }

    // 등록 처리
    @PostMapping("/register")
    public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes){
        log.info("dto..." +dto);

        // 새로 추가된 Entity 번호
        Long gno = service.register(dto);
        redirectAttributes.addFlashAttribute("msg", gno);
        redirectAttributes.addFlashAttribute("exPage", "register");

        return "redirect:/guestbook/list";
    }

    @GetMapping({"/read", "/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model){
        log.info("gno: "+ gno);
        GuestbookDTO dto = service.read(gno);
        model.addAttribute("dto", dto);
    }

    @PostMapping("/modify")
    public String modify(GuestbookDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes){

        log.info("post modify...................");
        log.info("dto: "+ dto);

        service.modify(dto);

        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());
        redirectAttributes.addAttribute("gno", dto.getGno());

        return "redirect:/guestbook/read";
    }

    @PostMapping("/remove")
    public String remove(long gno, RedirectAttributes redirectAttributes){
        log.info("gno: "+ gno);
        service.remove(gno);

        // addAttribute와 다르게 새로고침 시 증발하게 된다.
        // 두번 이상 삭제 되지 않기 위해 addFlashAttribute를 이용하여 등록해야한다.
        redirectAttributes.addFlashAttribute("msg", gno);
        redirectAttributes.addFlashAttribute("exPage", "remove");
        return "redirect:/guestbook/list";
    }

}

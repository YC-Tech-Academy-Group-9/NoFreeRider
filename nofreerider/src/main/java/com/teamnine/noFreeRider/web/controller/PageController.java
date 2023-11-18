package com.teamnine.noFreeRider.web.controller;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.service.MemberService;
import com.teamnine.noFreeRider.notification.domain.Notification;
import com.teamnine.noFreeRider.notification.dto.NotificationDto;
import com.teamnine.noFreeRider.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PageController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping("/")
    public String login() {
        return "home";
    }

    @RequestMapping("/signup")
    public String signup(Model model, Authentication authentication) {

        String email = authentication.getName();
        model.addAttribute("useremail", email);
        return "signup";
    }

    @RequestMapping("/main")
    public String index(Model model, Authentication authentication) {

        String email = authentication.getName();
        Member loginMember = memberService.getMemberByEmail(email);
        model.addAttribute("name", loginMember.getMemberName());
        model.addAttribute("temp", loginMember.getMemberTemperature());

        Notification[] notificationList = notificationService.getNotificationListByMember(loginMember);

        NotificationDto[] notificationDtoList = new NotificationDto[notificationList.length];
        for (int i = 0; i < notificationList.length; i++) {
            notificationDtoList[i] = new NotificationDto(notificationList[i].getNotice_title(), notificationList[i].getNotice_content());
        }

        model.addAttribute("notificationList", notificationDtoList);

        return "main";
    }

}

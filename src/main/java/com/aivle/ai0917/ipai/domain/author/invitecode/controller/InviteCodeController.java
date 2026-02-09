package com.aivle.ai0917.ipai.domain.author.invitecode.controller;

import com.aivle.ai0917.ipai.global.security.jwt.CurrentUserId;
import com.aivle.ai0917.ipai.domain.author.invitecode.service.InviteCodeService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/author/manager")
public class InviteCodeController {

    private final InviteCodeService inviteCodeService;

    public InviteCodeController(InviteCodeService inviteCodeService) {
        this.inviteCodeService = inviteCodeService;
    }

    // POST /api/v1/author/manager/code
    @PostMapping("/code")
    public Map<String, Object> createCode(@CurrentUserId Long userId) {
        return inviteCodeService.createInviteCode(userId);
    }
}

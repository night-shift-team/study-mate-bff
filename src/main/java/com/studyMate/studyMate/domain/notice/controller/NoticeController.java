package com.studyMate.studyMate.domain.notice.controller;

import com.studyMate.studyMate.domain.notice.dto.GetNoticePagingDto;
import com.studyMate.studyMate.domain.notice.entity.Notice;
import com.studyMate.studyMate.domain.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
@Tag(name = "Notice API")
public class NoticeController {

    private final NoticeService noticeService;
    // 공지사항 조회 (페이징 최신순 조회)
    @GetMapping("")
    @Operation(summary = "공지사항 전체 내역 조회 (페이징)", description = "공지사항 전체 내역 조회 - 페이지네이션 적용")
    public GetNoticePagingDto getNoticesByPagenation(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        System.out.println("Get Notices By Pagenation Controller");
        return noticeService.findNoticesByPagenation(page, limit);
    }
    // 공지사항 조회 (현재 유효한 내용만)
}

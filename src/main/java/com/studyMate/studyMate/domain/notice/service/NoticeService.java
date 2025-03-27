package com.studyMate.studyMate.domain.notice.service;

import com.studyMate.studyMate.domain.notice.dto.GetNoticePagingDto;
import com.studyMate.studyMate.domain.notice.entity.Notice;
import com.studyMate.studyMate.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public GetNoticePagingDto findNoticesByPagenation(int page, int limit){
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdDt"));
        return new GetNoticePagingDto(noticeRepository.findAll(pageRequest));
    }
}

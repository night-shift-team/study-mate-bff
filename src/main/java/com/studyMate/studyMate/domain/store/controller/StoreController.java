package com.studyMate.studyMate.domain.store.controller;

import com.studyMate.studyMate.domain.store.dto.PageResponseDto;
import com.studyMate.studyMate.domain.store.dto.vo.StoreItemDto;
import com.studyMate.studyMate.domain.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "store")
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    @Operation(summary = "상점 삼품 조회 API", description = "상점 노출 상품 목록 조회하는 기능")
    public PageResponseDto<StoreItemDto> getStoreItems(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        return storeService.getStoreItems(page, limit);
    }
}

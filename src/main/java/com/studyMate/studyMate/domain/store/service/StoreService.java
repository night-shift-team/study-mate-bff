package com.studyMate.studyMate.domain.store.service;

import com.studyMate.studyMate.domain.store.dto.PageResponseDto;
import com.studyMate.studyMate.domain.store.dto.vo.StoreItemDto;
import com.studyMate.studyMate.domain.store.entity.StoreItems;
import com.studyMate.studyMate.domain.store.repository.StoreItemsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StoreService {

    private final StoreItemsRepository storeItemsRepository;

    public PageResponseDto<StoreItemDto> getStoreItems(Integer page, Integer size){
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<StoreItems> storeItems = storeItemsRepository.findAll(pageRequest);
        Page<StoreItemDto> storeItemDto = storeItems.map(StoreItemDto::new);

        return new PageResponseDto<>(storeItemDto);
    }
}

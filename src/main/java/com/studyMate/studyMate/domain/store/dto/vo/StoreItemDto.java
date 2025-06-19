package com.studyMate.studyMate.domain.store.dto.vo;

import com.studyMate.studyMate.domain.store.entity.StoreItems;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreItemDto {
    private String itemId;

    private String itemName;
    private String itemDescription;

    private String itemImage;
    private Integer priceKrw;

    public StoreItemDto(StoreItems storeItems) {
        this.itemId = storeItems.getItemId();
        this.itemName = storeItems.getName();
        this.itemDescription = storeItems.getDescription();
        this.itemImage = storeItems.getItemImage();
        this.priceKrw = storeItems.getPriceKrw();
    }
}

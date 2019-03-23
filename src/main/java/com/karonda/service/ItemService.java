package com.karonda.service;

import com.karonda.error.BusinessException;
import com.karonda.service.model.ItemModel;

import java.util.List;

public interface ItemService {

    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    List<ItemModel> listItem();

    ItemModel getItemById(Integer id);

    boolean decreaseStock(Integer itemId, Integer amount);

    void increaseSales(Integer itemId, Integer amount) throws BusinessException;
}

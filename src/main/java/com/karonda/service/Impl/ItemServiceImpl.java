package com.karonda.service.Impl;

import com.karonda.dao.ItemDOMapper;
import com.karonda.dao.ItemStockDOMapper;
import com.karonda.dataobject.ItemDO;
import com.karonda.dataobject.ItemStockDO;
import com.karonda.error.BusinessException;
import com.karonda.error.EmBusinessError;
import com.karonda.service.ItemService;
import com.karonda.service.model.ItemModel;
import com.karonda.validator.ValidationResult;
import com.karonda.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemDOMapper itemDOMapper;

    @Autowired
    private ItemStockDOMapper itemStockDOMapper;

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {

        ValidationResult result = validator.validate(itemModel);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }

        ItemDO itemDO = convertFromModel(itemModel);
        itemDOMapper.insertSelective(itemDO);

        itemModel.setId(itemDO.getId());

        ItemStockDO itemStockDO = convertItemStockFromModel(itemModel);
        itemStockDOMapper.insertSelective(itemStockDO);

        return getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemDO> itemDOList = itemDOMapper.listItem();

        List<ItemModel> itemModelList = itemDOList.stream().map(itemDO -> {
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
            ItemModel itemModel = convertFromDataObject(itemDO, itemStockDO);
            return itemModel;
        }).collect(Collectors.toList());

        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
        if(itemDO == null){
            return null;
        }

        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());

        ItemModel itemModel = convertFromDataObject(itemDO, itemStockDO);

        return itemModel;
    }

    private ItemDO convertFromModel(ItemModel itemModel){
        if(itemModel == null){
            return null;
        }

        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel, itemDO);

        itemDO.setPrice(itemModel.getPrice().doubleValue());

        return itemDO;
    }

    private ItemStockDO convertItemStockFromModel(ItemModel itemModel){
        if(itemModel == null){
            return null;
        }

        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setItemId(itemModel.getId());
        itemStockDO.setStock(itemModel.getStock());

        return itemStockDO;
    }

    private ItemModel convertFromDataObject(ItemDO itemDO, ItemStockDO itemStockDO){
        if(itemDO == null){
            return null;
        }

        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO, itemModel);

        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));

        itemModel.setStock(itemStockDO.getStock());

        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) {

        int affectedRow = itemStockDOMapper.decreaseStock(itemId, amount);

        return affectedRow > 0;
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {
        itemDOMapper.increaseSales(itemId, amount);
    }
}

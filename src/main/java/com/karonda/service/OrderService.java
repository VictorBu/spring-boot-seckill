package com.karonda.service;

import com.karonda.error.BusinessException;
import com.karonda.service.model.OrderModel;

public interface OrderService {
    OrderModel createOrder(Integer userId, Integer itemId, Integer amount) throws BusinessException;
}

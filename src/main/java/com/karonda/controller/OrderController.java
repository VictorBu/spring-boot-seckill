package com.karonda.controller;

import com.karonda.error.BusinessException;
import com.karonda.error.EmBusinessError;
import com.karonda.response.CommonReturnType;
import com.karonda.service.OrderService;
import com.karonda.service.model.OrderModel;
import com.karonda.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller("order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping(value = "/createorder", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name="itemId") Integer itemId,
                                        @RequestParam(name="amount") Integer amount)
            throws BusinessException {

        Boolean isLogin = (Boolean)httpServletRequest.getSession().getAttribute("LOGIN");
        if(isLogin == null || !isLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }

        UserModel userModel = (UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");

        OrderModel orderModel = orderService.createOrder(userModel.getId(), itemId, amount);

        return CommonReturnType.create(null);
    }
}

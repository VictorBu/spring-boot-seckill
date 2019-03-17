package com.karonda.controller;

import com.karonda.controller.viewobject.UserVO;
import com.karonda.error.BusinessException;
import com.karonda.error.EmBusinessError;
import com.karonda.response.CommonReturnType;
import com.karonda.service.UserService;
import com.karonda.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin // 解决跨域问题
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping(value = "/getotp", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name="telphone") String telphone){
        // 生成 otp 验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);

        // 将 otp 验证码同对应的用户关联 (暂时使用 httpsession 的方式绑定 otp 与手机号)
        httpServletRequest.getSession().setAttribute(telphone, otpCode);

        // 将 otp 验证码通过短信通道发送给用户 (省略，使用控制台输出代替)
        System.out.println(String.format("telphone = %s & otpCode = %s", telphone, otpCode));

        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException{
        UserModel userModel = userService.getUserById(id);

        if(userModel == null){
//            userModel.setEncrptPassword("123");
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        UserVO userVO = convertFromModel(userModel);

        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);

        return userVO;
    }
}

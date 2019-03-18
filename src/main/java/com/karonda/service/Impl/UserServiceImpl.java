package com.karonda.service.Impl;

import com.karonda.dao.UserDOMapper;
import com.karonda.dao.UserPasswordDOMapper;
import com.karonda.dataobject.UserDO;
import com.karonda.dataobject.UserPasswordDO;
import com.karonda.error.BusinessException;
import com.karonda.error.EmBusinessError;
import com.karonda.service.UserService;
import com.karonda.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Override
    public UserModel getUserById(Integer id){
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);

        if(userDO == null){
            return null;
        }

        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());

        return convertFromDataObject(userDO, userPasswordDO);

    }

    @Override
    @Transactional // 事务操作
    public void register(UserModel userModel) throws BusinessException {
        if(userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        if(StringUtils.isEmpty(userModel.getTelphone())
                || StringUtils.isEmpty(userModel.getName())
                || userModel.getGender() == null
                || userModel.getAge() == null
                || StringUtils.isEmpty(userModel.getEncrptPassword())){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        UserDO userDO = convertFromModel(userModel);
        try{
            userDOMapper.insertSelective(userDO);
        }catch (DuplicateKeyException ex){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "手机号码已注册");
        }

        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);

        return;
    }

    private UserDO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }

        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel, userDO);

        return userDO;
    }

    private UserPasswordDO convertPasswordFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }

        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());

        return userPasswordDO;
    }

    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        if(userDO == null){
            return null;
        }

        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO, userModel);

        if(userPasswordDO != null){
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }

        return userModel;
    }
}

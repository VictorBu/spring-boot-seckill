package com.karonda.service;

import com.karonda.error.BusinessException;
import com.karonda.service.model.UserModel;

public interface UserService {

    UserModel getUserById(Integer id);

    void register(UserModel userModel) throws BusinessException;
}

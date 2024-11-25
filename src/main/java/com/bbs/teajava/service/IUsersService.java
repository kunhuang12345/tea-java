package com.bbs.teajava.service;

import com.bbs.teajava.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
public interface IUsersService extends IService<Users> {

    List<Users> getAllUsers();

}

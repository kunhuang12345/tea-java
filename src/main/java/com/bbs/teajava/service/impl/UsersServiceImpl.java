package com.bbs.teajava.service.impl;

import com.bbs.teajava.entity.Users;
import com.bbs.teajava.mapper.UsersMapper;
import com.bbs.teajava.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  用户实现类
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {
    @Autowired
    private UsersMapper usersMapper;

    @Override
    public List<Users> getAllUsers() {
        return usersMapper.getAllUsers();
    }
}

package com.bbs.teajava.mapper;

import com.bbs.teajava.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
@Mapper
public interface UsersMapper extends BaseMapper<Users> {

    @Select("SELECT * FROM users")
    List<Users> getAllUsers();

    @Update("UPDATE users SET role = #{role} WHERE id = #{id}")
    void updateRole(@Param("id") Integer id, @Param("role") Integer role);
}

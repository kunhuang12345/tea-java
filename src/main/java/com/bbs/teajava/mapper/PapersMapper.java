package com.bbs.teajava.mapper;

import com.bbs.teajava.entity.Papers;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
public interface PapersMapper extends BaseMapper<Papers> {

    @Select("select * from papers")
    List<Papers> getAllPapers();

}

package com.bbs.teajava.controller;


import com.bbs.teajava.annotation.Authentication;
import com.bbs.teajava.entity.Papers;
import com.bbs.teajava.service.IPapersService;
import com.bbs.teajava.utils.ApiResultUtils;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hk
 * @since 2024-11-25
 */
@RestController
@RequestMapping("/Papers")
@RequiredArgsConstructor
public class PapersController {

    private final IPapersService papersService;

    @RequestMapping(value = "/GetAllPapers", method = { RequestMethod.GET, RequestMethod.POST} )
    @ApiResponse(description = "获取所有论文")
    public Object getAllPapers() {
        return ApiResultUtils.success(papersService.getAllPapers());
    }

    @RequestMapping(value = "uploadPapers", method = {RequestMethod.POST})
    @ApiResponse(description = "上传论文(返回保存行数)")
    @Authentication
    public Object uploadPapers(@ModelAttribute List<Papers> papers) {
        return papersService.uploadPapers(papers);
    }

    @RequestMapping(value = "", method = {RequestMethod.POST})
    @ApiResponse(description = "上传文件(测试)")
    // @Authentication
    public ApiResultUtils uploadFile (@RequestParam(value = "file")MultipartFile file) {
        return papersService.uploadFile(file);
    }

}

package com.bbs.teajava.controller;

import com.bbs.teajava.annotation.Authentication;
import com.bbs.teajava.service.IPaperAuthorService;
import com.bbs.teajava.utils.ApiResultUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author kunhuang
 */
@RestController
@RequestMapping("/PaperAuthor")
@RequiredArgsConstructor
public class PaperAuthorController {

    private final IPaperAuthorService paperAuthorService;

    @PostMapping(value = "/Add")
    @ApiOperation("添加论文作者, userIds以逗号分隔")
    @Authentication(requireAdmin = true)
    public ApiResultUtils add(@RequestParam(value = "paperId") Integer paperId,
                              @RequestParam(value = "userIds") String userIds) {
        return paperAuthorService.add(paperId, userIds);
    }

    @GetMapping(value = "/GetAuthorList")
    @ApiOperation("通过论文id查询作者信息")
    @Authentication(requireAdmin = true)
    public ApiResultUtils getAuthorList(@RequestParam(value = "paperId") Integer paperId) {
        return paperAuthorService.getAuthorList(paperId);
    }


}

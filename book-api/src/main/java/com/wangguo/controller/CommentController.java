package com.wangguo.controller;

import com.wangguo.base.BaseInfoProperties;
import com.wangguo.bo.CommentBO;
import com.wangguo.grace.result.GraceJSONResult;
import com.wangguo.service.CommentService;
import com.wangguo.vo.CommentVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Api(tags = "CommentController 评论模块的接口")
@RequestMapping("comment")
@RestController
public class CommentController extends BaseInfoProperties {

    @Autowired
    private CommentService commentService;

    /**
     * 创建评论，用于发表评论信息
     *
     * @param commentBO 前端传来的评论信息
     * @return 返回包装的要呈现在前端的评论相关的东西
     * @throws Exception
     */
    @PostMapping("create")
    public GraceJSONResult create(@RequestBody @Valid CommentBO commentBO) throws Exception {
        CommentVO commentVO = commentService.createComment(commentBO);
        return GraceJSONResult.ok(commentVO);
    }

    /**
     * 返回该视频的评论的总数
     * @param vlogId
     * @return
     */
    @GetMapping("counts")
    public GraceJSONResult counts(@RequestParam String vlogId) {
        String countsStr = redis.get(REDIS_VLOG_COMMENT_COUNTS + ":" + vlogId);
        if (StringUtils.isBlank(countsStr)) {
            countsStr = "0";
        }
        return GraceJSONResult.ok(Integer.valueOf(countsStr));
    }

    /**
     * 分页查询评论列表
     * @param vlogId 视频的id
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("list")
    public GraceJSONResult list(@RequestParam String vlogId,
                                @RequestParam(defaultValue = "") String userId,
                                @RequestParam Integer page,
                                @RequestParam Integer pageSize) {
        return GraceJSONResult.ok(commentService.queryVlogComments(vlogId, userId, page, pageSize));

    }
    @PostMapping("like")
    public GraceJSONResult like(@RequestParam String commentId,
                                  @RequestParam String userId) {
        redis.incrementHash(REDIS_VLOG_COMMENT_LIKED_COUNTS, commentId, 1);
        /**
         * 把每个用户喜欢的评论保存下来，用于展示点亮的红心
         */
        redis.setHashValue(REDIS_USER_LIKE_COMMENT, userId + ":" + commentId, "1");
    }
}

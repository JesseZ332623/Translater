package com.jesse.translater.controller.viewcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页视图控制器。
 */
@Controller
public class FrontPageViewController
{
    /**
     * Get 请求方法，跳转至首页。
     * 可能的 URL 为：<a href="http://localhost:8091/">翻译首页</a>
     */
    @GetMapping(path = "/")
    String frontPageView() { return "TranslateFrontPage"; }
}

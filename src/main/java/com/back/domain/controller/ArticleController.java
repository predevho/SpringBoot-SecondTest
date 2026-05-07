package com.back.domain.controller;

import com.back.domain.entity.Article;
import com.back.domain.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("articleList", articleService.getList());
        return "article_list";
    }

    @GetMapping("/create")
    public String articleCreate() {
        return "article_create";
    }

    @PostMapping("/create")
    public String articleCreate(Article article) {
        articleService.createArticle(article.getTitle(), article.getContent());
        return "redirect:/article/list";
    }

    @GetMapping("/detail/{id}")
    public String articleDetail(Model model, @PathVariable("id") long id) {
        Article article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "article_detail";
    }

    @GetMapping("/modify/{id}")
    public String articleModify(Model model, @PathVariable("id") long id) {
        Article article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "article_modify";
    }

    @PostMapping("/modify/{id}")
    public String articleModify(Article article, @PathVariable("id") long id) {
        articleService.modifyArticle(id, article.getTitle(), article.getContent());
        return "redirect:/article/detail/" + id;
    }

    @PostMapping("/delete/{id}")
    public String articleDelete(@PathVariable("id") long id) {
        Article article = articleService.getArticleById(id);
        articleService.deleteArticle(article);
        return "redirect:/article/list";
    }

}


package com.back.domain.controller;

import com.back.domain.entity.Article;
import com.back.domain.service.ArticleService;
import com.back.user.entity.SiteUser;
import com.back.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "kw", defaultValue = "") String kw) {
        model.addAttribute("articleList", articleService.getList(kw));
        model.addAttribute("kw", kw);
        return "article_list";
    }

    @GetMapping("/create")
    public String articleCreate() {
        return "article_create";
    }

    @PostMapping("/create")
    public String articleCreate(Article article, Principal principal) {
        SiteUser author = userService.getUser(principal.getName());
        articleService.createArticle(article.getTitle(), article.getContent(), author);
        return "redirect:/article/list";
    }

    @GetMapping("/detail/{id}")
    public String articleDetail(Model model, @PathVariable("id") long id) {
        Article article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "article_detail";
    }

    @GetMapping("/modify/{id}")
    public String articleModify(Model model, @PathVariable("id") long id, Principal principal) {
        Article article = articleService.getArticleById(id);
        if (!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        model.addAttribute("article", article);
        return "article_modify";
    }

    @PostMapping("/modify/{id}")
    public String articleModify(Article article, @PathVariable("id") long id, Principal principal) {
        Article existing = articleService.getArticleById(id);
        if(!existing.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        articleService.modifyArticle(id, article.getTitle(), article.getContent());
        return "redirect:/article/detail/" + id;
    }

    @PostMapping("/delete/{id}")
    public String articleDelete(@PathVariable("id") long id, Principal principal) {
        Article article = articleService.getArticleById(id);
        if(!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        articleService.deleteArticle(article);
        return "redirect:/article/list";
    }


}


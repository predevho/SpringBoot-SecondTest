package com.back.domain.service;

import com.back.DataNotFoundException;
import com.back.domain.entity.Article;
import com.back.domain.repository.ArticleRepository;
import com.back.user.entity.SiteUser;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    private Specification<Article> search(String kw){
        return new Specification<Article>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                query.distinct(true);
                return criteriaBuilder.or(
                        criteriaBuilder.like(root.get("title"), "%" + kw + "%"),
                        criteriaBuilder.like(root.get("content"), "%" + kw + "%"),
                        criteriaBuilder.like(root.get("author").get("username"), "%" + kw + "%"),
                        criteriaBuilder.like(root.get("author").get("nickname"), "%" + kw + "%")
                );
            }
        };
    }

    public void createArticle(String title, String content, SiteUser author) {
        Article article = new Article();
        article.setAuthor(author);
        article.setTitle(title);
        article.setContent(content);
        article.setCreateDate(LocalDateTime.now());
        articleRepository.save(article);
    }

    public void modifyArticle(Long id, String title, String content) {
        Article article = getArticleById(id);
        article.setTitle(title);
        article.setContent(content);
        article.setModifyDate(LocalDateTime.now());
        articleRepository.save(article);
    }

    public void deleteArticle(Article article) {
        articleRepository.delete(article);
    }

    public List<Article> getList(String kw) {
        Specification<Article> search = search(kw);
        return articleRepository.findAll(search);
    }

    public Article getArticleById(Long id) {
        Optional<Article> op = articleRepository.findById(id);
        if(op.isPresent()) {
            return op.get();
        }else{
            throw new DataNotFoundException("article not found");
        }

    }

}

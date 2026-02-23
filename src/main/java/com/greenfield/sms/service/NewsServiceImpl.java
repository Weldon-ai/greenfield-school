package com.greenfield.sms.service;

import com.greenfield.sms.model.News;
import com.greenfield.sms.repository.NewsRepository;
import com.greenfield.sms.service.NewsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository repository;

    public NewsServiceImpl(NewsRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<News> getLatestNews() {
        return repository.findTop5ByOrderByPublishDateDesc();
    }

    @Override
    public News saveNews(News news) {
        return repository.save(news);
    }
}

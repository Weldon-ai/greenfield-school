package com.greenfield.sms.service;

import com.greenfield.sms.model.News;
import java.util.List;

public interface NewsService {

    List<News> getLatestNews();

    News saveNews(News news);
}

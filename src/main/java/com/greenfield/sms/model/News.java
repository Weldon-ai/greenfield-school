package com.greenfield.sms.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 3000)
    private String content;

    private LocalDate publishDate;

    public News() {}

    public News(String title, String content, LocalDate publishDate) {
        this.title = title;
        this.content = content;
        this.publishDate = publishDate;
    }

    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDate getPublishDate() { return publishDate; }
    public void setPublishDate(LocalDate publishDate) { this.publishDate = publishDate; }
}

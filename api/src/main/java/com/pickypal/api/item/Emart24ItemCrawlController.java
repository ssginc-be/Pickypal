package com.pickypal.api.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value="/crawler")
@RequiredArgsConstructor
public class Emart24ItemCrawlController {
    private final Emart24ItemCrawlService service;

    @GetMapping("/save")
    public void saveItems() throws IOException {
        service.saveEventItems();
    }
}

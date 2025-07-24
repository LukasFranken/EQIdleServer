package de.instinct.base.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public abstract class BaseController {

    @GetMapping("/ping")
    public ResponseEntity<Void> ping() {
        return ResponseEntity.ok().build();
    }
    
}
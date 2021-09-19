package com.document.manager.rest;

import com.document.manager.dto.LoginDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/home")
public class HomeController {

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
//
//        return new ResponseEntity<>(null, OK);
//    }
}

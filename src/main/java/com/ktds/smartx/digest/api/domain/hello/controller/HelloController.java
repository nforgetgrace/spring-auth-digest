package com.ktds.smartx.digest.api.domain.hello.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * packageName    : com.ktds.smartx.digest.api.domain.hello.controller
 * fileName       : HelloController
 * author         : Jae Gook Jung
 * date           : 2/1/24
 * description    :
 * project name   : digest-auth
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/1/24        Jae Gook Jung       최초 생성
 */
@RestController
public class HelloController {
    @GetMapping("/auth")
    public Object home() {
        Map<String, String> result = new HashMap<>();
        result.put("result", "SUCCESS");
        return result;
    }

    @RequestMapping(value = "/echo/{message}", method = RequestMethod.GET)
    public ResponseEntity testEcho(@PathVariable String message) {
        return new ResponseEntity(message, HttpStatus.OK);
    }

}

package kltn.userservice.user.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class A {

    @GetMapping("/api/v1/order")
    public String loadDataBySocialAccount() {
        System.out.println("j");
        return "hello world";
    }

    @GetMapping("/api/v1/order1")
    public String loadDataBySocialAccount1() {
        System.out.println("j");
        return "hello world";
    }
}

package lk.ijse.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/health")
@CrossOrigin
public class HealthTestController {
    @GetMapping
    public String health() {return "HealthTest controller is running";}
}

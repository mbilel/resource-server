package com.example.demo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Controller {
    @GetMapping("/pet")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String pet(Principal principal) {
        return "Hi " + principal.getName() + ". My pet is dog";
    }

    @GetMapping("/favouritePet")
    public String favouritePet(Principal principal) {
        return "Hi " + principal.getName() + ". My favourite pet is cat";
    }
}

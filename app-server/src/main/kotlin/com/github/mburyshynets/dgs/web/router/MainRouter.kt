package com.github.mburyshynets.dgs.web.router

import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router

@Component
class MainRouter : RouterFunction<ServerResponse> by router({

    GET(pattern = "/api/health") {
        ServerResponse
            .ok()
            .body("UP")
    }
})

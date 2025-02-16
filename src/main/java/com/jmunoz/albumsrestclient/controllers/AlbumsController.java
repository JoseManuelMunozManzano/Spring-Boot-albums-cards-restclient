package com.jmunoz.albumsrestclient.controllers;

import com.jmunoz.albumsrestclient.model.Player;
import com.jmunoz.albumsrestclient.service.FootballClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/albums")
public class AlbumsController {

    private final FootballClientService footballClientService;

    // Inyectamos el service.
    public AlbumsController(FootballClientService footballClientService) {
        this.footballClientService = footballClientService;
    }

    @GetMapping("/players")
    public List<Player> getPlayers() {
        return footballClientService.getPlayers();
    }

    @GetMapping("/players/{id}")
    public Optional<Player> getPlayer(@PathVariable String id) {
        return footballClientService.getPlayer(id);
    }
}

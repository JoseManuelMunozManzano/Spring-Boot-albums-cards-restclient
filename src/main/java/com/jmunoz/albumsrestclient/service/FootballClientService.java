package com.jmunoz.albumsrestclient.service;

import com.jmunoz.albumsrestclient.model.Player;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Service
public class FootballClientService {

    private RestClient restClient;

    // Inyectamos el bean RestClient usando el constructor.
    public FootballClientService(RestClient restClient) {
        this.restClient = restClient;
    }

    // Recuperamos la data del API RESTful remoto.
    public List<Player> getPlayers() {
        // Como se devuelve una lista de Players nos hace falta usar
        // ParameterizedTypeReference, que nos permite pasar un tipo genérico.
        // Para capturar el tipo genérico es necesario definir una subclase de
        // ParameterizedTypeReference, cosa que se hace definiendo una clase inline
        // anónima (de ahí las llaves al final).
        return restClient.get().uri("/players").retrieve()
                .body(new ParameterizedTypeReference<List<Player>>() {});
    }

    public Optional<Player> getPlayer(String id) {
        // El méto-do get devuelve un objeto que puede usarse para configurar las
        // propiedades de la petición, como el URI, cabeceras, y otros.
        //
        // Cuando llamamos al méto-do exchange, RestClient realiza la llamada al
        // API RESTful remoto. También provee un handler para gestionar la respuesta.
        //
        // En el handler de la repuesta controlamos lo que ocurre:
        //   - Si Player no se encuentra devolvemos un objeto vacío
        //   - Si Player se encuentra, usamos el méto-do bodyTo, que nos permite
        //        pasar un tipo que usaremos para deserializar la respuesta (en este claso Player).
        return restClient.get().uri("/players/{id}", id)
                .exchange((request, response) -> {
                    if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                        return Optional.empty();
                    }
                    return Optional.ofNullable(response.bodyTo(Player.class));
                });
    }
}

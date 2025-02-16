package com.jmunoz.albumsrestclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

// Clase de configuración donde definimos un bean RestClient.
@Configuration
public class AlbumsConfiguration {

    // Configuramos la URL del servidor remoto.
    // Con la anotación @Values podemos inyectar valores de fuentes externas,
    // como ficheros de configuración o variables de entorno.
    //
    // El programa intentará obtener primero el valor asociado a la propiedad
    // football.api.url. Si no la encuentra, tomarå la por defecto, es decir,
    // http://localhost:8080
    @Value("${football.api.url:http://localhost:8080}")
    String baseURI;

    @Bean
    RestClient restClient() {
        return RestClient.create(baseURI);
    }
}

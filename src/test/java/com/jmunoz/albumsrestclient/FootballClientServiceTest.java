package com.jmunoz.albumsrestclient;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.jmunoz.albumsrestclient.model.Player;
import com.jmunoz.albumsrestclient.service.FootballClientService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"football.api.url=http://localhost:7979"})
public class FootballClientServiceTest {

    private static WireMockServer wireMockServer;

    @Autowired
    FootballClientService footballClientService;

    // Ejecutamos la inicialización del server Wiremock antes de ejecutar cada test.
    // Configuramos el server Wiremock para que escuche en el puerto 7979.
    // Este valor debe ser el mismo indicado en la configuración pasada en @SpringBootTest
    @BeforeAll
    static void init() {
        wireMockServer = new WireMockServer(7979);
        wireMockServer.start();
        WireMock.configureFor(7979);
    }

    @Test
    void getPlayersTest() {

        // ARRANGE
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/players"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [
                                    {
                                        "id": "325636",
                                        "jerseyNumber": 11,
                                        "name": "Alexia PUTELLAS",
                                        "position": "Midfielder",
                                        "dateOfBirth": "1994-02-04"
                                    },
                                    {
                                        "id": "396930",
                                        "jerseyNumber": 2,
                                        "name": "Ona BATLLE",
                                        "position": "Defender",
                                        "dateOfBirth": "1999-06-10"
                                    }
                                ]""")));

        // ACT
        List<Player> players = footballClientService.getPlayers();

        // ASSERT
        assertEquals(2, players.size());

        List<Player> expectedPlayers = List.of(
                new Player("325636", 11, "Alexia PUTELLAS", "Midfielder", LocalDate.of(1994, 2, 4)),
                new Player("396930", 2, "Ona BATLLE", "Defender", LocalDate.of(1999, 6, 10))
        );
        assertArrayEquals(expectedPlayers.toArray(), players.toArray());
    }

    @Test
    void getPlayerTest() {

        // ARRANGE
        // Indicamos el resultado esperado del servicio remoto.
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/players/325636"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "id": "325636",
                                    "jerseyNumber": 11,
                                    "name": "Alexia PUTELLAS",
                                    "position": "Midfielder",
                                    "dateOfBirth": "1994-02-04"
                                }
                                """)));

        // ACT
        // Llamamos al méto-do getPlayer(). Este méto-do depende del servicio remoto.
        Optional<Player> player = footballClientService.getPlayer("325636");

        // ASSERT
        // Validamos el resultado
        Player expectedPlayer = new Player("325636", 11, "Alexia PUTELLAS",
                "Midfielder", LocalDate.of(1994, 2, 4));
        assertEquals(expectedPlayer, player.get());
    }

    @Test
    void getPlayer_notFound() {

        // ARRANGE
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/players/8888"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(404)));

        // ACT
        Optional<Player> player = footballClientService.getPlayer("8888");

        // ASSERT
        assertTrue(player.isEmpty());
    }
}

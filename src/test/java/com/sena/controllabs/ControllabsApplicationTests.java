package com.sena.controllabs;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.sena.controllabs.entity.RoomEntity;
import com.sena.controllabs.repository.RoomRepository;

@SpringBootTest
@AutoConfigureMockMvc
class ControllabsApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    void clearRooms() {
        roomRepository.deleteAll();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void roomCrudUsesJpaServiceAndDtoMappings() throws Exception {
        String response = mockMvc.perform(post("/api/v1/rooms")
                        .servletPath("/api/v1")
                        .pathInfo("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Laboratorio Redes",
                                  "locationBlock": "Bloque A",
                                  "totalPcs": 30
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Laboratorio Redes")))
                .andExpect(jsonPath("$.bloque", is("Bloque A")))
                .andExpect(jsonPath("$.capacidadPcs", is(30)))
                .andExpect(jsonPath("$.estado", is("OPERATIVO")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Matcher idMatcher = Pattern.compile("\"id\"\\s*:\\s*\"([^\"]+)\"").matcher(response);
        assertTrue(idMatcher.find());
        String roomId = idMatcher.group(1);

        mockMvc.perform(get("/api/v1/rooms").servletPath("/api/v1").pathInfo("/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(roomId)));

        mockMvc.perform(get("/api/v1/rooms").servletPath("/api/v1").pathInfo("/rooms")
                        .param("block", "Bloque A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)));

        mockMvc.perform(get("/api/v1/rooms/" + roomId)
                        .servletPath("/api/v1")
                        .pathInfo("/rooms/" + roomId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Laboratorio Redes")));

        mockMvc.perform(delete("/api/v1/rooms/" + roomId)
                        .servletPath("/api/v1")
                        .pathInfo("/rooms/" + roomId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/rooms/" + roomId)
                        .servletPath("/api/v1")
                        .pathInfo("/rooms/" + roomId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createRoomRejectsInvalidRequest() throws Exception {
        mockMvc.perform(post("/api/v1/rooms")
                        .servletPath("/api/v1")
                        .pathInfo("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": " ",
                                  "locationBlock": "Bloque A",
                                  "totalPcs": 0
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void roomFilterReturnsOnlyRoomsFromRequestedBlock() throws Exception {
        RoomEntity roomInBlockA = new RoomEntity();
        roomInBlockA.setNombre("Laboratorio A");
        roomInBlockA.setBloque("Bloque A");
        roomInBlockA.setCapacidadPcs(20);
        roomRepository.save(roomInBlockA);

        RoomEntity roomInBlockB = new RoomEntity();
        roomInBlockB.setNombre("Laboratorio B");
        roomInBlockB.setBloque("Bloque B");
        roomInBlockB.setCapacidadPcs(25);
        roomRepository.save(roomInBlockB);

        mockMvc.perform(get("/api/v1/rooms").servletPath("/api/v1").pathInfo("/rooms")
                        .param("block", "Bloque B"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Laboratorio B")));
    }
}

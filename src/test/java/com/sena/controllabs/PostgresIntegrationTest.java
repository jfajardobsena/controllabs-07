package com.sena.controllabs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.sena.controllabs.entity.RoomEntity;
import com.sena.controllabs.repository.RoomRepository;

@SpringBootTest
@ContextConfiguration(initializers = PostgresIntegrationTest.PostgresProperties.class)
@Import(PostgresIntegrationTest.JdbcConfiguration.class)
@EnabledIfSystemProperty(named = "postgres.integration", matches = "true")
class PostgresIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    void connectsToExpectedDatabaseAndUser() {
        Map<String, Object> connection = jdbcTemplate.queryForMap(
                "SELECT current_database() AS database_name, current_user AS user_name");

        assertEquals("controllabs", connection.get("database_name"));
        assertEquals("postgres", connection.get("user_name"));
    }

    @Test
    @Transactional
    void persistsAndQueriesRoomInPostgres() {
        String roomName = "PostgreSQL integration " + UUID.randomUUID();
        RoomEntity room = new RoomEntity();
        room.setNombre(roomName);
        room.setBloque("Test");
        room.setCapacidadPcs(1);

        RoomEntity savedRoom = roomRepository.saveAndFlush(room);

        assertTrue(roomRepository.findById(savedRoom.getId()).isPresent());
        assertEquals(roomName, roomRepository.findByBloque("Test").stream()
                .filter(foundRoom -> foundRoom.getId().equals(savedRoom.getId()))
                .findFirst()
                .orElseThrow()
                .getNombre());

        roomRepository.deleteById(savedRoom.getId());
        roomRepository.flush();
        assertFalse(roomRepository.existsById(savedRoom.getId()));
    }

    static class PostgresProperties
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext context) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + System.getenv().getOrDefault(
                            "DB_URL", "jdbc:postgresql://localhost:5432/controllabs"),
                    "spring.datasource.username=" + System.getenv().getOrDefault(
                            "DB_USERNAME", "postgres"),
                    "spring.datasource.password=" + System.getenv().getOrDefault("DB_PASSWORD", ""),
                    "spring.datasource.driver-class-name=org.postgresql.Driver",
                    "spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect",
                    "spring.jpa.hibernate.ddl-auto=validate",
                    "spring.jpa.show-sql=false",
                    "spring.jpa.open-in-view=false")
                    .applyTo(context.getEnvironment());
        }
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class JdbcConfiguration {

        @Bean
        JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }
    }
}

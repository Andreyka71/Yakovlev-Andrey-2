package homework.Yakovlev_Andrey_2;

import homework.Yakovlev_Andrey_2.entity.UserAudit;
import homework.Yakovlev_Andrey_2.service.UserAuditService;

import java.time.Duration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.scylladb.ScyllaDBContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class UserAuditServiceIntegrationTest {

    @Container
    private static final ScyllaDBContainer scyllaDBContainer =
        new ScyllaDBContainer(DockerImageName.parse("scylladb/scylla:4.1.0"))
            .withExposedPorts(9042)
            .withStartupTimeout(Duration.ofSeconds(60))
            .withCommand("--smp 1");

     @BeforeAll
    static void setUp() {
        System.setProperty("scylla.port", String.valueOf(scyllaDBContainer.getMappedPort(9042)));
        scyllaDBContainer.start();
    }

    @Autowired
    private UserAuditService userAuditService;

    @Test
    void createAudit_positive() {
        UUID userId = UUID.randomUUID();
        String actionType = "LOGIN";
        String details = "Successful login";

        userAuditService.createAuditRecord(userId, actionType, details);

        List<UserAudit> records = userAuditService.getAuditByUserId(userId);
        assertEquals(1, records.size());
        assertEquals(actionType, records.get(0).getActionType());
    }

    @Test
    void createAudit_negative_nullUserId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userAuditService.createAuditRecord(null, "LOGIN", "details");
        });
        assertEquals("UserId cannot be null", exception.getMessage());
    }

    @Test
    void getAuditByUserId_positive() {
        UUID userId = UUID.randomUUID();
        userAuditService.createAuditRecord(userId, "ACTION1", "Details1");
        userAuditService.createAuditRecord(userId, "ACTION2", "Details2");

        List<UserAudit> records = userAuditService.getAuditByUserId(userId);
        assertEquals(2, records.size());
    }

    @Test
    void getAuditByUserId_negative() {
        UUID nonExistentUserId = UUID.randomUUID();
        List<UserAudit> records = userAuditService.getAuditByUserId(nonExistentUserId);
        assertTrue(records.isEmpty());
    }
}
package homework.Yakovlev_Andrey_2.entity;

import lombok.Data;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Table("user_audit")
public class UserAudit {

    @PrimaryKey
    private UserKey key;
    
    @Column("action_type")
    private String actionType;

    @Column("details")
    private String details;

    @Data
    @PrimaryKeyClass
    public static class UserKey {
        @PrimaryKeyColumn(name = "user_id", type = PrimaryKeyType.PARTITIONED)
        private UUID userId;

        @PrimaryKeyColumn(name = "action_time", type = PrimaryKeyType.CLUSTERED)
        private Instant actionTime;
    }
}

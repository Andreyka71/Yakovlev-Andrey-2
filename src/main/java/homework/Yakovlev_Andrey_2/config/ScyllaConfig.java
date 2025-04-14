package homework.Yakovlev_Andrey_2.config;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import java.net.InetSocketAddress;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScyllaConfig {

  @Value("${scylla.port}")
  private int port;

  @Bean
  public CqlSession cqlSession(CqlSessionBuilder sessionBuilder) {
    InetSocketAddress address = InetSocketAddress.createUnresolved("127.0.0.1", port);
    sessionBuilder = sessionBuilder.addContactPoint(address);
    sessionBuilder.withKeyspace((CqlIdentifier) null).withLocalDatacenter("datacenter1");

    try (CqlSession session = sessionBuilder.build()) {

      SimpleStatement statement =
          SchemaBuilder.createKeyspace("audit_keyspace")
              .ifNotExists()
              .withNetworkTopologyStrategy(Map.of("datacenter1", 1))
              .build();
      session.execute(statement);
      session.execute(
          """
            CREATE TABLE IF NOT EXISTS audit_keyspace.user_audit (
                user_id UUID,
                action_time TIMESTAMP,
                action_type TEXT,
                details TEXT,
                PRIMARY KEY ((user_id), action_time)
            ) WITH CLUSTERING ORDER BY (action_time DESC)
               AND default_time_to_live = 31536000
            """);

      return sessionBuilder.withKeyspace("audit_keyspace").build();
    }
  }
}

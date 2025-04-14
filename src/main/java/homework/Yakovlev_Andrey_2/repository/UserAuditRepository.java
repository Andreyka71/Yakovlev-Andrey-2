package homework.Yakovlev_Andrey_2.repository;

import homework.Yakovlev_Andrey_2.entity.UserAudit;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserAuditRepository extends CassandraRepository<UserAudit, UserAudit.UserKey> {
    
    @Query("SELECT * FROM user_audit WHERE user_id = ?0")
    List<UserAudit> findByUserId(UUID userId);
}

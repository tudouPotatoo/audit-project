package com.tudou.repository;
import com.tudou.pojo.UserOperationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOperationLogRepository extends JpaRepository<UserOperationLog, Long> {
}

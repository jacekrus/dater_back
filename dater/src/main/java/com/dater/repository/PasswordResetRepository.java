package com.dater.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dater.model.PasswordResetEntity;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordResetEntity, String> {
	
	Optional<PasswordResetEntity> findByEmail(String email);
	
	@Query("from PasswordResetEntity where createTime < :time")
	List<PasswordResetEntity> findRequestedBefore(@Param("time") LocalDateTime time);
	
	@Modifying
	@Query("delete from PasswordResetEntity where id = :reqId")
	void deleteRequestById(@Param("reqId") String requestId);

}

package com.devcv.member.repository;

import com.devcv.member.domain.MemberLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface MemberLogRepository extends CrudRepository<MemberLog,Long> {
}

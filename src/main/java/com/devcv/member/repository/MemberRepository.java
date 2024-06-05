package com.devcv.member.repository;

import com.devcv.member.domain.Member;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
@Transactional
public interface MemberRepository extends CrudRepository<Member, Long> {

    @Query(value = "SELECT * FROM member WHERE email=:email", nativeQuery = true)
    Member findMemberByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM member WHERE userid=:userid", nativeQuery = true)
    Member findMemberByUserId(@Param("userid") Long userid);

    @Query(value = "SELECT * FROM member WHERE username=:username AND phone=:phone", nativeQuery = true)
    Member findMemberByUserNameAndPhone(@Param("username") String username,@Param("phone") String phone);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE member SET password=:password WHERE userid=:userid", nativeQuery = true)
    int updatePasswordByUserId(@Param("password") String password, @Param("userid") Long userid);

//    @Modifying(clearAutomatically = true)
//    @Query(value = "UPDATE member SET member WHERE userid=:userid", nativeQuery = true)
//    int updateMemberByUserId(@Param("userid") Long userid);
}

package com.devcv.member.repository;

import com.devcv.member.domain.Member;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/*
*   어노테이션 정리
*   @Transactional : 하위 메서드 실행 실패시 DB데이터 쿼리수행 최근데이터로 자동롤백
*   @Query : CrudRepository의 기본 메서드 커스텀
*   @Modifying( clearAutomatically= true) : 영속성 컨텍스트 자동 clear.
*/
@Transactional
public interface MemberRepository extends CrudRepository<Member, Long> {

    @Query(value = "SELECT * FROM tb_member WHERE email=:email", nativeQuery = true)
    Member findMemberByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM tb_member WHERE memberid=:memberid", nativeQuery = true)
    Member findMemberBymemberid(@Param("memberid") Long memberid);

    @Query(value = "SELECT * FROM tb_member WHERE membername=:membername AND phone=:phone", nativeQuery = true)
    Member findMemberByMemberNameAndPhone(@Param("membername") String membername,@Param("phone") String phone);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE tb_member SET password=:password WHERE memberid=:memberid", nativeQuery = true)
    int updatePasswordByMemberId(@Param("password") String password, @Param("memberid") Long memberid);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE tb_member SET membername=:membername, email=:email, password=:password, nickname=:nickname, phone=:phone, address=:address, social=:social, company=:company, job=:job, stack=:stack WHERE memberid=:memberid", nativeQuery = true)
    int updateMemberByMemberId(@Param("membername") String membername, @Param("email") String email, @Param("password") String password, @Param("nickname") String nickname, @Param("phone") String phone, @Param("address") String address, @Param("social") String  social, @Param("company") String company, @Param("job") String job, @Param("stack") String stack, @Param("memberid") Long memberid);

}

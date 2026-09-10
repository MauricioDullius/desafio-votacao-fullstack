package br.com.db.system.votingsystem.v1.repository;

import br.com.db.system.votingsystem.v1.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member,Long> {

    @Query("SELECT m FROM Member m where m.cpf =:cpf")
    Member findMemberByCpf(@Param("cpf") String cpf);
}

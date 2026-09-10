package br.com.db.system.votingsystem.v1.repository;

import br.com.db.system.votingsystem.v1.model.entity.Assembly;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssemblyRepository extends JpaRepository<Assembly, Long> {}

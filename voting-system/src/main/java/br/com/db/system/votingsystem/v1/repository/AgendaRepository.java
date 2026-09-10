package br.com.db.system.votingsystem.v1.repository;

import br.com.db.system.votingsystem.v1.model.entity.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {}
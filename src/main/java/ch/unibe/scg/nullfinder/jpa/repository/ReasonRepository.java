package ch.unibe.scg.nullfinder.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.unibe.scg.nullfinder.jpa.entity.Reason;

public interface ReasonRepository extends JpaRepository<Reason, Long> {

}

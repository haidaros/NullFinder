package ch.unibe.scg.nullfinder.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.unibe.scg.nullfinder.jpa.entity.BadCompilationUnit;

public interface BadCompilationUnitRepository extends
		JpaRepository<BadCompilationUnit, Long> {

}

package ch.unibe.scg.nullfinder.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

public interface NullCheckRepository extends JpaRepository<NullCheck, Long> {

}

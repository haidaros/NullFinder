package ch.unibe.scg.nullfinder.jpa.repository;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.scg.nullfinder.NullCheck;

public interface INullCheckRepository extends CrudRepository<NullCheck, Long> {

}

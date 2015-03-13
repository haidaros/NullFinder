package ch.unibe.scg.nullfinder.jpa.repository;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.scg.nullfinder.feature.reason.Reason;

public interface IReasonRepository extends CrudRepository<Reason, Long> {

}

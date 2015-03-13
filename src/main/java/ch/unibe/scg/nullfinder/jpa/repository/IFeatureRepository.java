package ch.unibe.scg.nullfinder.jpa.repository;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.scg.nullfinder.feature.Feature;

public interface IFeatureRepository extends CrudRepository<Feature, Long> {

}

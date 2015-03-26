package ch.unibe.scg.nullfinder.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.unibe.scg.nullfinder.feature.Feature;

public interface FeatureRepository extends JpaRepository<Feature, Long> {

}

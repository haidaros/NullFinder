package ch.unibe.scg.nullfinder.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.unibe.scg.nullfinder.jpa.entity.Node;

public interface NodeRepository extends JpaRepository<Node, Long> {

}

package ch.unibe.scg.nullfinder.jpa.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.scg.nullfinder.ast.CompilationUnit;
import ch.unibe.scg.nullfinder.ast.Node;

public interface INodeRepository extends CrudRepository<Node, Long> {

	List<Node> findByCompilationUnitAndClassNameAndBeginLineAndBeginColumnAndEndLineAndEndColumn(
			CompilationUnit compilationUnit, String className, int beginLine,
			int beginColumn, int endLine, int endColumn);

}

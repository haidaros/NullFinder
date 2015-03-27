package ch.unibe.scg.nullfinder.batch;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.unibe.scg.nullfinder.jpa.entity.CompilationUnit;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

@Component
public class NullFinderProcessor implements
		ItemProcessor<Path, CompilationUnit> {

	@Autowired
	protected JavaSourceProcessor javaSourceProcessor;
	@Autowired
	protected CompilationUnitProcessor compilationUnitProcessor;
	@Autowired
	protected NullCheckProcessor nullCheckProcessor;

	@Override
	public CompilationUnit process(Path javaSourcePath)
			throws UnparsableException, IOException {
		CompilationUnit compilationUnit = this.javaSourceProcessor
				.process(javaSourcePath);
		List<NullCheck> nullChecks = this.compilationUnitProcessor
				.process(compilationUnit);
		for (NullCheck nullCheck : nullChecks) {
			this.nullCheckProcessor.process(nullCheck);
		}
		return compilationUnit;
	}

}

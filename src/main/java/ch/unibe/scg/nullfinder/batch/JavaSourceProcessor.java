package ch.unibe.scg.nullfinder.batch;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import ch.unibe.scg.nullfinder.jpa.entity.CompilationUnit;

import com.github.javaparser.JavaParser;

@Component
public class JavaSourceProcessor implements
		ItemProcessor<Path, CompilationUnit> {

	@Override
	public CompilationUnit process(Path javaSourcePath)
			throws UnparsableException, IOException {
		try {
			com.github.javaparser.ast.CompilationUnit javaParserCompilationUnit = JavaParser
					.parse(javaSourcePath.toFile());
			CompilationUnit compilationUnit = new CompilationUnit(
					javaParserCompilationUnit, javaSourcePath);
			javaParserCompilationUnit.setData(compilationUnit);
			return compilationUnit;
		} catch (Throwable throwable) {
			// parsing may throw ParseException | TokenMgrError |
			// NullPointerException | StackOverflowError or maybe more...
			throw new UnparsableException(javaSourcePath, throwable);
		}
	}

}

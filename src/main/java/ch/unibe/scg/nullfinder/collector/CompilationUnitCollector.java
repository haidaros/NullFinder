package ch.unibe.scg.nullfinder.collector;

import java.io.IOException;
import java.nio.file.Path;

import ch.unibe.scg.nullfinder.ast.CompilationUnit;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;

public class CompilationUnitCollector implements
		ICollector<Path, CompilationUnit> {

	@Override
	public CompilationUnit collect(Path path) throws ParseException,
			IOException {
		com.github.javaparser.ast.CompilationUnit javaParserCompilationUnit = JavaParser
				.parse(path.toFile());
		CompilationUnit compilationUnit = new CompilationUnit(
				javaParserCompilationUnit, path);
		javaParserCompilationUnit.setData(compilationUnit);
		return compilationUnit;
	}

}

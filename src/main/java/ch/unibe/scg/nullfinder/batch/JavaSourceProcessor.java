package ch.unibe.scg.nullfinder.batch;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.ast.CompilationUnit;
import ch.unibe.scg.nullfinder.collector.CompilationUnitCollector;
import ch.unibe.scg.nullfinder.collector.FeatureCollector;
import ch.unibe.scg.nullfinder.collector.NullCheckCollector;
import ch.unibe.scg.nullfinder.feature.Feature;
import ch.unibe.scg.nullfinder.feature.extractor.UnextractableException;

import com.github.javaparser.ParseException;

public class JavaSourceProcessor implements
		ItemProcessor<Path, CompilationUnit> {

	protected CompilationUnitCollector compilationUnitCollector;
	protected NullCheckCollector nullCheckCollector;
	protected FeatureCollector featureCollector;

	public JavaSourceProcessor() throws NoSuchMethodException,
			SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		this.compilationUnitCollector = new CompilationUnitCollector();
		this.nullCheckCollector = new NullCheckCollector();
		this.featureCollector = new FeatureCollector();
	}

	@Override
	public CompilationUnit process(Path javaSourcePath) throws ParseException,
			IOException, UnextractableException {
		CompilationUnit compilationUnit = this.compilationUnitCollector
				.collect(javaSourcePath);
		List<NullCheck> nullChecks = this.nullCheckCollector
				.collect(compilationUnit);
		List<Feature> features = new ArrayList<>();
		for (NullCheck nullCheck : nullChecks) {
			// TODO handle unextractable
			features.addAll(this.featureCollector.collect(nullCheck));
		}
		// connect
		return compilationUnit;
	}

}

package ch.unibe.scg.nullfinder.batch;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.ast.CompilationUnit;
import ch.unibe.scg.nullfinder.collector.CompilationUnitCollector;
import ch.unibe.scg.nullfinder.collector.FeatureCollector;
import ch.unibe.scg.nullfinder.collector.NullCheckCollector;
import ch.unibe.scg.nullfinder.feature.Feature;
import ch.unibe.scg.nullfinder.feature.extractor.UnextractableException;

import com.github.javaparser.ParseException;
import com.github.javaparser.TokenMgrError;

@Component
public class JavaSourceProcessor implements
		ItemProcessor<Path, Entry<CompilationUnit, List<NullCheck>>> {

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
	public Entry<CompilationUnit, List<NullCheck>> process(Path javaSourcePath)
			throws ParseException, TokenMgrError, IOException,
			UnextractableException {
		CompilationUnit compilationUnit = this.compilationUnitCollector
				.collect(javaSourcePath);
		List<NullCheck> nullChecks = this.nullCheckCollector
				.collect(compilationUnit);
		for (NullCheck nullCheck : nullChecks) {
			// TODO handle unextractable
			List<Feature> features = this.featureCollector.collect(nullCheck);
			nullCheck.getFeatures().addAll(features);
		}
		return new Entry<CompilationUnit, List<NullCheck>>() {

			private CompilationUnit key = compilationUnit;
			private List<NullCheck> value = nullChecks;

			@Override
			public CompilationUnit getKey() {
				return this.key;
			}

			@Override
			public List<NullCheck> getValue() {
				return this.value;
			}

			@Override
			public List<NullCheck> setValue(List<NullCheck> value) {
				List<NullCheck> oldValue = this.value;
				this.value = value;
				return oldValue;
			}

		};
	}

}

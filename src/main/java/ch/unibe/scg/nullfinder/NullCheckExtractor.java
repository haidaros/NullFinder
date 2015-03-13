package ch.unibe.scg.nullfinder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.unibe.scg.nullfinder.collector.FeatureCollector;
import ch.unibe.scg.nullfinder.collector.NullCheckCollector;
import ch.unibe.scg.nullfinder.feature.Feature;
import ch.unibe.scg.nullfinder.feature.reason.Reason;
import ch.unibe.scg.nullfinder.jpa.repository.IFeatureRepository;
import ch.unibe.scg.nullfinder.jpa.repository.INullCheckRepository;
import ch.unibe.scg.nullfinder.jpa.repository.IReasonRepository;

@Component
public class NullCheckExtractor {

	@Autowired
	protected INullCheckRepository nullCheckRepository;
	@Autowired
	protected IFeatureRepository featureRepository;
	@Autowired
	protected IReasonRepository reasonRepository;
	protected NullCheckCollector checkCollector;
	protected FeatureCollector featureCollector;

	public NullCheckExtractor() throws NoSuchMethodException,
			SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		this.checkCollector = new NullCheckCollector();
		this.featureCollector = new FeatureCollector();
	}

	public Stream<Feature> extract(Path root) throws IOException,
			NoSuchMethodException, SecurityException {
		return Files.walk(root).filter(this::isJavaSource)
				.flatMap(this::collectNullChecks).peek(this::saveNullCheck)
				.flatMap(this::collectFeatures).peek(this::saveFeature);
	}

	protected boolean isJavaSource(Path path) {
		return path.getFileName().toString().endsWith(".java");
	}

	protected Stream<NullCheck> collectNullChecks(Path path) {
		try {
			return this.checkCollector.collect(path).stream();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return Stream.of();
	}

	protected Stream<Feature> collectFeatures(NullCheck check) {
		try {
			return this.featureCollector.collect(check).stream();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return Stream.of();
	}

	protected void saveNullCheck(NullCheck check) {
		this.nullCheckRepository.save(check);
	}

	protected void saveFeature(Feature feature) {
		this.featureRepository.save(feature);
		for (Reason reason : feature.getReasons()) {
			this.reasonRepository.save(reason);
		}
	}

}

package ch.unibe.scg.nullfinder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.unibe.scg.nullfinder.ast.CompilationUnit;
import ch.unibe.scg.nullfinder.ast.Node;
import ch.unibe.scg.nullfinder.collector.CompilationUnitCollector;
import ch.unibe.scg.nullfinder.collector.FeatureCollector;
import ch.unibe.scg.nullfinder.collector.NullCheckCollector;
import ch.unibe.scg.nullfinder.feature.Feature;
import ch.unibe.scg.nullfinder.feature.reason.FeatureReason;
import ch.unibe.scg.nullfinder.feature.reason.NodeReason;
import ch.unibe.scg.nullfinder.feature.reason.Reason;
import ch.unibe.scg.nullfinder.jpa.repository.IFeatureRepository;
import ch.unibe.scg.nullfinder.jpa.repository.INodeRepository;
import ch.unibe.scg.nullfinder.jpa.repository.INullCheckRepository;
import ch.unibe.scg.nullfinder.jpa.repository.IReasonRepository;

@Component
public class NullCheckExtractor {

	@Autowired
	protected INodeRepository nodeRepository;
	@Autowired
	protected INullCheckRepository nullCheckRepository;
	@Autowired
	protected IFeatureRepository featureRepository;
	@Autowired
	protected IReasonRepository reasonRepository;
	protected CompilationUnitCollector compilationUnitCollector;
	protected NullCheckCollector checkCollector;
	protected FeatureCollector featureCollector;

	public NullCheckExtractor() throws NoSuchMethodException,
			SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		this.compilationUnitCollector = new CompilationUnitCollector();
		this.checkCollector = new NullCheckCollector();
		this.featureCollector = new FeatureCollector();
	}

	public Stream<Feature> extract(Path root) throws IOException,
			NoSuchMethodException, SecurityException {
		return Files.walk(root).filter(this::isJavaSource)
				.flatMap(this::collectCompilationUnit)
				.peek(this::saveCompilationUnit)
				.flatMap(this::collectNullChecks).peek(this::saveNullCheck)
				.flatMap(this::collectFeatures).peek(this::saveFeature);
	}

	protected boolean isJavaSource(Path path) {
		return path.getFileName().toString().endsWith(".java");
	}

	protected Stream<CompilationUnit> collectCompilationUnit(Path path) {
		try {
			return Stream.of(this.compilationUnitCollector.collect(path));
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return Stream.of();
	}

	protected Stream<NullCheck> collectNullChecks(
			CompilationUnit compilationUnit) {
		try {
			return this.checkCollector.collect(compilationUnit).stream();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return Stream.of();
	}

	protected Stream<Feature> collectFeatures(NullCheck nullCheck) {
		try {
			return this.featureCollector.collect(nullCheck).stream();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return Stream.of();
	}

	protected void saveCompilationUnit(CompilationUnit compilationUnit) {
		this.nodeRepository.save(compilationUnit);
	}

	protected void saveNullCheck(NullCheck nullCheck) {
		this.nodeRepository.save(nullCheck.getNode());
		this.nullCheckRepository.save(nullCheck);
	}

	protected void saveFeature(Feature feature) {
		this.featureRepository.save(feature);
		for (Reason reason : feature.getReasons()) {
			this.saveReason(reason);
		}
	}

	protected void saveReason(Reason reason) {
		// TODO naughty boy!
		if (reason instanceof FeatureReason) {
			this.saveFeatureReason((FeatureReason) reason);
		}
		if (reason instanceof NodeReason) {
			this.saveNodeReason((NodeReason) reason);
		}
	}

	protected void saveFeatureReason(FeatureReason reason) {
		this.reasonRepository.save(reason);
	}

	protected void saveNodeReason(NodeReason reason) {
		reason.setNode(this.getExistingOrNewNode(reason.getNode()));
		this.nodeRepository.save(reason.getNode());
		this.reasonRepository.save(reason);
	}

	protected Node getExistingOrNewNode(Node node) {
		List<Node> nodes = this.nodeRepository
				.findByCompilationUnitAndClassNameAndBeginLineAndBeginColumnAndEndLineAndEndColumn(
						node.getCompilationUnit(), node.getClassName(),
						node.getBeginLine(), node.getBeginColumn(),
						node.getEndLine(), node.getEndColumn());
		assert nodes.size() <= 1;
		if (!nodes.isEmpty()) {
			return nodes.get(0);
		}
		return node;
	}

}

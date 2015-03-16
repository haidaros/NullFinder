package ch.unibe.scg.nullfinder.collector;

import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.ast.CompilationUnit;
import ch.unibe.scg.nullfinder.feature.Feature;
import ch.unibe.scg.nullfinder.feature.extractor.UnextractableException;
import ch.unibe.scg.nullfinder.feature.extractor.level0.ArrayAccessExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.level0.CastExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.level0.EnclosedExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.level0.FieldAccessExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.level0.MethodCallExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.level0.NameExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.level1.LocalVariableExtractor;

public class FeatureCollectorTest {

	private CompilationUnitCollector compilationUnitCollector;
	private NullCheckCollector checkCollector;
	private FeatureCollector featureCollector;
	private List<NullCheck> nullChecks;

	@Before
	public void setUp() throws Exception {
		this.compilationUnitCollector = new CompilationUnitCollector();
		this.checkCollector = new NullCheckCollector();
		this.featureCollector = new FeatureCollector();
		URL url = this.getClass().getResource("../TestNullClass.java");
		CompilationUnit compilationUnit = this.compilationUnitCollector
				.collect(Paths.get(url.toURI()));
		this.nullChecks = this.checkCollector.collect(compilationUnit);
	}

	@Test
	public void testFieldAccessNullCheck() throws UnextractableException {
		Optional<NullCheck> match = this.nullChecks
				.stream()
				.filter(nullCheck -> nullCheck.getNode().getJavaParserNode()
						.getParentNode().toString()
						.equals("this.field == null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck nullCheck = match.get();
		List<Feature> features = this.featureCollector.collect(nullCheck);
		Assert.assertEquals(features.size(), 1);
		Assert.assertTrue(features.iterator().next().getExtractor() instanceof FieldAccessExtractor);
	}

	@Test
	public void testArrayAccessNullCheck() throws UnextractableException {
		Optional<NullCheck> match = this.nullChecks
				.stream()
				.filter(nullCheck -> nullCheck.getNode().getJavaParserNode()
						.getParentNode().toString().equals("array[0] == null"))
				.findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck nullCheck = match.get();
		List<Feature> features = this.featureCollector.collect(nullCheck);
		Assert.assertEquals(features.size(), 1);
		Assert.assertTrue(features.iterator().next().getExtractor() instanceof ArrayAccessExtractor);
	}

	@Test
	public void testEnclosedNullCheck() throws UnextractableException {
		Optional<NullCheck> match = this.nullChecks
				.stream()
				.filter(nullCheck -> nullCheck.getNode().getJavaParserNode()
						.getParentNode().toString()
						.equals("(value = this) != null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck nullCheck = match.get();
		List<Feature> features = this.featureCollector.collect(nullCheck);
		Assert.assertEquals(features.size(), 1);
		Assert.assertTrue(features.iterator().next().getExtractor() instanceof EnclosedExtractor);
	}

	@Test
	public void testCastNullCheck() throws UnextractableException {
		Optional<NullCheck> match = this.nullChecks
				.stream()
				.filter(nullCheck -> nullCheck.getNode().getJavaParserNode()
						.getParentNode().toString()
						.equals("(String) value != null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck nullCheck = match.get();
		List<Feature> features = this.featureCollector.collect(nullCheck);
		Assert.assertEquals(features.size(), 1);
		Assert.assertTrue(features.iterator().next().getExtractor() instanceof CastExtractor);
	}

	@Test
	public void testNameNullCheck() throws UnextractableException {
		Optional<NullCheck> match = this.nullChecks
				.stream()
				.filter(nullCheck -> nullCheck.getNode().getJavaParserNode()
						.getParentNode().toString().equals("name == null"))
				.findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck nullCheck = match.get();
		List<Feature> features = this.featureCollector.collect(nullCheck);
		Assert.assertEquals(features.size(), 2);
		Assert.assertTrue(features.stream().anyMatch(
				feature -> feature.getExtractor() instanceof NameExtractor));
		Assert.assertTrue(features
				.stream()
				.anyMatch(
						feature -> feature.getExtractor() instanceof LocalVariableExtractor));
	}

	@Test
	public void testMethodCallNullCheck() throws UnextractableException {
		Optional<NullCheck> match = this.nullChecks
				.stream()
				.filter(nullCheck -> nullCheck.getNode().getJavaParserNode()
						.getParentNode().toString()
						.equals("this.getNull() == null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck nullCheck = match.get();
		List<Feature> features = this.featureCollector.collect(nullCheck);
		Assert.assertEquals(features.size(), 1);
		Assert.assertTrue(features.iterator().next().getExtractor() instanceof MethodCallExtractor);
	}

}

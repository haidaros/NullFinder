package ch.unibe.scg.nullfinder.collector;

import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ch.unibe.scg.nullfinder.feature.extractor.UnextractableException;
import ch.unibe.scg.nullfinder.feature.extractor.level0.ArrayAccessComparandExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.level0.CastComparandExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.level0.EnclosedComparandExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.level0.FieldAccessComparandExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.level0.MethodCallComparandExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.level0.NameComparandExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.level1.LocalVariableComparandExtractor;
import ch.unibe.scg.nullfinder.jpa.entity.CompilationUnit;
import ch.unibe.scg.nullfinder.jpa.entity.Feature;
import ch.unibe.scg.nullfinder.jpa.entity.NullCheck;

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
		Assert.assertTrue(features.iterator().next().getExtractor() instanceof FieldAccessComparandExtractor);
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
		Assert.assertTrue(features.iterator().next().getExtractor() instanceof ArrayAccessComparandExtractor);
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
		Assert.assertTrue(features.iterator().next().getExtractor() instanceof EnclosedComparandExtractor);
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
		Assert.assertTrue(features.iterator().next().getExtractor() instanceof CastComparandExtractor);
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
				feature -> feature.getExtractor() instanceof NameComparandExtractor));
		Assert.assertTrue(features
				.stream()
				.anyMatch(
						feature -> feature.getExtractor() instanceof LocalVariableComparandExtractor));
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
		Assert.assertTrue(features.iterator().next().getExtractor() instanceof MethodCallComparandExtractor);
	}

}

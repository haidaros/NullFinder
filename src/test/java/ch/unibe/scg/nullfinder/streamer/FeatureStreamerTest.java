package ch.unibe.scg.nullfinder.streamer;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.feature.IFeature;
import ch.unibe.scg.nullfinder.feature.extractor.UnextractableException;
import ch.unibe.scg.nullfinder.feature.extractor.level0.ArrayAccessExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.level0.CastExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.level0.EnclosedExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.level0.FieldAccessExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.level0.MethodCallExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.level0.NameExtractor;
import ch.unibe.scg.nullfinder.feature.extractor.level1.LocalVariableExtractor;

public class FeatureStreamerTest {

	private NullCheckStreamer checkStreamer;
	private FeatureStreamer featureStreamer;
	private Stream<NullCheck> checks;

	@Before
	public void setUp() throws Exception {
		this.checkStreamer = new NullCheckStreamer();
		this.featureStreamer = new FeatureStreamer();
		URL url = this.getClass().getResource("../TestNullClass.java");
		this.checks = this.checkStreamer.stream(Paths.get(url.toURI()));
	}

	@Test
	public void testFieldAccessNullCheck() throws UnextractableException {
		Optional<NullCheck> match = this.checks.filter(
				check -> check.getNode().getParentNode().toString()
						.equals("this.field == null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck check = match.get();
		Set<IFeature> features = this.featureStreamer.stream(check).collect(
				Collectors.toSet());
		Assert.assertEquals(features.size(), 1);
		Assert.assertTrue(features.iterator().next().getExtractor() instanceof FieldAccessExtractor);
	}

	@Test
	public void testArrayAccessNullCheck() throws UnextractableException {
		Optional<NullCheck> match = this.checks.filter(
				check -> check.getNode().getParentNode().toString()
						.equals("array[0] == null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck check = match.get();
		Set<IFeature> features = this.featureStreamer.stream(check).collect(
				Collectors.toSet());
		Assert.assertEquals(features.size(), 1);
		Assert.assertTrue(features.iterator().next().getExtractor() instanceof ArrayAccessExtractor);
	}

	@Test
	public void testEnclosedNullCheck() throws UnextractableException {
		Optional<NullCheck> match = this.checks.filter(
				check -> check.getNode().getParentNode().toString()
						.equals("(value = this) != null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck check = match.get();
		Set<IFeature> features = this.featureStreamer.stream(check).collect(
				Collectors.toSet());
		Assert.assertEquals(features.size(), 1);
		Assert.assertTrue(features.iterator().next().getExtractor() instanceof EnclosedExtractor);
	}

	@Test
	public void testCastNullCheck() throws UnextractableException {
		Optional<NullCheck> match = this.checks.filter(
				check -> check.getNode().getParentNode().toString()
						.equals("(String) value != null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck check = match.get();
		Set<IFeature> features = this.featureStreamer.stream(check).collect(
				Collectors.toSet());
		Assert.assertEquals(features.size(), 1);
		Assert.assertTrue(features.iterator().next().getExtractor() instanceof CastExtractor);
	}

	@Test
	public void testNameNullCheck() throws UnextractableException {
		Optional<NullCheck> match = this.checks.filter(
				check -> check.getNode().getParentNode().toString()
						.equals("name == null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck check = match.get();
		Set<IFeature> features = this.featureStreamer.stream(check).collect(
				Collectors.toSet());
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
		Optional<NullCheck> match = this.checks.filter(
				check -> check.getNode().getParentNode().toString()
						.equals("this.getNull() == null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck check = match.get();
		Set<IFeature> features = this.featureStreamer.stream(check).collect(
				Collectors.toSet());
		Assert.assertEquals(features.size(), 1);
		Assert.assertTrue(features.iterator().next().getExtractor() instanceof MethodCallExtractor);
	}

}

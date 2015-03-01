package ch.unibe.scg.nullfinder;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ch.unibe.scg.nullfinder.classifier.AbstractNameNullCheckClassifier;
import ch.unibe.scg.nullfinder.classifier.ArrayAccessNullCheckClassifier;
import ch.unibe.scg.nullfinder.classifier.CastNullCheckClassifier;
import ch.unibe.scg.nullfinder.classifier.EnclosedNullCheckClassifier;
import ch.unibe.scg.nullfinder.classifier.FieldAccessNullCheckClassifier;
import ch.unibe.scg.nullfinder.classifier.MethodCallNullCheckClassifier;
import ch.unibe.scg.nullfinder.classifier.UnclassifiableNullCheckException;

public class NullCheckClassifierTest {

	private NullCheckSelector selector;
	private NullCheckClassifier classifier;
	private Stream<NullCheck> checks;

	@Before
	public void setUp() throws Exception {
		this.selector = new NullCheckSelector();
		this.classifier = new NullCheckClassifier();
		URL url = this.getClass().getResource("TestNullClass.java");
		this.checks = this.selector.selectAll(Paths.get(url.toURI()));
	}

	@Test
	public void testFieldAccessNullCheck() throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, UnclassifiableNullCheckException {
		Optional<NullCheck> match = this.checks.filter(
				check -> check.getNode().getParentNode().toString()
						.equals("this.field == null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck check = match.get();
		Set<NullCheckClassification> classifications = this.classifier
				.classifyAll(check).collect(Collectors.toSet());
		Assert.assertEquals(classifications.size(), 1);
		Assert.assertTrue(classifications.iterator().next().getClassifier() instanceof FieldAccessNullCheckClassifier);
	}

	@Test
	public void testArrayAccessNullCheck() throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, UnclassifiableNullCheckException {
		Optional<NullCheck> match = this.checks.filter(
				check -> check.getNode().getParentNode().toString()
						.equals("array[0] == null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck check = match.get();
		Set<NullCheckClassification> classifications = this.classifier
				.classifyAll(check).collect(Collectors.toSet());
		Assert.assertEquals(classifications.size(), 1);
		Assert.assertTrue(classifications.iterator().next().getClassifier() instanceof ArrayAccessNullCheckClassifier);
	}

	@Test
	public void testEnclosedNullCheck() throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, UnclassifiableNullCheckException {
		Optional<NullCheck> match = this.checks.filter(
				check -> check.getNode().getParentNode().toString()
						.equals("(value = this) != null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck check = match.get();
		Set<NullCheckClassification> classifications = this.classifier
				.classifyAll(check).collect(Collectors.toSet());
		Assert.assertEquals(classifications.size(), 1);
		Assert.assertTrue(classifications.iterator().next().getClassifier() instanceof EnclosedNullCheckClassifier);
	}

	@Test
	public void testCastNullCheck() throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, UnclassifiableNullCheckException {
		Optional<NullCheck> match = this.checks.filter(
				check -> check.getNode().getParentNode().toString()
						.equals("(String) value != null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck check = match.get();
		Set<NullCheckClassification> classifications = this.classifier
				.classifyAll(check).collect(Collectors.toSet());
		Assert.assertEquals(classifications.size(), 1);
		Assert.assertTrue(classifications.iterator().next().getClassifier() instanceof CastNullCheckClassifier);
	}

	@Test
	public void testNameNullCheck() throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, UnclassifiableNullCheckException {
		Optional<NullCheck> match = this.checks.filter(
				check -> check.getNode().getParentNode().toString()
						.equals("name == null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck check = match.get();
		Set<NullCheckClassification> classifications = this.classifier
				.classifyAll(check).collect(Collectors.toSet());
		Assert.assertEquals(classifications.size(), 1);
		Assert.assertTrue(classifications.iterator().next().getClassifier() instanceof AbstractNameNullCheckClassifier);
	}

	@Test
	public void testMethodCallNullCheck() throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, UnclassifiableNullCheckException {
		Optional<NullCheck> match = this.checks.filter(
				check -> check.getNode().getParentNode().toString()
						.equals("this.getNull() == null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck check = match.get();
		Set<NullCheckClassification> classifications = this.classifier
				.classifyAll(check).collect(Collectors.toSet());
		Assert.assertEquals(classifications.size(), 1);
		Assert.assertTrue(classifications.iterator().next().getClassifier() instanceof MethodCallNullCheckClassifier);
	}

}

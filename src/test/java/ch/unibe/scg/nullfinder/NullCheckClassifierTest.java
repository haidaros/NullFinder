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

import ch.unibe.scg.nullfinder.classification.ArrayAccessNullCheckClassification;
import ch.unibe.scg.nullfinder.classification.CastNullCheckClassification;
import ch.unibe.scg.nullfinder.classification.EnclosedNullCheckClassification;
import ch.unibe.scg.nullfinder.classification.FieldAccessNullCheckClassification;
import ch.unibe.scg.nullfinder.classification.INullCheckClassification;
import ch.unibe.scg.nullfinder.classification.MethodCallNullCheckClassification;
import ch.unibe.scg.nullfinder.classification.NameNullCheckClassification;
import ch.unibe.scg.nullfinder.classification.UnclassifiableNullCheckException;

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
		Set<INullCheckClassification> classifications = this.classifier
				.classifyAll(check).collect(Collectors.toSet());
		Assert.assertEquals(classifications.size(), 1);
		Assert.assertTrue(classifications.iterator().next() instanceof FieldAccessNullCheckClassification);
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
		Set<INullCheckClassification> classifications = this.classifier
				.classifyAll(check).collect(Collectors.toSet());
		Assert.assertEquals(classifications.size(), 1);
		Assert.assertTrue(classifications.iterator().next() instanceof ArrayAccessNullCheckClassification);
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
		Set<INullCheckClassification> classifications = this.classifier
				.classifyAll(check).collect(Collectors.toSet());
		Assert.assertEquals(classifications.size(), 1);
		Assert.assertTrue(classifications.iterator().next() instanceof EnclosedNullCheckClassification);
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
		Set<INullCheckClassification> classifications = this.classifier
				.classifyAll(check).collect(Collectors.toSet());
		Assert.assertEquals(classifications.size(), 1);
		Assert.assertTrue(classifications.iterator().next() instanceof CastNullCheckClassification);
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
		Set<INullCheckClassification> classifications = this.classifier
				.classifyAll(check).collect(Collectors.toSet());
		Assert.assertEquals(classifications.size(), 1);
		Assert.assertTrue(classifications.iterator().next() instanceof NameNullCheckClassification);
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
		Set<INullCheckClassification> classifications = this.classifier
				.classifyAll(check).collect(Collectors.toSet());
		Assert.assertEquals(classifications.size(), 1);
		Assert.assertTrue(classifications.iterator().next() instanceof MethodCallNullCheckClassification);
	}

}

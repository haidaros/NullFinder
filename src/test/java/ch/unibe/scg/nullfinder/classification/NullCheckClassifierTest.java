package ch.unibe.scg.nullfinder.classification;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.NullCheckFinder;

public class NullCheckClassifierTest {

	private NullCheckFinder finder;
	private NullCheckClassifier classifier;
	private Collection<NullCheck> checks;

	@Before
	public void setUp() throws Exception {
		this.finder = new NullCheckFinder();
		this.classifier = new NullCheckClassifier();
		URL url = this.getClass().getResource("../TestNullClass.java");
		this.checks = this.finder.find(Paths.get(url.toURI()));
	}

	@Test
	public void testFieldAccessNullCheck() throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, UnclassifiableNullCheckException {
		Optional<NullCheck> match = this.checks
				.stream()
				.filter(check -> check.getNode().getParentNode().toString()
						.equals("this.field == null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck check = match.get();
		INullCheckClassification classification = this.classifier
				.classify(check);
		Assert.assertTrue(classification instanceof FieldAccessNullCheckClassification);
	}

	@Test
	public void testArrayAccessNullCheck() throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, UnclassifiableNullCheckException {
		Optional<NullCheck> match = this.checks
				.stream()
				.filter(check -> check.getNode().getParentNode().toString()
						.equals("array[0] == null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck check = match.get();
		INullCheckClassification classification = this.classifier
				.classify(check);
		Assert.assertTrue(classification instanceof ArrayAccessNullCheckClassification);
	}

	@Test
	public void testEnclosedNullCheck() throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, UnclassifiableNullCheckException {
		Optional<NullCheck> match = this.checks
				.stream()
				.filter(check -> check.getNode().getParentNode().toString()
						.equals("(value = this) != null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck check = match.get();
		INullCheckClassification classification = this.classifier
				.classify(check);
		Assert.assertTrue(classification instanceof EnclosedNullCheckClassification);
	}

	@Test
	public void testCastNullCheck() throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, UnclassifiableNullCheckException {
		Optional<NullCheck> match = this.checks
				.stream()
				.filter(check -> check.getNode().getParentNode().toString()
						.equals("(String) value != null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck check = match.get();
		INullCheckClassification classification = this.classifier
				.classify(check);
		Assert.assertTrue(classification instanceof CastNullCheckClassification);
	}

	@Test
	public void testNameNullCheck() throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, UnclassifiableNullCheckException {
		Optional<NullCheck> match = this.checks
				.stream()
				.filter(check -> check.getNode().getParentNode().toString()
						.equals("name == null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck check = match.get();
		INullCheckClassification classification = this.classifier
				.classify(check);
		Assert.assertTrue(classification instanceof NameNullCheckClassification);
	}

	@Test
	public void testMethodCallNullCheck() throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, UnclassifiableNullCheckException {
		Optional<NullCheck> match = this.checks
				.stream()
				.filter(check -> check.getNode().getParentNode().toString()
						.equals("this.getNull() == null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck check = match.get();
		INullCheckClassification classification = this.classifier
				.classify(check);
		Assert.assertTrue(classification instanceof MethodCallNullCheckClassification);
	}

}

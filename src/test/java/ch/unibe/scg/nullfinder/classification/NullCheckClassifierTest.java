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

	private NullCheckFinder nullCheckFinder;
	private NullCheckClassifier nullCheckClassifier;
	private Collection<NullCheck> nullChecks;

	@Before
	public void setUp() throws Exception {
		this.nullCheckFinder = new NullCheckFinder();
		this.nullCheckClassifier = new NullCheckClassifier();
		URL url = this.getClass().getResource("../TestNullClass.java");
		this.nullChecks = this.nullCheckFinder.find(Paths.get(url.toURI()));
	}

	@Test
	public void testFieldAccessNullCheck() throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, UnclassifiableNullCheckException {
		Optional<NullCheck> match = this.nullChecks
				.stream()
				.filter(nullCheck -> nullCheck.getNode().getParentNode()
						.toString().equals("this.field == null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck nullCheck = match.get();
		INullCheckClassification classification = this.nullCheckClassifier
				.classify(nullCheck);
		Assert.assertTrue(classification instanceof FieldAccessNullCheckClassification);
	}

	@Test
	public void testArrayAccessNullCheck() throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, UnclassifiableNullCheckException {
		Optional<NullCheck> match = this.nullChecks
				.stream()
				.filter(nullCheck -> nullCheck.getNode().getParentNode()
						.toString().equals("array[0] == null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck nullCheck = match.get();
		INullCheckClassification classification = this.nullCheckClassifier
				.classify(nullCheck);
		Assert.assertTrue(classification instanceof ArrayAccessNullCheckClassification);
	}

	@Test
	public void testNameNullCheck() throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, UnclassifiableNullCheckException {
		Optional<NullCheck> match = this.nullChecks
				.stream()
				.filter(nullCheck -> nullCheck.getNode().getParentNode()
						.toString().equals("name == null")).findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck nullCheck = match.get();
		INullCheckClassification classification = this.nullCheckClassifier
				.classify(nullCheck);
		Assert.assertTrue(classification instanceof NameNullCheckClassification);
	}

	@Test
	public void testMethodCallNullCheck() throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, UnclassifiableNullCheckException {
		Optional<NullCheck> match = this.nullChecks
				.stream()
				.filter(nullCheck -> nullCheck.getNode().getParentNode()
						.toString().equals("this.getNull() == null"))
				.findFirst();
		Assert.assertTrue(match.isPresent());
		NullCheck nullCheck = match.get();
		INullCheckClassification classification = this.nullCheckClassifier
				.classify(nullCheck);
		Assert.assertTrue(classification instanceof MethodCallNullCheckClassification);
	}

}

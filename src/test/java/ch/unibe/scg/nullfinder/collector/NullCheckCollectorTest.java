package ch.unibe.scg.nullfinder.collector;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ch.unibe.scg.nullfinder.NullCheck;

public class NullCheckCollectorTest {

	private NullCheckCollector checkCollector;
	private Set<NullCheck> checks;

	@Before
	public void setUp() throws Exception {
		this.checkCollector = new NullCheckCollector();
		URL url = this.getClass().getResource("../TestNullClass.java");
		this.checks = this.checkCollector.collect(Paths.get(url.toURI()));
	}

	@Test
	public void testSetUp() {
		Assert.assertEquals(this.checks.stream().count(), 6);
	}

	@Test
	public void testFieldAccessNullCheck() {
		boolean found = this.checks.stream().anyMatch(
				check -> check.getNode().getParentNode().toString()
						.equals("this.field == null"));
		Assert.assertTrue(found);
	}

	@Test
	public void testArrayAccessNullCheck() {
		boolean found = this.checks.stream().anyMatch(
				check -> check.getNode().getParentNode().toString()
						.equals("array[0] == null"));
		Assert.assertTrue(found);
	}

	@Test
	public void testEnclosedNullCheck() {
		boolean found = this.checks.stream().anyMatch(
				check -> check.getNode().getParentNode().toString()
						.equals("(value = this) != null"));
		Assert.assertTrue(found);
	}

	@Test
	public void testCastNullCheck() {
		boolean found = this.checks.stream().anyMatch(
				check -> check.getNode().getParentNode().toString()
						.equals("(String) value != null"));
		Assert.assertTrue(found);
	}

	@Test
	public void testNameNullCheck() {
		boolean found = this.checks.stream().anyMatch(
				check -> check.getNode().getParentNode().toString()
						.equals("name == null"));
		Assert.assertTrue(found);
	}

	@Test
	public void testMethodCallNullCheck() {
		boolean found = this.checks.stream().anyMatch(
				check -> check.getNode().getParentNode().toString()
						.equals("this.getNull() == null"));
		Assert.assertTrue(found);
	}

}

package ch.unibe.scg.nullfinder;

import java.io.File;
import java.net.URL;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NullCheckFinderTest {

	private NullCheckFinder nullCheckFinder;
	private Collection<NullCheck> nullChecks;

	@Before
	public void setUp() throws Exception {
		this.nullCheckFinder = new NullCheckFinder();
		URL url = this.getClass().getResource("TestNullClass.java");
		File file = new File(url.getFile());
		this.nullChecks = this.nullCheckFinder.findNullChecks(file);
	}

	@Test
	public void testSetUp() {
		Assert.assertEquals(this.nullChecks.size(), 3);
	}

	@Test
	public void testFieldAccessNullCheck() {
		boolean found = this.nullChecks.stream().anyMatch(
				nullCheck -> nullCheck.getNode().getParentNode().toString()
						.equals("this.field == null"));
		Assert.assertTrue(found);
	}

	@Test
	public void testNameNullCheck() {
		boolean found = this.nullChecks.stream().anyMatch(
				nullCheck -> nullCheck.getNode().getParentNode().toString()
						.equals("name == null"));
		Assert.assertTrue(found);
	}

	@Test
	public void testMethodCallNullCheck() {
		boolean found = this.nullChecks.stream().anyMatch(
				nullCheck -> nullCheck.getNode().getParentNode().toString()
						.equals("this.getNull() == null"));
		Assert.assertTrue(found);
	}

}

package ch.unibe.scg.nullfinder;

import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class NullFinderTest {

	private NullFinder nullFinder;
	private Collection<NullCheck> nullChecks;

	@Before
	public void setUp() throws Exception {
		this.nullFinder = new NullFinder();
		URL url = this.getClass().getResource("TestNullClass.java");
		File file = new File(url.getFile());
		this.nullChecks = this.nullFinder.findNullChecks(file);
	}

	@Test
	public void testCheckInstanceVariable() {
		fail("Not yet implemented");
	}

}

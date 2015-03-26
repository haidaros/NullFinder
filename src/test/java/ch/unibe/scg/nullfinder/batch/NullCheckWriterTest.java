package ch.unibe.scg.nullfinder.batch;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.unibe.scg.nullfinder.NullCheck;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/application-context.xml")
public class NullCheckWriterTest {
	@Autowired
	NullCheckWriter nullCheckWriter;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		assertNotNull(nullCheckWriter);
		assertNotNull(nullCheckWriter.getFeatureRepository());
		assertNotNull(nullCheckWriter.getNodeRepository());
		assertNotNull(nullCheckWriter.getNullCheckRepository());
		assertNotNull(nullCheckWriter.getReasonRepository());
	}
}

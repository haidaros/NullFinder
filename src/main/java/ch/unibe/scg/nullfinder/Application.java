package ch.unibe.scg.nullfinder;

import org.springframework.batch.core.launch.support.CommandLineJobRunner;
import org.springframework.context.annotation.ImportResource;

@ImportResource("classpath:META-INF/application-context.xml")
public class Application extends CommandLineJobRunner {

}

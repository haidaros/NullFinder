package ch.unibe.scg.nullfinder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import ch.unibe.scg.nullfinder.feature.Feature;

@SpringBootApplication
@ImportResource("classpath:META-INF/application-context.xml")
@EnableJpaRepositories
public class Application implements CommandLineRunner {

	public static void main(String[] arguments) {
		SpringApplication.run(Application.class, arguments);
	}

	@Autowired
	protected NullCheckExtractor extractor;

	@Override
	public void run(String... arguments) throws ParseException, IOException {
		Options options = new Options();
		CommandLineParser parser = new BasicParser();
		CommandLine commandLine = parser.parse(options, arguments);
		String[] inputs = commandLine.getArgs();
		if (inputs.length == 0) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("NullFinder [options] input [input...]",
					options);
			System.exit(1);
		}
		long before = System.currentTimeMillis();
		System.out.println("PENDING processing...");
		Stream.of(inputs).map(Paths::get).flatMap(this::extractAll).count();
		long after = System.currentTimeMillis();
		System.out.println(String.format("DONE in %d seconds",
				(after - before) / 1000));
	}

	protected Stream<Feature> extractAll(Path path) {
		try {
			return this.extractor.extract(path);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return Stream.of();
	}

}

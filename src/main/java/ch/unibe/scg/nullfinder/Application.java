package ch.unibe.scg.nullfinder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ImportResource("classpath:META-INF/application-context.xml")
@PropertySource("classpath:application.properties")
public class Application implements CommandLineRunner {

	public static void main(String[] arguments) {
		SpringApplication.run(Application.class, arguments);
	}

	@Autowired
	protected NullCheckExtractor extractor;

	@Override
	public void run(String... arguments) throws ParseException, IOException {
		Options options = new Options();
		options.addOption(new Option("o", "output", true,
				"CSV file to ouput to, defaults to stdout"));
		CommandLineParser parser = new BasicParser();
		CommandLine commandLine = parser.parse(options, arguments);
		BufferedWriter output = new BufferedWriter(new OutputStreamWriter(
				System.out));
		boolean hasOutput = commandLine.hasOption("output");
		if (hasOutput) {
			Path outputPath = Paths.get(commandLine.getOptionValue("output"));
			output = Files.newBufferedWriter(outputPath);
		}
		String[] inputs = commandLine.getArgs();
		if (inputs.length == 0) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("NullFinder [options] input [input...]",
					options);
			System.exit(1);
		}
		BufferedWriter writer = output;
		long before = System.currentTimeMillis();
		System.out.println("PENDING processing...");
		Stream.of(inputs).map(Paths::get).flatMap(this::extractAll)
				.forEach(line -> this.write(writer, line));
		writer.flush();
		if (hasOutput) {
			writer.close();
		}
		long after = System.currentTimeMillis();
		System.out.println(String.format("DONE in %d seconds",
				(after - before) / 1000));
	}

	protected void write(BufferedWriter writer, String line) {
		try {
			writer.write(line);
			writer.newLine();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	protected Stream<String> extractAll(Path path) {
		try {
			return this.extractor.extract(path);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return Stream.of();
	}

}

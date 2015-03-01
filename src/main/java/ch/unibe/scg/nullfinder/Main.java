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

public class Main {

	public static void main(String[] arguments) throws ParseException,
			IOException {
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
		Stream.of(inputs).map(Paths::get).flatMap(Main::extractAll)
				.forEach(line -> Main.write(writer, line));
		writer.flush();
		if (hasOutput) {
			writer.close();
		}
		long after = System.currentTimeMillis();
		System.out.println(String.format("DONE in %d seconds",
				(after - before) / 1000));
	}

	private static void write(BufferedWriter writer, String line) {
		try {
			writer.write(line);
			writer.newLine();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	private static Stream<String> extractAll(Path path) {
		try {
			NullCheckExtractor extractor = new NullCheckExtractor();
			return extractor.extract(path);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		return Stream.of();
	}

}

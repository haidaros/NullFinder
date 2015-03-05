package ch.unibe.scg.nullfinder.streamer;

import java.util.stream.Stream;

public interface IStreamer<SOURCE, TARGET> {

	Stream<TARGET> stream(SOURCE source) throws Exception;

}

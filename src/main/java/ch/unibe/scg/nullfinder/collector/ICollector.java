package ch.unibe.scg.nullfinder.collector;

@Deprecated
public interface ICollector<SOURCE, TARGET> {

	TARGET collect(SOURCE source) throws Exception;

}

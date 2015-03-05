package ch.unibe.scg.nullfinder.collector;


public interface ICollector<SOURCE, TARGET> {

	TARGET collect(SOURCE source) throws Exception;

}

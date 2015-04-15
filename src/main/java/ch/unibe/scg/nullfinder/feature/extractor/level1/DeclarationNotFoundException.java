package ch.unibe.scg.nullfinder.feature.extractor.level1;

import ch.unibe.scg.nullfinder.jpa.entity.Node;

public class DeclarationNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public DeclarationNotFoundException(Node variableExtractorNode) {
		super(variableExtractorNode.getJavaParserNode().toString());
	}

}

package ch.unibe.scg.nullfinder.batch;

import java.util.List;
import java.util.Map.Entry;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.unibe.scg.nullfinder.NullCheck;
import ch.unibe.scg.nullfinder.ast.CompilationUnit;
import ch.unibe.scg.nullfinder.jpa.repository.NodeRepository;
import ch.unibe.scg.nullfinder.jpa.repository.NullCheckRepository;

@Component
public class CompilationUnitWriter implements
		ItemWriter<Entry<CompilationUnit, List<NullCheck>>> {

	@Autowired
	protected NodeRepository nodeRepository;
	@Autowired
	protected NullCheckRepository nullCheckRepository;

	@Override
	public void write(
			List<? extends Entry<CompilationUnit, List<NullCheck>>> entries)
			throws Exception {
		for (Entry<CompilationUnit, List<NullCheck>> entry : entries) {
			this.nodeRepository.save(entry.getKey());
			for (NullCheck nullCheck : entry.getValue()) {
				this.nullCheckRepository.save(nullCheck);
			}
		}
	}

}
